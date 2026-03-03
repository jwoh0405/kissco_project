package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}