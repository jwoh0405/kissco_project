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

    @PutMapping("/me")
    public String updateMe(@RequestBody Member member,
                           HttpSession session) {

        String loginUser = (String) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "login required";
        }

        memberService.updateMember(loginUser, member);

        return "success";
    }
}