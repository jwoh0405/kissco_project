package com.example.demo.controller;

import com.example.demo.service.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailTestController {

    private final MailService mailService;

    public MailTestController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/mail/test")
    public String send(@RequestParam String to) {
        mailService.sendTest(to);
        return "메일 발송 완료: " + to;
    }
}