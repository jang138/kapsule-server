package net.kosa.kapsuleserver.controller;

import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.kosa.kapsuleserver.base.util.LoginUtil;
import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.service.CapsuleService;

import lombok.RequiredArgsConstructor;

/**
 * @author dayoung
 * CapsuleController는 타임캡슐과 관련된 요청들을 처리합니다.
 */
@Controller
@RequestMapping("/capsule")
@RequiredArgsConstructor
public class CapsuleController {

	private final CapsuleService capsuleService;
	private final LoginUtil loginUtil;

	@PostMapping("/create")
	public ResponseEntity<?> saveCapsule(@RequestBody CapsuleDTO capsuleDTO) {
		try{
			if(loginUtil.isLogin()) {
				Member member = loginUtil.getMember();
				capsuleService.saveCapsule(capsuleDTO, member);

				return ResponseEntity.status(HttpStatus.CREATED)
						.body("타임캡슐이 성공적으로 저장되었습니다.");
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("로그인 상태를 확인해주세요.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("타임캠슐 저장 중 오류가 발생했습니다.");
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findMyCapsule(@PathVariable Long id) {
		Member member = loginUtil.getMember();

		if (Objects.equals(member.getId(), id)) {
			List<CapsuleDTO> myCapsuleList = capsuleService.findMyCapsule(id);
			return ResponseEntity.ok(myCapsuleList);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유저 정보가 올바르지 않습니다.");
		}
	}
}
