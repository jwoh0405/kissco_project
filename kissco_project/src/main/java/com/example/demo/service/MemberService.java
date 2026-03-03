package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void updateMember(String id, Member updated) {

        Member member = memberRepository.findById(id).orElse(null);

        if (member == null) {
            throw new RuntimeException("회원 없음");
        }

        member.setEmail(updated.getEmail());
        member.setPassword(updated.getPassword());
        member.setName(updated.getName());

        memberRepository.save(member);
    }
}