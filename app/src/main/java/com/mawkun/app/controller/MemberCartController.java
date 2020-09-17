package com.mawkun.app.controller;

import com.mawkun.core.base.entity.MemberCart;
import com.mawkun.core.base.service.MemberCartService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 22:45:50
 */
@RestController
@RequestMapping("/memberCart")
public class MemberCartController {
    
    @Autowired
    private MemberCartService memberCartService;

    @GetMapping("/get/{id}")
    public MemberCart getById(@PathVariable Long id) {
        MemberCart memberCart = memberCartService.getById(id);
        return memberCart!=null?memberCart:new MemberCart();
    }

    @GetMapping("/get")
    public MemberCart getByEntity(MemberCart memberCart) {
        return memberCartService.getByEntity(memberCart);
    }

    @GetMapping("/list")
    public List<MemberCart> list(MemberCart memberCart) {
        List<MemberCart> memberCartList = memberCartService.listByEntity(memberCart);
        return memberCartList;
    }

    @PostMapping("/insert")
    public MemberCart insert(@RequestBody MemberCart memberCart){
        memberCartService.insert(memberCart);
        return memberCart;
    }

    @PutMapping("/update")
    public int update(@RequestBody MemberCart memberCart){
        return memberCartService.update(memberCart);
    }

    @DeleteMapping("/delete/{id}")
    public int deleteOne(@PathVariable Long id){
        return memberCartService.deleteById(id);
    }

    @DeleteMapping("/delete")
    public int deleteBatch(@RequestBody List<Long> ids){
        int result = 0;
        if (ids!=null&&ids.size()>0) result = memberCartService.deleteByIds(ids);
        return result;
    }

}