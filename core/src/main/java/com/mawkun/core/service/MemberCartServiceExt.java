package com.mawkun.core.service;

import com.mawkun.core.base.dao.MemberCartDao;
import com.mawkun.core.base.entity.MemberCart;
import com.mawkun.core.base.service.MemberCartService;
import com.mawkun.core.dao.MemberCartDaoExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 22:42:31
 */
@Service
public class MemberCartServiceExt extends MemberCartService {

    @Autowired
    private MemberCartDaoExt memberCartDaoExt;

    public MemberCart findByIdAndStatus(Long cartId, Integer status) {
        MemberCart cart = new MemberCart();
        cart.setId(cartId);
        cart.setStatus(status);
        return memberCartDaoExt.getByEntity(cart);
    }

}