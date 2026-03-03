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

    public Member login(String id, String password) {

        //DB에서 회원 조회
        Member member = memberRepository
                .findById(id)
                .orElse(null);

        //회원 없으면 실패
        if (member == null) {
            return null;
        }

        //비밀번호 다르면 실패
        if (!member.getPassword().equals(password)) {
            return null;
        }

        //성공
        return member;
    }
}