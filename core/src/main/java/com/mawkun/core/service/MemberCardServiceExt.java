package com.mawkun.core.service;

import com.mawkun.core.base.entity.MemberCard;
import com.mawkun.core.base.service.MemberCardService;
import com.mawkun.core.dao.MemberCardDaoExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mawkun
 * @date 2020-09-17 22:42:31
 */
@Service
public class MemberCardServiceExt extends MemberCardService {

    @Autowired
    private MemberCardDaoExt memberCardDaoExt;

    public MemberCard findByIdAndStatus(Long cartId, Integer status) {
        MemberCard cart = new MemberCard();
        cart.setId(cartId);
        cart.setStatus(status);
        return memberCardDaoExt.getByEntity(cart);
    }

}