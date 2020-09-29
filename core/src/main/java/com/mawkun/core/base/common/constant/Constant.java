package com.mawkun.core.base.common.constant;

//系统常量
public interface Constant {
    int STATUS_NO = 0;
    int STATUS_YES = 1;

    //=============================管理员类型=============================//
    int ADMIN_TYPE_SUPER = 1;   //主管理员
    int ADMIN_TYPE_COMMON = 2;  //普通管理员

    //=============================用户类型及状态=============================//
    int USER_TYPE_CUSTOMER = 1;     //顾客
    int USER_TYPE_DISTRIBUTOR = 2;  //配送员
    int USER_TYPE_ADMIN = 3;        //管理员

    int USER_STATUS_LOCK = 0;       //用户封锁
    int USER_STATUS_ACTIVE = 1;     //用户激活

    //=============================下单类型=============================//
    int ORDER_DELIVERY_SEND = 0;    //客户送至门店
    int ORDER_DELIVERY_GET = 1;     //配送员上门取货

    //=============================充值订单状态=============================//
    int INVEST_ORDER_WAITING_WAY = 1;   //待支付
    int INVEST_ORDER_FINISH = 2 ;       //已完成

    //=============================新旧订单状态=============================//
    int ORDER_NEW = 1;
    int ORDER_OLD = 2;

    //=============================配送订单状态（用户）=============================//
    int ORDER_STATUS_WAITING_PAY = 1;   //待支付

    int DELIVERY_ORDER_WAITING_REAP = 2;    //待收货
    int DELIVERY_ORDER_SURE_TAKE = 3;       //确认收货
    int DELIVERY_ORDER_CLEANING = 4;        //洗涤中
    int DELIVERY_ORDER_WAITING_TAKE = 5;    //待送达
    int DELIVERY_ORDER_SURE_FINISH = 6;       //已完成


    int SELF_ORDER_WAITING_SEND = 2;    //待送达门店
    int SELF_ORDER_SURE_SEND = 3;       //确认送达
    int SELF_ORDER_CLEANING = 4;        //洗涤中
    int SELF_ORDER_WAITING_TAKE = 6;    //待取货
    int SELF_ORDER_SURE_TAKE = 5;       //已完成

    int ORDER_STATUS_CANCEL = 7;        //订单取消

    //=============================订单状态（用户）=============================//

    //=============================配送订单状态（实际）=============================//


    //=============================配送订单状态=============================//


    //=============================自提订单状态=============================//
    int ORDER_WAITING_PAY = 1;   //待支付

    //=============================收货员订单状态=============================//
    int DISTRIBUTOR_ORDER_WAITING_TAKING = 1;   //待接单
    int DISTRIBUTOR_ORDER_SURE_TAKE = 2;        //已接单
    int DISTRIBUTOR_ORDER_TAKED = 3;            //已确认收货
    int DISTRIBUTOR_ORDER_SERVICE = 4;          //确认送达

    //=============================订单类型=============================//
    int ORDER_TYPE_GOODS = 1;   //商品订单
    int ORDER_TYPE_INVEST = 2;  //充值订单

    //=============================系统参数状态=============================//
    int SYS_PARAM_OPEN = 1;     //开启
    int SYS_PARAM_CLOSE = 0;    //关闭

    //=============================支付类型================================//
    int PAY_WITH_WEIXIN = 0;    //微信支付
    int PAY_WITH_ZHIFUBAO = 1;  //支付宝支付
    int PAY_WITH_CASH = 2;      //现金支付
    int PAY_WITH_REMAINDER = 4; //余额支付
    //=============================商品状态================================//
    int GOODS_GROUNDING = 0;    //上架
    int GOODS_UNDERCARRIAGE = 1;//下架
    //=============================充值方式================================//
    int RECHARGE_WITH_MONEY = 1;    //直接使用金额
    int RECHARGE_WITH_CARD = 2;     //购买充值卡

    String INVEST_WITH_MONEY = "用户金额充值";
    String INVEST_WITH_CARD = "充值卡";
    //=============================运费等级配置=============================//
    String TRANSPORT_LEVEL1 = "transport_level1";
    String TRANSPORT_LEVEL2 = "transport_level2";
    String TRANSPORT_LEVEL3 = "transport_level3";
    String TRANSPORT_LEVEL4 = "transport_level4";

    //=============================充值卡券状态=============================//
    int MEMBER_CART_ON = 0;     //上架
    int MEMBER_CART_DOWN = 1;   //下架

    //=============================用户地址状态=============================//
    int USER_ADDRESS_USED = 0;      //默认地址
    int USER_ADDRESS_UNUSED = 1;
    //=============================系统用户状态=============================
    //用户登录和请求token权限返回状态
    int LOGIN_NOTFIND = 40000;
    //用户被锁定
    int LOGIN_LOCKED = 40001;
    //token失效
    int LOGIN_TIME_OUT = 40002;
    //没有权限,操作失败
    int LOGIN_AUTHORITY = 40003;
    //没有token授权码
    int LOGIN_TOKEN_EMPTY = 40005;

    //=============================支付状态=============================
    //状态10:支付成功,20:等待退款,30:退款失败,40:退款成功 50:拒绝退款
    String PAY_STATU_SUCCESS = "SUCCESS";
    int PAY_STATU_REFUND_WAIT = 20;
    int PAY_STATU_REFUND_FAIL = 30;
    int PAY_STATU_REFUND_SUCCESS = 30;
    int PAY_STATU_REFUND_REJECT = 50;

    //=============================购物车信息状态=============================

    //=============================微信返回结果状态=============================
    String WX_RETURN_SUCCESS = "SUCCESS";

    //=============================购物车信息状态=============================
    int SHOP_LEVEL_FIRST = 0;
    //分店
    int SHOP_LEVEL_SECOND = 1;

    //正常
    int SHOP_STATUS_NORMAL = 0;
    //锁定
    int SHOP_STATUS_LOCK = 1;
    //删除
    int SHOP_STATUS_DELETE = 2;
}
