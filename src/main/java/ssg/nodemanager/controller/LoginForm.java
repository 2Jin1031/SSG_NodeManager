package ssg.nodemanager.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {

    @NotEmpty(message = "회원 아이디를 입력해주세요.")
    private String memberId;
    private String password;
}
