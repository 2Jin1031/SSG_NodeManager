package ssg.nodemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    private String allocationUrl; // 과제 할당 주소

    private boolean isAllocation; // 할당 여부

    private String submissionUrl; // 과제 제출 주소

    private boolean isSubmission; // 제출 여부

    @Enumerated(EnumType.STRING)
    private ScoreStatus scoreStatus; // 채점 상태 [Checking, Success]

    @OneToOne
    private Member member;
}
