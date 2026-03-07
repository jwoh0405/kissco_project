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
    
    public Member findById(Long memberNo){

        return memberRepository.findById(memberNo).orElse(null);

    }

    public void updateMember(Long memberNo, Member updated) {

        Member member = memberRepository.findById(memberNo).orElse(null);

        if (member == null) {
            throw new RuntimeException("회원 없음");
        }

        member.setEmail(updated.getEmail());
        member.setName(updated.getName());

        // 비밀번호가 입력된 경우에만 변경
        if(updated.getPassword() != null && !updated.getPassword().isEmpty()){
            member.setPassword(updated.getPassword());
        }

        memberRepository.save(member);
    }
}