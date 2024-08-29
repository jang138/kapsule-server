package net.kosa.kapsuleserver.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.kosa.kapsuleserver.dto.ImageDTO;
import net.kosa.kapsuleserver.entity.Image;
import net.kosa.kapsuleserver.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.base.entity.Role;
import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.dto.CapsuleDetailDTO;
import net.kosa.kapsuleserver.dto.MemberDTO;
import net.kosa.kapsuleserver.entity.Capsule;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.CapsuleRepository;
import net.kosa.kapsuleserver.repository.MemberRepository;
import net.kosa.kapsuleserver.repository.SharedKeyRepository;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author dayoung
 *         CapsuleService는 타임캡슐과 관련된 로직들을 구현합니다.
 */
@Service
@RequiredArgsConstructor
public class CapsuleService {

	private static final String CHARACTER_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final SecureRandom random = new SecureRandom();
	private final CapsuleRepository capsuleRepository;
	private final MemberRepository memberRepository;
	private final SharedKeyRepository sharedKeyRepository;
	private final ImageService imageService;

	/**
	 * 타임캡슐 저장
	 */
	@Transactional
	public void saveCapsule(CapsuleDTO capsuleDTO, Member member) {
		// Capsule 엔티티 생성
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

		// 데이터베이스에 저장
		Capsule savedCapsule = capsuleRepository.save(capsule);

		// 이미지 저장
		if (capsuleDTO.getImages() != null && !capsuleDTO.getImages().isEmpty()) {
			imageService.save(savedCapsule, capsuleDTO.getImages());
		}
	}

	/**
	 * 나의 타임캡슐 조회
	 */
	@Transactional(readOnly = true)
	public List<CapsuleDTO> findMyCapsule(Long memberId) {
		// 멤버 ID로 캡슐 조회
		List<Capsule> capsuleList = capsuleRepository.findAllByMemberId(memberId);

		// DTO로 변환하여 반환
		return convertToDTO(capsuleList);
	}

	/**
	 * 타임캡슐 삭제
	 */
	@Transactional
	public void deleteCapsule(Long capsuleId, Member member) {
		// ID로 타임캡슐 조회
		Capsule capsule = capsuleRepository.findById(capsuleId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 타임캡슐입니다."));

		// 멤버가 해당 캡슐의 소유자인지 확인
		if (!capsule.getMember().getKakaoId().equals(member.getKakaoId())) {
			throw new SecurityException("타임캡슐을 삭제할 권한이 없습니다.");
		}

		// 캡슐 삭제
		capsuleRepository.deleteById(capsuleId);
	}

	/**
	 * 
	 * Capsule 리스트를 DTO 리스트로 변환
	 */
	private List<CapsuleDTO> convertToDTO(List<Capsule> capsuleList) {
		return capsuleList.stream()
				.map(this::convertCapsuleToDTO)
				.collect(Collectors.toList());
	}

	private CapsuleDTO convertCapsuleToDTO(Capsule capsule) {
		return CapsuleDTO.builder()
				.id(capsule.getId())
				.member(convertMemberToDTO(capsule.getMember()))
				.title(capsule.getTitle())
				.content(capsule.getContent())
				.address(capsule.getAddress())
				.longitude(capsule.getLongitude())
				.latitude(capsule.getLatitude())
				.unlockDate(capsule.getUnlockDate())
				.capsuleCode(capsule.getCapsuleCode())
				.build();
	}

	private MemberDTO convertMemberToDTO(Member member) {
		return MemberDTO.builder()
				.id(member.getId())
				.nickname(member.getNickname())
				.kakaoId(member.getKakaoId())
				.role(String.valueOf(member.getRole()))
				.build();
	}

	/**
	 * CHARACTER_SET으로 구성된 난수 생성
	 */
	public String createRandomCode(int length) {
		String code;

		do {
			StringBuilder codeBuilder = new StringBuilder(length);

			for (int i = 0; i < length; i++) {
				int idx = random.nextInt(CHARACTER_SET.length());
				codeBuilder.append(CHARACTER_SET.charAt(idx));
			}

			code = codeBuilder.toString();
		} while (capsuleRepository.existsByCapsuleCode(code));

		return code;
	}

	/* 타임캡슐 디테일 페이지 : 캡슐 ID로 캡슐 조회 */
	@Transactional
	public CapsuleDetailDTO findCapsuleById(Long capsuleId, Member member) {
		Capsule capsule = capsuleRepository.findById(capsuleId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 타임캡슐입니다."));

		// 타임캡슐 소유자 확인 또는 공유 상태 확인
		if (!capsule.getMember().getId().equals(member.getId()) && !isCapsuleSharedWithMember(capsule, member)) {
			throw new SecurityException("타임캡슐을 조회할 권한이 없습니다.");
		}

		List<Image> imageList = imageService.getImage(capsuleId);
		List<String> imagePathList = imageList.stream()
				.map(Image::getPath)
				.toList();

		return CapsuleDetailDTO.builder()
				.id(capsule.getId())
				.title(capsule.getTitle())
				.content(capsule.getContent())
				.address(capsule.getAddress())
				.longitude(capsule.getLongitude())
				.latitude(capsule.getLatitude())
				.unlockDate(capsule.getUnlockDate())
				.capsuleType(capsule.getCapsuleType())
				.images(imagePathList)
				.build();
	}

	private boolean isCapsuleSharedWithMember(Capsule capsule, Member member) {
		return sharedKeyRepository.existsByCapsuleAndMember(capsule, member);
	}


}
