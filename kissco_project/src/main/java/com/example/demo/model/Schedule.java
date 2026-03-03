package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SCHEDULE")
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_seq_gen")
	@SequenceGenerator(
	        name = "schedule_seq_gen",
	        sequenceName = "SCHEDULE_SEQ",
	        allocationSize = 1
	)
	@Column(name = "ID")
	private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "DEADLINE")
    private LocalDateTime deadline;

    @Column(name = "IMPORTANCE")
    private Integer importance;

    @Column(name = "ALERT_ENABLED")
    private String alertEnabled;

    @Column(name = "IS_NOTIFIED")
    private String isNotified;

    @Column(name = "IS_COMPLETED")
    private String isCompleted;

    @Column(name = "MEMBER_ID")
    private String memberId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

	public Integer getImportance() {
		return importance;
	}

	public void setImportance(Integer importance) {
		this.importance = importance;
	}

	public String getAlertEnabled() {
		return alertEnabled;
	}

	public void setAlertEnabled(String alertEnabled) {
		this.alertEnabled = alertEnabled;
	}

	public String getIsNotified() {
		return isNotified;
	}

	public void setIsNotified(String isNotified) {
		this.isNotified = isNotified;
	}

	public String getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

    
}