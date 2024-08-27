package net.kosa.kapsuleserver.base.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.base.provider.JwtProvider;
import net.kosa.kapsuleserver.entity.CustomOAuth2User;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String kakaoId = oAuth2User.getName();
        Member member = memberService.getMemberByKakaoId(kakaoId);
        String role = String.valueOf(member.getRole());
        String token = jwtProvider.create(kakaoId, role);

        logger.info("token " + token);

        response.sendRedirect("https://localhost:8080/auth/oauth-response/" + token + "/3600");
//        response.sendRedirect("https://192.168.230.28:8080/auth/oauth-response/" + token + "/3600");

//        // HTTP 응답 헤더에 JWT 추가
//        response.setHeader("Authorization", "Bearer " + token);
//
//        // Vue.js 애플리케이션으로 리디렉션
//        response.sendRedirect("http://localhost:8080/auth/oauth-response");

//        // 요청된 호스트 정보 가져오기
//        String scheme = request.getScheme(); // http 또는 https
//        String serverName = request.getServerName(); // localhost 또는 192.168.230.28
//        int serverPort = request.getServerPort(); // 8080
//        String contextPath = "/auth/oauth-response"; // /auth/oauth-response
//
//        System.out.println("eeeeeeeeeeeeeeee" + serverPort);
//
//        // 리디렉션 URL 생성
//        String redirectUrl = String.format("%s://%s:%d%s?token=%s&expires=%d",
//                scheme, serverName, serverPort, contextPath, token, 3600);
//
//        response.sendRedirect(redirectUrl);
    }
}
