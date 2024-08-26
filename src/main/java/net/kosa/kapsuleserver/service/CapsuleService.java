package net.kosa.kapsuleserver.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import net.kosa.kapsuleserver.base.entity.Role;
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
			.capsuleType(member.getRole() == Role.ROLE_ADMIN ? 2 : 1)
			.build();

		capsuleRepository.save(capsule);
	}

	// 나의 타임 캡슐 조회
	@Transactional
	public List<CapsuleDTO> findMyCapsule(Long memberId) {
		List<Capsule> capsuleList = capsuleRepository.findAllByMemberId(memberId);

		return convertToDTO(capsuleList);
	}

	@Transactional
	public void deleteCapsule(Long capsuleId, Member member) {
		Capsule capsule = capsuleRepository.findById(capsuleId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 타임캡슐입니다."));

		if (!capsule.getMember().getKakaoId().equals(member.getKakaoId())) {
			throw new SecurityException("타임캡슐을 삭제할 권한이 없습니다.");
		}

		capsuleRepository.deleteById(capsuleId);
	}


	// 캡슐 리스트를 DTO로 변환
	public List<CapsuleDTO> convertToDTO(List<Capsule> capsuleList) {
		return capsuleList.stream()
			.map(capsule -> CapsuleDTO.builder()
				.id(capsule.getId())
				.member(capsule.getMember())
				.title(capsule.getTitle())
				.content(capsule.getContent())
				.address(capsule.getAddress())
				.longitude(capsule.getLongitude())
				.latitude(capsule.getLatitude())
				.unlockDate(capsule.getUnlockDate())
				.capsuleCode(capsule.getCapsuleCode())
				.build())
			.collect(Collectors.toList());
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
