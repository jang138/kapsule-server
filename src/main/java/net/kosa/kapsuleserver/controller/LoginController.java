package net.kosa.kapsuleserver.controller;

import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.base.util.LoginUtil;
import net.kosa.kapsuleserver.entity.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginUtil loginUtil;

    @GetMapping("/status")
    public String getLoginStatus() {
        if (loginUtil.isLogin()) {
            return "사용자가 로그인했습니다.";
        } else {
            return "사용자가 로그인하지 않았습니다.";
        }
    }

    @GetMapping("/ip")
    public String getClientIpAddress() {
        return "클라이언트 IP 주소: " + loginUtil.getClientIpAddress();
    }

    @GetMapping("/member")
    public Member getMember() {
        Member member = loginUtil.getMember();
        if (member != null) {
            return member;
        } else {
            throw new RuntimeException("사용자가 로그인하지 않았거나 멤버를 찾을 수 없습니다.");
        }
    }
}
