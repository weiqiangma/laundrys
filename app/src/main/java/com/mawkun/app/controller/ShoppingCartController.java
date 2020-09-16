package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.NumberUtils;
import com.alibaba.fastjson.JSONObject;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.OrderFormQuery;
import com.mawkun.core.base.data.query.ShoppingCartQuery;
import com.mawkun.core.base.data.vo.ShopVo;
import com.mawkun.core.base.entity.*;
import com.mawkun.core.base.service.ShoppingCartService;
import com.mawkun.core.dao.SysParamDaoExt;
import com.mawkun.core.service.*;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.mawkun.core.utils.StringUtils;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.NumberUtil;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validation;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags={"购物车操作接口"})
@RequestMapping("/api/shoppingCart")
public class ShoppingCartController extends BaseController {
    
    @Autowired
    private ShoppingCartServiceExt shoppingCartServiceExt;
    @Autowired
    private GaoDeApiServiceExt gaoDeApiServiceExt;
    @Autowired
    private SysParamDaoExt sysParamDaoExt;
    @Autowired
    private UserAddressServiceExt userAddressServiceExt;
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private OrderFormServiceExt orderFormServiceExt;

    @GetMapping("/get")
    public JsonResult getById(Long id) {
        ShoppingCart shoppingCart = shoppingCartServiceExt.getById(id);
        return sendSuccess(shoppingCart);
    }

    @GetMapping("/getByEntity")
    public JsonResult getByEntity(@LoginedAuth UserSession session, ShoppingCart shoppingCart) {
        if(session.getId() > 0) shoppingCart.setUserId(session.getId());
        ShoppingCart cart = shoppingCartServiceExt.getByEntity(shoppingCart);
        return sendSuccess(cart);
    }

    @GetMapping("/list")
    public JsonResult list(@LoginedAuth UserSession session, ShoppingCart shoppingCart) {
        if(session.getId() > 0) shoppingCart.setUserId(session.getId());
        List<ShoppingCart> shoppingCartList = shoppingCartServiceExt.listByEntity(shoppingCart);
        //List<ShoppingCart> resutlList = shoppingCartList.stream().filter(a -> a.getStatus() == Constant.GOODS_UNDERCARRIAGE).collect(Collectors.toList());
        return sendSuccess(shoppingCartList);
    }

    @PostMapping("/insert")
    public JsonResult insert(@LoginedAuth UserSession session, Long goodsId, Integer goodsNum){
        Assert.notNull(goodsId);
        shoppingCartServiceExt.save(session, goodsId, goodsNum);
        return sendSuccess("ok");
    }

    @PutMapping("/update")
    public JsonResult update(ShoppingCart shoppingCart){
        int result = shoppingCartServiceExt.update(shoppingCart);
        return sendSuccess(result);
    }


    @PostMapping("/deleteByUserId")
    public JsonResult deleteByUserId(Long userId) {
        int result = shoppingCartServiceExt.deleteByUserId(userId);
        return sendSuccess(result);
    }

    /**
     * 计算运费
     * @param address
     */
    @GetMapping("/countTransportFee")
    public JsonResult countTransportFee(String address, String shopLocation, Integer amount) {
        /**
         * 1.根据用户收货地址及门店坐标计算距离
         * 2.根据距离计算运费
         */
        JSONObject object = new JSONObject();
        String location = gaoDeApiServiceExt.getLalByAddress(address);
        String distanceStr = gaoDeApiServiceExt.getDistanceWithUserAndShop(location, shopLocation);
        Integer distance = NumberUtils.str2Int(distanceStr);
        List<SysParam> paramList = sysParamDaoExt.selectTransportFee();
        List<SysParam> sortList = paramList.stream().sorted(Comparator.comparingInt(SysParam::getDistance)).collect(Collectors.toList());
        for(int i = 0; i < sortList.size() - 1; i++) {
            int front = sortList.get(i).getDistance() * 1000;
            int next = sortList.get(i+1).getDistance() * 1000;
            int max = sortList.get(paramList.size() -1).getDistance() * 1000;
            if(distance >= max) return sendSuccess("ok", "所选地址附近洗衣店正在建设中，请耐心等待");
            if(distance >= front && distance < next) {
                int fee = 0;
                int feeDiff = 0;
                int lowAmount = sortList.get(i).getLowAmount();
                if(amount >= lowAmount) {
                    object.put("fee", 0);
                    object.put("feeDiff", "");
                    return sendSuccess(object);
                }
                String feeStr = sortList.get(i).getSysValue();
                fee = NumberUtils.str2Int(feeStr);
                feeDiff = lowAmount - amount;
                object.put("fee", fee);
                object.put("feeDiff", feeDiff);
                break;
            }
        }
        return sendSuccess(object);
    }

    public JsonResult countOrderForm(@LoginedAuth UserSession session, OrderFormQuery query) {
        /**
         * 1.查询用户是否存在
         * 2.查询用户购物车商品总金额与前端传入是否相同
         * 3.判断用户是否使用积分抵消消费金额，如果使用减去相应积分
         * 4.判断用户运费生成最终金额
         */
        User user = userServiceExt.getById(session.getId());
        if(user == null) return sendArgsError("数据库中未查询到该用户信息,请联系管理员");
        List<ShoppingCart> cartList = shoppingCartServiceExt.findByUserId(user.getId());
        if(cartList.isEmpty()) return sendArgsError("购物车内无商品信息,请先添加");
        double resultAmount = 0;
        for(ShoppingCart cart : cartList) {
            //商品单价，商品数量计算该商品总价
            Double price = cart.getGoodsPrice();
            Integer goodsNum = cart.getGoodsNum();
            double goodsAmount = price * goodsNum;
            resultAmount = goodsAmount + resultAmount;
        }
        if(resultAmount != query.getAmount()) return sendArgsError("所选商品价格和购物车内商品价格不一致,请重新添加");
        if(query.getIntegral() != null && query.getIntegral() > 0) {
            Integer integral = user.getIntegral();
            resultAmount = resultAmount - NumberUtil.div(integral, 100, 1);
        }
        if(query.getTransportFee() != null && query.getTransportFee() > 0) {
            resultAmount = resultAmount + query.getTransportFee();
        }
        //获取用户收货地址
        UserAddress address = userAddressServiceExt.getByIdAndUserId(query.getShopId(), user.getId());
        String city = (address.getCity() != null) ? address.getCity() : "";
        String region = (address.getArea() != null) ? address.getArea() : "";
        String street = (address.getStreet() != null) ? address.getStreet() : "";
        String detail = (address.getDetail() != null) ? address.getDetail() : "";
        String detailAddress = city + region + street + detail;
        //生成订单
        OrderForm form = new OrderForm();
        form.setUserId(user.getId());
        form.setShopId(query.getShopId());
        form.setAddressId(address.getId());
        form.setUserName(user.getUserName());
        form.setRemark(query.getRemark());
        form.setStatus(Constant.ORDER_STATUS_PAID);
        form.setTotalAmount(query.getAmount());
        form.setRealAmount(resultAmount);
        form.setUserAddress(detailAddress);
        form.setTransportWay(query.getTransportWay());
        form.setPayKind(Constant.PAY_WITH_WEIXIN);
        form.setUpdateTime(new Date());
        form.setCreateTime(new Date());
        int orderKey = orderFormServiceExt.insert(form);
        String orderSerial = StringUtils.createOrderFormNo(String.valueOf(orderKey));
        form.setOrderSerial(orderSerial);
        orderFormServiceExt.update(null, form);
        return sendSuccess();
    }

}