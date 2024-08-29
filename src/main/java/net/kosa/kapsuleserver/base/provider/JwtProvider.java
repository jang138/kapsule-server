package net.kosa.kapsuleserver.base.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${secret-key}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String create (String kakaoId, String role, String profileImageUrl ){

        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        Key key = getSigningKey();

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(kakaoId)
                .claim("role", role)
                .claim("profileImage", profileImageUrl)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .compact();
    }

    public String validate (String jwt){
        String subject = null;
        Key key = getSigningKey();

        try {
            subject = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return subject;
    }


    // Role 클레임 추출 메서드
    public String getRoleFromJwt(String jwt) {
        Key key = getSigningKey();
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .get("role", String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 토큰의 만료 여부 검증 메서드
    public boolean isTokenExpired(String jwt) {
        Key key = getSigningKey();
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return true; // 만료되었거나 예외 발생 시 true 반환
        }
    }

    // JWT에서 kakaoId를 추출하는 메서드
    public String getIdFromJwt(String jwt) {
        Key key = getSigningKey();
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
