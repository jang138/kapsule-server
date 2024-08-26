package net.kosa.kapsuleserver.base.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Log4j2
@Component
@RequestScope
public class LoginUtil {
    private final MemberService memberService;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    @Getter
    private HttpSession session;
    private OAuth2User oAuth2User;
    private Member member;

    public LoginUtil(HttpServletRequest req, HttpServletResponse resp, HttpSession session, MemberService memberService) {
        this.req = req;
        this.resp = resp;
        this.session = session;
        this.memberService = memberService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof OAuth2User oUser) {
            this.oAuth2User = oUser;
        } else {
            this.oAuth2User = null;
        }
    }

    public boolean isLogin() {
        return this.oAuth2User != null;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    public String getClientIpAddress() {
        return req.getRemoteAddr();
    }

    public Member getMember() {
        if (isLogout()) {
            return null;
        }

        if (member == null) {
            String oAuthId = oAuth2User.getAttribute("id");
            member = memberService.getUserByKakaoId(oAuthId);
        }

        return member;
    }
}
