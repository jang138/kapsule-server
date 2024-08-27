package net.kosa.kapsuleserver.controller;

import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TestController {

    private final MemberService memberService;

    @GetMapping("/print-kakaoid")
    public String printKakaoId(@RequestAttribute("kakaoId") String kakaoId) {
        System.out.println("Kakao ID: " + kakaoId);
        return "Kakao ID has been printed to the console!";
    }

    @GetMapping("/print-authen")
    public ResponseEntity<Member> printKakaoIdUsedAuth(Authentication authentication) {
        String kakaoId = (String) authentication.getPrincipal();
        Member member = memberService.getMemberByKakaoId(kakaoId);
        return ResponseEntity.ok(member);
    }
}
