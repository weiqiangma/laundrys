package com.mawkun.core.base.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.entity.Admin;
import com.mawkun.core.base.entity.User;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSession {

    private Long id;

    private Long shopId;

    private String userName;

    private String realName;

    private String password;

    private String mobile;

    private Long sumOfMoney;

    private String address;

    private Integer integral;

    private Integer kind;

    private Integer level;

    private String token;

    private String openId;

    private String sessionKey;

    private List<Long> shopIdList;

    public UserSession(){}

    public UserSession(String token, Admin admin) {
        this.token = token;
        this.id = admin.getId();
        this.shopId = admin.getShopId();
        this.userName = admin.getUserName();
        this.password = admin.getPassword();
        this.mobile = admin.getMobile();
        this.level = admin.getLevel();
        this.kind = Constant.USER_TYPE_ADMIN;
    }

    public UserSession(User user) {
        this.userName = user.getUserName();
        this.realName = user.getRealName();
        this.mobile = user.getMobile();
        this.address = user.getAddress();
        this.sumOfMoney = user.getSumOfMoney();
        this.integral = user.getIntegral();
        this.kind = user.getKind();
    }

    public UserSession(String token, WxLoginResultData data) {
        this.id = data.getUserId();
        this.token = token;
        this.openId = data.getOpenId();
        this.sessionKey = data.getSessionKey();
        this.kind = data.getKind();
        this.shopIdList = data.getShopIdList();
    }

    public boolean isAdmin() {
        return this.kind == Constant.USER_TYPE_ADMIN;
    }

    public boolean isSuperAdmin() {
        return (this.isAdmin() && this.level == 0);
    }

    public boolean isCustomer() {
        return this.getKind() == Constant.USER_TYPE_CUSTOMER;
    }

    public boolean isDistributor() {
        boolean result =  false;
        if(this.getKind() != null && this.getKind() == Constant.USER_TYPE_DISTRIBUTOR) {
            result = true;
        }
        if(this.getLevel() != null && this.getLevel() == Constant.ADMIN_TYPE_DISTRIBUTOR) {
            result = true;
        }
        return result;
    }
}
