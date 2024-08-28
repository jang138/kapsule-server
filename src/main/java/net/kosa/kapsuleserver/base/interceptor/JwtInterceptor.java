package net.kosa.kapsuleserver.base.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.base.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // JWT가 유효한지 확인
        if (!jwtUtil.isTokenValid()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing JWT");
            return false;
        }

        // 사용자 ID를 request에 추가
        String kakaoId = jwtUtil.getKakaoIdFromRequest();
        request.setAttribute("kakaoId", kakaoId);

        System.out.println("Yes I'm Interceptor!!!!!!!!!");

        return true;
    }
}

