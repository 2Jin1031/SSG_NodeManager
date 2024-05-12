package ssg.nodemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    private boolean hasChangedLoginId;

    private boolean hasChangedPassword;

    @OneToOne(mappedBy = "member")
    private Task task;

    protected Member() {
    }

    public Member(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
        this.currentLevel = 1;
        this.nextLevel = 2;
        this.hasChangedLoginId = false;
        this.hasChangedPassword = false;
    }

    public boolean isCorrectPassword(String password) {
        return Objects.equals(this.getPassword(), password);
    }

    public boolean hasChangedLoginId() {
        return hasChangedLoginId;
    }

    public boolean hasChangedPassword() {
        return hasChangedPassword;
    }

    public void setHasChangedLoginId(boolean hasChangedLoginId) {
        this.hasChangedLoginId = hasChangedLoginId;
    }

    public void setHasChangedPassword(boolean hasChangedPassword) {
        this.hasChangedPassword = hasChangedPassword;
    }

    public void levelUP(int currentLevel) {
        this.currentLevel = currentLevel + 1;
        this.nextLevel = this.currentLevel + 1;
    }

}
