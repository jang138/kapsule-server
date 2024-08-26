package net.kosa.kapsuleserver.service;

import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member getMemberByKakaoId(String kakaoId) {
        return memberRepository.findByKakaoId(kakaoId).get();
    }
}
