package ssg.nodemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter @Setter
public class Task {

    @Id
    @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER) // 이 어노테이션을 추가
    @CollectionTable(name = "task_allocation_map", joinColumns = @JoinColumn(name = "task_id"))
    @MapKeyColumn(name = "level") // Map의 키를 저장할 컬럼
    @Column(name = "url") // Map의 값(URL)을 저장할 컬럼
    private Map<Integer, String> allocationMapByLevel = new HashMap<>();

    private boolean isAllocation = false; // 할당 여부

    private String submissionUrl; // 과제 제출 주소

    private boolean isSubmission = false; // 제출 여부

    @Enumerated(EnumType.STRING)
    private ScoreStatus scoreStatus; // 채점 상태 [Checking, Success]

    @OneToOne
    private Member member;

    public Task() {
        this.makeMap();
    }

    public void makeMap() {
        allocationMapByLevel.put(1, "https://brief-fan-a81.notion.site/HTTP-b03eeadc33b445a686caf1fa8058363c");
        allocationMapByLevel.put(2, "https://brief-fan-a81.notion.site/d138e0a913824cf08c1674d2ce0fd862?v=20a350f2f1fb471ab77b6d399d561b4a&p=18c00643c7c14fd8b8e827c8b0a7c5c0&pm=s");
        allocationMapByLevel.put(3, "https://www.notion.so/SSG-04b1e57a3e5d425fbccb67c37d420e10?p=3e92bebd34ed421db47a30e81ab04f46&pm=s");
        allocationMapByLevel.put(4, "https://www.notion.so/SSG-04b1e57a3e5d425fbccb67c37d420e10?p=e421b6c48e5a4cde8308924f4ff2938c&pm=s");
    }

    public void submission(String submissionUrl) {
        this.submissionUrl = submissionUrl;
        this.isSubmission = true;
        this.scoreStatus = ScoreStatus.Checking;
    }

    public void clear() {
        this.submissionUrl = null;
        this.isSubmission = false;
    }
}
