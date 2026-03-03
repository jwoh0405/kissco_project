package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.repository.MemberRepository;
import com.example.demo.model.Member;

@Service
public class AuthService {

    private final MemberRepository memberRepository;

    // 생성자
    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member login(String email, String password) {

        Member member = memberRepository
                .findByEmail(email)
                .orElse(null);

        if (member != null &&
            member.getPassword().equals(password)) {

            return member;
        }

        return null;
    }
}