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

    @Transient // JPA에서 제외
    private static Map<Integer, String> allocationMap = new HashMap<>();

    private boolean isAllocation = false; // 할당 여부

    private String submissionUrl; // 과제 제출 주소

    private boolean isSubmission = false; // 제출 여부

    @Enumerated(EnumType.STRING)
    private ScoreStatus scoreStatus; // 채점 상태 [Checking, Success]

    @OneToOne
    private Member member;

    static {
        allocationMap.put(1, "https://brief-fan-a81.notion.site/HTTP-18dbd03023834411b0b8dd8c2fcd318e?pvs=4");
        allocationMap.put(2, "https://brief-fan-a81.notion.site/Node-js-083bafc85a3642fcbefcd5b13c29720f?pvs=4");
        allocationMap.put(3, "https://brief-fan-a81.notion.site/d7478b5f4d2547f0bd1f175ebdaae101?pvs=4");
        allocationMap.put(4, "https://brief-fan-a81.notion.site/get-post-4a88e2e4ef5c41afae80a8f994863127?pvs=4");
        allocationMap.put(5, "https://brief-fan-a81.notion.site/1e3afd5ad23841f6a93b02f4c245e221?pvs=4");
        allocationMap.put(6, "https://brief-fan-a81.notion.site/05b47de5c63b45e8983941c16ec8bc22?pvs=4");
        allocationMap.put(7, "https://brief-fan-a81.notion.site/be0e81a2940f42aead45e052ee5f346a?pvs=4");
        allocationMap.put(8, "https://brief-fan-a81.notion.site/a6132390a3ec432fa6547c39733380b7?pvs=4");
        allocationMap.put(9, "https://brief-fan-a81.notion.site/router-middleware-fcb87855f5094c6992888b243623ff17?pvs=4");
        allocationMap.put(10, "https://brief-fan-a81.notion.site/express-Router-46ca4b4d08864caaa54db6194cc52ddd?pvs=4");
        allocationMap.put(11, "https://brief-fan-a81.notion.site/express-session-5b77070db075421e82da889dfdd56326?pvs=4");
    }

    public Task() {
        // 기본 생성자
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

    public static Map<Integer, String> getAllocationMap() {
        return allocationMap;
    }
}
