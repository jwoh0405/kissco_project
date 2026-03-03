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
    
    public void register(Member member) {
        memberRepository.save(member);
    }

    public void updateMember(Long memberNo, Member updated) {

        Member member = memberRepository.findById(memberNo).orElse(null);

        if (member == null) {
            throw new RuntimeException("회원 없음");
        }

        member.setEmail(updated.getEmail());
        member.setPassword(updated.getPassword());
        member.setName(updated.getName());

        memberRepository.save(member);
    }
}