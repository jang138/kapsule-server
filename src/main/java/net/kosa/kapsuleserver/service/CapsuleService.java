package net.kosa.kapsuleserver.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
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
				.content("{\"daterange\": \"" + capsuleDTO.getContent().getDaterange() + "\","
						+ " \"subtitle\": \"" + capsuleDTO.getContent().getSubtitle() + "\","
						+ " \"text\": \"" + capsuleDTO.getContent().getText() + "\"}")
				.address(capsuleDTO.getAddress())
				.longitude(capsuleDTO.getCoordinates().getLng())
				.latitude(capsuleDTO.getCoordinates().getLat())
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
		return capsuleList.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
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



	// 단일 캡슐을 DTO로 변환
	private CapsuleDTO convertToDTO(Capsule capsule) {
		ObjectMapper objectMapper = new ObjectMapper();  // Jackson ObjectMapper를 사용해 JSON 문자열을 객체로 변환
		CapsuleDTO.Content content = null;
		try {
			content = objectMapper.readValue(capsule.getContent(), CapsuleDTO.Content.class);  // JSON 문자열을 CapsuleDTO.Content 객체로 변환
		} catch (Exception e) {
			// 예외 처리: JSON 파싱 실패 시 로깅하거나 적절한 조치를 취할 수 있습니다.
			e.printStackTrace();
		}

		return CapsuleDTO.builder()
				.id(capsule.getId())
				.member(capsule.getMember())
				.title(capsule.getTitle())
				.content(content)  // 변환된 content 객체를 설정
				.address(capsule.getAddress())
				.capsuleCode(capsule.getCapsuleCode())
				.unlockDate(capsule.getUnlockDate())
				.coordinates(CapsuleDTO.Coordinates.builder()
						.lat(capsule.getLatitude())
						.lng(capsule.getLongitude())
						.build())
				.build();
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
