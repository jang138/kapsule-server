package net.kosa.kapsuleserver.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.kosa.kapsuleserver.base.util.LoginUtil;
import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.service.CapsuleService;
import net.kosa.kapsuleserver.service.MemberService;

import lombok.RequiredArgsConstructor;

/**
 * @author dayoung
 * CapsuleController는 타임캡슐과 관련된 요청들을 처리합니다.
 */
@Controller
@RequestMapping("/capsule")
@RequiredArgsConstructor
public class CapsuleController {

	private final MemberService memberService;
	private final CapsuleService capsuleService;
	private final LoginUtil loginUtil;

	/**
     * 타임캡슐 생성
     */
	@PostMapping("/create")
	public ResponseEntity<String> saveCapsule(@RequestBody CapsuleDTO capsuleDTO) {
		try {
			if (!loginUtil.isLogin()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("로그인 상태를 확인해주세요.");
			}

			Member member = loginUtil.getMember();
			capsuleService.saveCapsule(capsuleDTO, member);

			return ResponseEntity.status(HttpStatus.CREATED)
				.body("타임캡슐이 성공적으로 저장되었습니다.");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("타임캠슐 저장 중 오류가 발생했습니다.");
		}
	}

	/**
     * 나의 타임캡슐 조회
     */
	@GetMapping("/list")
	public ResponseEntity<?> findMyCapsule(@RequestParam String kakaoId) {
		try {
			if (kakaoId == null || kakaoId.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("로그인 상태를 확인해주세요.");
			}

			Long memberId = memberService.getIdByKakaoId(kakaoId);
			List<CapsuleDTO> myCapsule = capsuleService.findMyCapsule(memberId);

			return ResponseEntity.ok(myCapsule);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("타임캠슐 조회 중 오류가 발생했습니다.");
		}
	}

	// 타임캡슐 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCapsule(@PathVariable Long capsuleId) {
		try {
			if (!loginUtil.isLogin()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("로그인 상태를 확인해주세요.");
			}

			Member member = loginUtil.getMember();
			capsuleService.deleteCapsule(capsuleId, member);

			return ResponseEntity.noContent().build();

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(e.getMessage());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("타임캡슐 삭제 중 오류가 발생했습니다.");
		}
	}
}
