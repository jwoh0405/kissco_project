package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "member_seq_gen")
    @SequenceGenerator(
            name = "member_seq_gen",
            sequenceName = "MEMBER_SEQ",
            allocationSize = 1)
    private Long memberNo;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

	public Long getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(Long memberNo) {
		this.memberNo = memberNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    
}