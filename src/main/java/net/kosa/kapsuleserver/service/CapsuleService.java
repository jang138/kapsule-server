package net.kosa.kapsuleserver.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.entity.Capsule;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.CapsuleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CapsuleService {

	private final CapsuleRepository capsuleRepository;

	private static final String CHARACTER_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final SecureRandom random = new SecureRandom();

	public void saveCapsule(CapsuleDTO capsuleDTO, Member member) {
		Capsule capsule = Capsule.builder()
			.memberId(member)
			.title(capsuleDTO.getTitle())
			.content(capsuleDTO.getContent())
			.address(capsuleDTO.getAddress())
			.longitude(capsuleDTO.getLongitude())
			.latitude(capsuleDTO.getLatitude())
			.unlockDate(capsuleDTO.getUnlockDate())
			.capsuleCode(createRandomCode(8))
			.build();

		capsuleRepository.save(capsule);
	}

	// 8자리 난수를 생성하는 메소드
	public String createRandomCode(int length) {
		String code;

		do {
			StringBuilder codeBuilder = new StringBuilder(length);
			for(int i = 0; i < length; i++) {
				int idx = random.nextInt(CHARACTER_SET.length());
				codeBuilder.append(CHARACTER_SET.charAt(idx));
			}

			code = codeBuilder.toString();
		} while (capsuleRepository.existsByCapsuleCode(code));

		return code;
	}
}
