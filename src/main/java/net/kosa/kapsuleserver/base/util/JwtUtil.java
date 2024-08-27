package net.kosa.kapsuleserver.base.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.base.provider.JwtProvider;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProvider jwtProvider;
    private final HttpServletRequest request;

    public String getKakaoIdFromRequest() {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwtProvider.getIdFromJwt(token);
        }
        return null;
    }

    public boolean isTokenValid() {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwtProvider.validate(token) != null;
        }
        return false;
    }
}
