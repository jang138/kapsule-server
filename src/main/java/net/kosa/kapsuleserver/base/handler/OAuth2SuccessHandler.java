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

    }
}
