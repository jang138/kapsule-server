package net.kosa.kapsuleserver.base.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.kosa.kapsuleserver.base.provider.JwtProvider;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequiredArgsConstructor
public class LoginUtil {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    private final HttpServletRequest request;

    private String getTokenFromRequest() {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 이후의 토큰 부분 추출
        }
        return null;
    }

    public boolean isLogin() {
        String token = getTokenFromRequest();
        return token != null && jwtProvider.validate(token) != null;
    }

    public Member getMember() {
        String token = getTokenFromRequest();
        if (token == null) {
            return null;
        }
        String kakaoId = jwtProvider.getIdFromJwt(token);
        return memberService.getMemberByKakaoId(kakaoId);
    }
}