package net.kosa.kapsuleserver.service;

import java.util.Optional;

import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member getUserByKakaoId(String kakaoId) {
        return memberRepository.findByKakaoId(kakaoId).get();
    }

    public Long getIdByKakaoId(String kakaoId) {
        Member member = memberRepository.findByKakaoId(kakaoId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        return member.getId();
    }
}
