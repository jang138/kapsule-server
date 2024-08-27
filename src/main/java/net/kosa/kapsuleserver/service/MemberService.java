package net.kosa.kapsuleserver.service;

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
    private final JwtUtil jwtUtil;

    public Member getMemberByKakaoId(String kakaoId) {
        return memberRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new NoSuchElementException("Member not found with kakaoId: " + kakaoId));
    }

    public Member getMemberByToken(String token) {
        String kakaoId = jwtUtil.getKakaoIdFromJwt(token);
        if (kakaoId == null) {
            throw new IllegalArgumentException("Invalid JWT Token");
        }

        Optional<Member> memberOpt = memberRepository.findByKakaoId(kakaoId);
        return memberOpt.orElseThrow(() -> new IllegalArgumentException("Member not found for kakaoId: " + kakaoId));
    }
}
