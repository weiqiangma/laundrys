package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.DateUtils;
import cn.pertech.common.utils.NumberUtils;
import com.alibaba.fastjson.JSONObject;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsOrderQuery;
import com.mawkun.core.base.entity.*;
import com.mawkun.core.dao.ShopUserDaoExt;
import com.mawkun.core.dao.SysParamDaoExt;
import com.mawkun.core.service.*;
import com.mawkun.core.spring.annotation.LoginedAuth;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

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
    private ShopServiceExt shopServiceExt;
    @Autowired
    private GoodsOrderServiceExt goodsOrderServiceExt;
    //添加事务第一步 引入platformTransactionManager对象
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    @Autowired
    private ShopUserDaoExt shopUserDaoExt;

    @GetMapping("/get")
    @ApiOperation(value="门店详情", notes="门店详情")
    public JsonResult getById(Long id) {
        ShoppingCart shoppingCart = shoppingCartServiceExt.getById(id);
        return sendSuccess(shoppingCart);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="门店详情", notes="门店详情")
    public JsonResult getByEntity(@LoginedAuth UserSession session, ShoppingCart shoppingCart) {
        if(session.getId() > 0) shoppingCart.setUserId(session.getId());
        ShoppingCart cart = shoppingCartServiceExt.getByEntity(shoppingCart);
        return sendSuccess(cart);
    }

    @GetMapping("/list")
    @ApiOperation(value="购物车列表", notes="购物车列表")
    public JsonResult list(@LoginedAuth UserSession session, ShoppingCart shoppingCart) {
        if(session.getId() > 0) shoppingCart.setUserId(session.getId());
        List<ShoppingCart> shoppingCartList = shoppingCartServiceExt.listByEntity(shoppingCart);
        return sendSuccess(shoppingCartList);
    }

    @PostMapping("/insert")
    @ApiOperation(value="购物车添加商品", notes="购物车添加商品")
    public JsonResult insert(@LoginedAuth UserSession session, Long goodsId, Integer goodsNum){
        Assert.notNull(goodsId);
        shoppingCartServiceExt.save(session, goodsId, goodsNum);
        return sendSuccess("ok");
    }

    @PutMapping("/update")
    @ApiOperation(value="编辑购物车", notes="编辑购物车")
    public JsonResult update(ShoppingCart shoppingCart){
        int result = shoppingCartServiceExt.update(shoppingCart);
        return sendSuccess(result);
    }


    @PostMapping("/deleteByUserId")
    @ApiOperation(value="删除购物车", notes="删除购物车")
    public JsonResult deleteByUserId(@LoginedAuth UserSession session) {
        int result = shoppingCartServiceExt.deleteByUserId(session.getId());
        return sendSuccess(result);
    }

    @GetMapping("/countTransportFee")
    @ApiOperation(value="计算运费", notes="计算运费")
    public JsonResult countTransportFee(@LoginedAuth UserSession session, Long addressId, Long shopId, Integer amount) {
        /**
         * 1.根据用户收货地址及门店坐标计算距离
         * 2.根据距离计算运费
         */
        JSONObject object = new JSONObject();
        UserAddress userAddress = userAddressServiceExt.getById(addressId);
        Shop shop = shopServiceExt.getById(shopId);
        String location = userAddress.getLocation();
        String distanceStr = gaoDeApiServiceExt.getDistanceWithUserAndShop(location, shop.getLocation());
        double resultAmount = shoppingCartServiceExt.getAmountByUserId(session.getId());
        if(resultAmount != amount) return sendArgsError("所选商品价格和购物车内商品价格不一致,请重新添加");
        Integer distance = NumberUtils.str2Int(distanceStr);
        List<SysParam> paramList = sysParamDaoExt.selectTransportFee();
        List<SysParam> sortList = paramList.stream().sorted(Comparator.comparingInt(SysParam::getDistance)).collect(Collectors.toList());
        for(int i = 0; i < sortList.size() - 1; i++) {
            int front = sortList.get(i).getDistance() * 1000;
            int next = sortList.get(i+1).getDistance() * 1000;
            int max = sortList.get(paramList.size() -1).getDistance() * 1000;
            if(distance >= max) return sendArgsError("所选地址附近洗衣店即将开放，请耐心等待");
            if(distance < front) {
                int fee = 0;
                int feeDiff = 0;
                int lowAmount = sortList.get(i).getLowAmount();
                if(amount >= lowAmount) {
                    object.put("transportFee", 0);
                    object.put("feeDiff", "");
                    return sendSuccess(object);
                }
                String feeStr = sortList.get(i).getSysValue();
                fee = NumberUtils.str2Int(feeStr);
                feeDiff = lowAmount - amount;
                object.put("transportFee", fee);
                object.put("feeDiff", feeDiff);
                break;
            }
            if(distance >= front && distance < next) {
                int fee = 0;
                int feeDiff = 0;
                int lowAmount = sortList.get(i).getLowAmount();
                if(amount >= lowAmount) {
                    object.put("transportFee", 0);
                    object.put("feeDiff", feeDiff);
                    return sendSuccess(object);
                }
                String feeStr = sortList.get(i).getSysValue();
                fee = NumberUtils.str2Int(feeStr);
                feeDiff = lowAmount - amount;
                object.put("transportFee", fee);
                object.put("feeDiff", feeDiff);
                break;
            }
        }
        return sendSuccess(object);
    }

    @PostMapping("/countOrderForm")
    public JsonResult countGoodsOrder(@LoginedAuth UserSession session, GoodsOrderQuery query) {
        /**
         * 1.查询用户是否存在
         * 2.查询用户购物车商品总金额与前端传入是否相同
         * 3.判断用户是否使用积分抵消消费金额，如果使用减去相应积分
         * 4.判断用户运费生成最终金额
         */
//        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
//        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        GoodsOrder resultOrder = new GoodsOrder();
        User user = userServiceExt.getById(session.getId());
        if (user == null) {
            //transactionManager.commit(status);
            return sendArgsError("数据库中未查询到该用户信息,请联系管理员");
        }
        List<ShoppingCart> cartList = shoppingCartServiceExt.findByUserId(user.getId());
        if (cartList.isEmpty()) {
            //transactionManager.commit(status);
            return sendArgsError("购物车内无商品信息,请先添加");
        }
        //计算购物车内商品总价格+运费
        long resultAmount = 0;
        for (ShoppingCart cart : cartList) {
            Long price = cart.getGoodsPrice();
            Integer goodsNum = cart.getGoodsNum();
            Long goodsAmount = price * goodsNum;
            resultAmount = goodsAmount + resultAmount;
        }
        if(resultAmount != query.getAmount()){
            //transactionManager.commit(status);
            return sendArgsError("购物车内所选商品和实际商品价格不符,请重新添加");
        }
        Shop shop = shopServiceExt.getById(query.getShopId());
        if (shop == null) {
            //transactionManager.commit(status);
            return sendArgsError("所选店铺不存在,请重新选择");
        }
        try {
            //顾客送至门店
            if (query.getTransportWay() == Constant.ORDER_DELIVERY_SEND) {
                //判断是不是余额支付，如果是更新用户余额
                if (query.getSumOfMoney() != null && query.getSumOfMoney() > 0) {
                    Long sumOfMoney = user.getSumOfMoney();
                    if (sumOfMoney >= resultAmount) {
                        sumOfMoney = sumOfMoney - resultAmount;
                        user.setSumOfMoney(sumOfMoney);
                        //生成待支付订单
                        resultOrder = goodsOrderServiceExt.generateWaitingPayOrderForm(user, shop, query, null, resultAmount, cartList, Constant.PAY_WITH_REMAINDER);
                        //更新订单状态
                        resultOrder.setStatus(Constant.SELF_ORDER_WAITING_SEND);
                        resultOrder.setIsnew(Constant.ORDER_NEW);
                        goodsOrderServiceExt.update(session, resultOrder);
                        String timeEnd = DateUtils.format("yyyy-MM-dd HH:mm:ss", resultOrder.getCreateTime());
                        List<String> openIdList = shopUserDaoExt.selectDistorOpenIdByShopId(resultOrder.getShopId());
                        wxApiServiceExt.sendOrderPaySuccessNotice(user, resultOrder, timeEnd, user.getOpenId());
                        wxApiServiceExt.sendDistributorOrderTakeNotice(user, resultOrder, timeEnd, openIdList);
                        //transactionManager.commit(status);
                        return sendSuccess("支付成功");
                    } else {
                        //transactionManager.commit(status);
                        return sendArgsError("您的余额已不足,请充值或选择其它支付方式");
                    }
                } else {
                    //微信支付先生成订单
                    resultOrder = goodsOrderServiceExt.generateWaitingPayOrderForm(user, shop, query, null, resultAmount, cartList, Constant.PAY_WITH_WEIXIN);
                }
            }
            //配送员上门取货
            if (query.getTransportWay() == Constant.ORDER_DELIVERY_GET) {
                UserAddress address = userAddressServiceExt.getByIdAndUserId(query.getAddressId(), user.getId());
                if (address == null) {
                    //transactionManager.commit(status);
                    return sendArgsError("未查询到该收获地址,请选择其它地址或重新添加");
                }
                Long transportFee = shoppingCartServiceExt.countTransportFee(shop, address, resultAmount);
                if (!transportFee.equals(query.getTransportFee())) {
                    //transactionManager.commit(status);
                    return sendArgsError("运费计算有误");
                }
                //商品总价+运费
                resultAmount = resultAmount + transportFee;
                //判断是不是余额支付，如果是更新用户余额
                if (query.getSumOfMoney() != null && query.getSumOfMoney() > 0) {
                    Long sumOfMoney = user.getSumOfMoney();
                    if (!user.getSumOfMoney().equals(sumOfMoney)) {
                        //transactionManager.commit(status);
                        return sendArgsError("用户余额不一致");
                    }
                    if (sumOfMoney >= resultAmount) {
                        sumOfMoney = sumOfMoney - resultAmount;
                        user.setSumOfMoney(sumOfMoney);
                        //生成待支付订单
                        resultOrder = goodsOrderServiceExt.generateWaitingPayOrderForm(user, shop, query, address, resultAmount, cartList, Constant.PAY_WITH_REMAINDER);
                        resultOrder.setStatus(Constant.DELIVERY_ORDER_WAITING_REAP);
                        resultOrder.setIsnew(Constant.ORDER_NEW);
                        goodsOrderServiceExt.update(session, resultOrder);
                        //transactionManager.commit(status);
                        String timeEnd = DateUtils.format("yyyy-MM-dd HH:mm:ss", resultOrder.getCreateTime());
                        List<String> openIdList = shopUserDaoExt.selectDistorOpenIdByShopId(resultOrder.getShopId());
                        wxApiServiceExt.sendOrderPaySuccessNotice(user, resultOrder, timeEnd, user.getOpenId());
                        wxApiServiceExt.sendDistributorOrderTakeNotice(user, resultOrder, timeEnd, openIdList);
                        return sendSuccess("支付成功");
                    } else {
                        //transactionManager.commit(status);
                        return sendArgsError("您的余额已不足,请充值或选择其它支付方式");
                    }
                } else {
                    resultOrder = goodsOrderServiceExt.generateWaitingPayOrderForm(user, shop, query, address, resultAmount, cartList, Constant.PAY_WITH_WEIXIN);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //transactionManager.rollback(status);
        }
        //transactionManager.commit(status);
        if(resultOrder.getId() != null) return sendSuccess(resultOrder.getId());
        return sendArgsError("下单失败");
    }
}