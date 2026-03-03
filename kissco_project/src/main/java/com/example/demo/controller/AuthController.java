package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.AuthService;
import com.example.demo.model.Member;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody Member request,
                        HttpSession session) {

        Member member = authService.login(
                request.getId(),
                request.getPassword()
        );

        if (member == null) {
            return "fail";
        }

        // 세션에 로그인 정보 저장
        session.setAttribute("loginUser", member.getId());

        return "success";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "success";
    }
    
    
}