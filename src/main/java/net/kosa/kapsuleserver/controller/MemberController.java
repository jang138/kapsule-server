package net.kosa.kapsuleserver.controller;

import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/user/profile")
    public String getUserProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof String) {
            String kakaoId = (String) principal;
            Optional<Member> member = memberRepository.findByKakaoId(kakaoId);
            if (member.isPresent()) {
                model.addAttribute("user", member.get());
            } else {
                return "redirect:/";
            }
        } else if (principal instanceof Member) {
            Member member = (Member) principal;
            model.addAttribute("user", member);
        } else {
            return "redirect:/";
        }
        return "member-info"; // user/profile.html 페이지로 이동
    }

    @GetMapping("/logout")
    public String logout() {
        // Spring Security에서 제공하는 로그아웃 URL로 리다이렉트
        return "redirect:/perform_logout";
    }
}
