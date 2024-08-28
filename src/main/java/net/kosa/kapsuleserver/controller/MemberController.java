package net.kosa.kapsuleserver.controller;

import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.MemberRepository;
import net.kosa.kapsuleserver.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    /*
    Vue에서
    headers: {
        Authorization: `Bearer ${token}`,
    }
    형태로 보낸 요청에서 Authentication authentication의 getPrincipal()로 kakaoId 사용 가능
     */
    @GetMapping("/user-info")
    public ResponseEntity<Member> getUserInfo(Authentication authentication) {
        String kakaoId = (String) authentication.getPrincipal();
        Member member = memberService.getMemberByKakaoId(kakaoId);
        return ResponseEntity.ok(member);
    }

    @PostMapping("/member-info")
    public ResponseEntity<Member> getMemberByKakaoId(@RequestBody Map<String, String> request) {
        String kakaoId = request.get("kakaoId");
        if (kakaoId == null) {
            return ResponseEntity.badRequest().build();
        }

        Member member = memberService.getMemberByKakaoId(kakaoId);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(member);
    }
}
