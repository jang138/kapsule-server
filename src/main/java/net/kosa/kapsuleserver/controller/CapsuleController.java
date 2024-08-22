package net.kosa.kapsuleserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@PostMapping("/create")
	public void saveCapsule(@RequestBody CapsuleDTO capsuleDTO) {
		// TODO : 유저 존재하는지 확인 (로그인 유무 확인)
		// 유저가 없는 경우 : RuntimeException
		Member member = capsuleDTO.getMember();

		capsuleService.saveCapsule(capsuleDTO, member);
	}
}
