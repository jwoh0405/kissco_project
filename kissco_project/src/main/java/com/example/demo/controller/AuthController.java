package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> login(@RequestBody Member request, HttpSession session) {

        Member member = authService.login(
            request.getEmail(),
            request.getPassword()
        );

        if (member == null) {
            return ResponseEntity.status(401).body("fail");
        }

        session.setAttribute("loginUser", member.getMemberNo());

        return ResponseEntity.ok("success");
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "success";
    }
    
    
}