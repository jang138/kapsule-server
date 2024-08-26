package net.kosa.kapsuleserver.base.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Log4j2
@Component
@RequestScope
public class LoginUtil {
    private final MemberService memberService;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private HttpSession session;
    private OAuth2User oAuth2User;
    private Member member;

    @Value("${secret-key}")
    private String secretKey;

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

    public String getJwtFromRequest() {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims getClaimsFromJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())  // 시크릿 키 설정
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Member getMemberFromJwt() {
        String jwt = getJwtFromRequest();
        System.out.println("jwt eeeeeeeee" + jwt);

        if (jwt != null) {
            Claims claims = getClaimsFromJwt(jwt);
            String userId = claims.getSubject();
            if (userId != null) {
                return memberService.getMemberByKakaoId(userId);
            }
        }
        return null;
    }

    public Member getMember() {
        if (isLogout()) {
            return null;
        }

        if (member == null) {
            member = getMemberFromJwt();  // JWT에서 사용자 정보를 가져옵니다.
        }

        return member;
    }
}
