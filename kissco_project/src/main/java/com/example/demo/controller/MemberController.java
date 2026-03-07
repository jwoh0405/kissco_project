package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Member;
import com.example.demo.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    
    @PostMapping
    public String register(@RequestBody Member member) {

        memberService.register(member);

        return "success";
    }
    
    
    
    @GetMapping("/me")
    public Member getMyInfo(HttpSession session) {

        Long memberNo = (Long) session.getAttribute("loginUser");

        if(memberNo == null){
            return null;
        }

        return memberService.findById(memberNo);
    }
    

    @PutMapping("/me")
    public String updateMe(@RequestBody Member member,
                           HttpSession session) {

        Long memberNo = (Long) session.getAttribute("loginUser");

        if (memberNo == null) {
            return "login required";
        }

        memberService.updateMember(memberNo, member);

        return "success";
    }
}