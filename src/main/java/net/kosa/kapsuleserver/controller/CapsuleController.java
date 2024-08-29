package net.kosa.kapsuleserver.controller;

import java.util.List;

import net.kosa.kapsuleserver.entity.Capsule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import net.kosa.kapsuleserver.base.util.LoginUtil;
import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.dto.CapsuleDetailDTO;
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

	// 타임캡슐 생성
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

	@GetMapping("/list")
	public ResponseEntity<?> findMyCapsule(@RequestParam String kakaoId) {
		try {
			Long member = memberService.getIdByKakaoId(kakaoId);
			List<CapsuleDTO> myCapsule = capsuleService.findMyCapsule(member);

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

	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> getCapsuleDetail(@PathVariable Long id) {
		try {
			if (!loginUtil.isLogin()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("로그인 상태를 확인해주세요.");
			}

			Member member = loginUtil.getMember(); // 현재 로그인한 사용자
			CapsuleDetailDTO capsuleDetail = capsuleService.findCapsuleById(id, member);

			return ResponseEntity.ok(capsuleDetail);

		} catch (SecurityException e) {
			// 권한이 없는 경우
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(e.getMessage());

		} catch (IllegalArgumentException e) {
			// 존재하지 않는 캡슐인 경우
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(e.getMessage());

		} catch (Exception e) {
			// 그 외 오류
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("타임캡슐 조회 중 오류가 발생했습니다.");
		}
	}
}
