package com.example.demo.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
	
	// 직접 만든 캐시 방지 메서드
    private void setNoCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    // 1. 첫 접속 시 로그인 페이지로 이동
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    // 2. 로그인 페이지 (http://localhost:8081/login)
    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    // 3. 회원가입 페이지 (http://localhost:8081/signup)
    @GetMapping("/signup")
    public String signupPage() {
        return "signup.html";
    }

 // --- 여기서부터 로그인 체크가 필요한 페이지들 ---

    @GetMapping("/dashboard")
    public String dashboardPage(HttpSession session, HttpServletResponse response) {
        setNoCache(response); 
        if (session.getAttribute("loginUser") == null) return "redirect:/login?msg=require";
        return "dashboard.html";
    }

    @GetMapping("/input")
    public String inputPage(HttpSession session, HttpServletResponse response) {
        setNoCache(response); 
        if (session.getAttribute("loginUser") == null) return "redirect:/login?msg=require";
        return "input.html";
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session, HttpServletResponse response) {
        setNoCache(response); 
        if (session.getAttribute("loginUser") == null) return "redirect:/login?msg=require";
        return "profile.html";
    }
}