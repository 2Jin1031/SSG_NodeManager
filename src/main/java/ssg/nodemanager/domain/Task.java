package ssg.nodemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Task {

    @Id
    @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    private String allocationUrl; // 과제 할당 주소

    private boolean isAllocation = false; // 할당 여부

    private String submissionUrl; // 과제 제출 주소

    private boolean isSubmission = false; // 제출 여부

    @Enumerated(EnumType.STRING)
    private ScoreStatus scoreStatus; // 채점 상태 [Checking, Success]

    @OneToOne
    private Member member;

    public Long getId() {
        return id;
    }

    public String getAllocationUrl() {
        return allocationUrl;
    }

    public void setAllocationUrl(String allocationUrl) {
        this.allocationUrl = allocationUrl;
    }

    public boolean isAllocation() {
        return isAllocation;
    }

    public void setAllocation(boolean allocation) {
        isAllocation = allocation;
    }

    public String getSubmissionUrl() {
        return submissionUrl;
    }

    public void setSubmissionUrl(String submissionUrl) {
        this.submissionUrl = submissionUrl;
    }

    public boolean isSubmission() {
        return isSubmission;
    }

    public void setSubmission(boolean submission) {
        isSubmission = submission;
    }

    public ScoreStatus getScoreStatus() {
        return scoreStatus;
    }

    public void setScoreStatus(ScoreStatus scoreStatus) {
        this.scoreStatus = scoreStatus;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
