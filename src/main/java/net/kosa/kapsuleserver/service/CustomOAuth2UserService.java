//package net.kosa.kapsuleserver.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import net.kosa.kapsuleserver.base.entity.Role;
//import net.kosa.kapsuleserver.dto.CustomOAuth2User;
//import net.kosa.kapsuleserver.entity.Member;
//import net.kosa.kapsuleserver.repository.MemberRepository;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//@Log4j2
//@RequiredArgsConstructor
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//    private final MemberRepository memberRepository;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//
//        // 카카오로부터 사용자 정보를 가져옴
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//
//        log.info("카카오 사용자 정보: " + attributes);
//
//        // 카카오에서 반환된 사용자 정보를 처리
//        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
//
//        String kakaoId = String.valueOf(attributes.get("id"));
//        String nickname = (String) profile.get("nickname");
//
//        // 데이터베이스에서 카카오 ID로 사용자 조회
//        Member member = memberRepository.findByKakaoId(kakaoId)
//                .orElseGet(() -> registerNewMember(kakaoId, nickname, kakaoAccount));
//
//        // CustomOAuth2User 객체를 반환하여 인증된 사용자로 처리
//        return new CustomOAuth2User(member, attributes);
//    }
//
//    private Member registerNewMember(String kakaoId, String nickname, Map<String, Object> kakaoAccount) {
//        Member newMember = Member.builder()
//                .kakaoId(kakaoId)
//                .nickname(nickname)
//                .role(Role.ROLE_FREEUSER)  // 기본 역할 설정
//                .build();
//
//        return memberRepository.save(newMember);
//    }
//}
