package net.kosa.kapsuleserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.base.entity.Role;
import net.kosa.kapsuleserver.entity.CustomOAuth2User;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        try{
            System.out.println("kkeeyy");
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e){
            e.printStackTrace();
        }

        String kakaoId = oAuth2User.getAttributes().get("id").toString();
        Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        String nickname = (String) properties.get("nickname");
        String profileImageUrl = (String) properties.get("profile_image");

        Optional<Member> existingMember = memberRepository.findByKakaoId(kakaoId);
        Member member;
        if (existingMember.isEmpty()) {
            member = Member.builder()
                    .kakaoId(kakaoId)
                    .nickname(nickname)
                    .role(Role.ROLE_FREEUSER)  // 기본 역할 설정
                    .build();
            memberRepository.save(member);
        }

        return new CustomOAuth2User(kakaoId, profileImageUrl);
    }
}
