package net.kosa.kapsuleserver.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.entity.Capsule;
import net.kosa.kapsuleserver.service.MemberService;
import net.kosa.kapsuleserver.service.SharedKeyService;

import lombok.RequiredArgsConstructor;

/**
 * daeyong
 */
@Controller
@RequestMapping("/key")
@RequiredArgsConstructor
public class SharedKeyController {
	private final SharedKeyService sharedKeyService;
	private final MemberService memberService;

/**
 * 멤버 아이디로 캡슐 공유 키 조회
 */
	@GetMapping("/findAll")
	public ResponseEntity<?> findSharedKeyByMemberId(
		@RequestParam String kakaoId) {

		Long memberId = memberService.getIdByKakaoId(kakaoId);
		List<CapsuleDTO> capsuleDTOList = sharedKeyService.findAllByMemberId(memberId);
		return ResponseEntity.ok(capsuleDTOList);
	}

	// /**
	//  * 캡슐 코드와 카카오 ID로 캡슐 상세 정보 조회
	//  */

	// @GetMapping("/getCapsuleInfo")
	// public ResponseEntity<?> getCapsuleInfo(
	// 	@RequestParam String capsuleCode,
	// 	@RequestParam String kakaoId) {
	//
	// 	// 멤버 ID 및 캡슐 ID 가져오기
	// 	Long memberId = memberService.getIdByKakaoId(kakaoId);
	// 	Long capsuleId = sharedKeyService.getCapsuleIdByCode(capsuleCode);
	//
	// 	// 캡슐 정보 가져오기
	// 	Capsule capsule = sharedKeyService.getCapsuleById(capsuleId);
	//
	// 	// 원하는 정보 반환 (예: 캡슐 정보와 멤버 정보)
	// 	return ResponseEntity.ok(capsule);
	// }

	// POST
	/**
	 * 캡슐 공유 키 저장
	 */
	@PostMapping("/save")
	public ResponseEntity<String> saveSharedKey(
		@RequestParam String capsuleCode,
		@RequestParam String kakaoId) {
		Long memberId = memberService.getIdByKakaoId(kakaoId);
		Long capsuleId = sharedKeyService.getCapsuleIdByCode(capsuleCode);
		sharedKeyService.saveSharedKey(memberId, capsuleId);

		return ResponseEntity.ok("SharedKey가 성공적으로 저장되었습니다.");
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSharedKey(
		@PathVariable Long id,
		@RequestAttribute String kakaoId) {



		return ResponseEntity.noContent().build();
	}

}
