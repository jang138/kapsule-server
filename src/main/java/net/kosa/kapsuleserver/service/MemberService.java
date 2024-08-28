package net.kosa.kapsuleserver.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.base.util.JwtUtil;

import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getMemberByKakaoId(String kakaoId) {
        return memberRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new NoSuchElementException("Member not found with kakaoId: " + kakaoId));
    }

    public Long getIdByKakaoId(String kakaoId) {
        Member member = memberRepository.findByKakaoId(kakaoId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        return member.getId();
    }
}
