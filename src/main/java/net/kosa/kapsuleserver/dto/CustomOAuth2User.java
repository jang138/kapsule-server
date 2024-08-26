//package net.kosa.kapsuleserver.dto;
//
//import lombok.RequiredArgsConstructor;
//import net.kosa.kapsuleserver.entity.Member;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Map;
//
//@RequiredArgsConstructor
//public class CustomOAuth2User implements OAuth2User {
//
//    private final Member member;
//    private final Map<String, Object> attributes;
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return attributes; // OAuth2 사용자 속성 반환
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(member.getRole().name()));
//        return authorities;
//    }
//
//    @Override
//    public String getName() {
//        return member.getNickname();
//    }
//
//    public String getUsername() {
//        return member.getKakaoId();
//    }
//}
