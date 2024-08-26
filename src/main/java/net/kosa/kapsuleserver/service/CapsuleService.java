package net.kosa.kapsuleserver.service;

import java.security.SecureRandom;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.entity.Capsule;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.CapsuleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * @author dayoung
 * CapsuleService는 타임캡슐과 관련된 로직들을 구현합니다.
 */
@Service
@RequiredArgsConstructor
public class CapsuleService {

	private final CapsuleRepository capsuleRepository;

	private static final String CHARACTER_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final SecureRandom random = new SecureRandom();

	@Transactional
	public void saveCapsule(CapsuleDTO capsuleDTO, Member member) {
		Capsule capsule = Capsule.builder()
			.member(member)
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

	// CHARACTER_SET으로 구성된 난수를 생성하는 메소드
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
