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

        if(memberRepository.findByEmail(member.getEmail()).isPresent()){
            throw new RuntimeException("이미 사용중인 이메일입니다.");
        }

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

        if(!member.getEmail().equals(updated.getEmail())
            && memberRepository.findByEmail(updated.getEmail()).isPresent()){
            throw new RuntimeException("이미 사용중인 이메일입니다.");
        }

        member.setEmail(updated.getEmail());
        member.setName(updated.getName());

        if(updated.getPassword() != null && !updated.getPassword().isEmpty()){
            member.setPassword(updated.getPassword());
        }

        memberRepository.save(member);
    }
}