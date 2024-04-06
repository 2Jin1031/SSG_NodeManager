package ssg.nodemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String password;

    private int currentLevel;

    private int nextLevel;

    @OneToOne(mappedBy = "member")
    private Task task;

    protected Member() {
    }

    public Member(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
        this.currentLevel = 1;
        this.nextLevel = 2;
    }

    public void levelUP(int currentLevel) {
        this.currentLevel = currentLevel + 1;
        this.nextLevel = this.currentLevel + 1;
    }

}
