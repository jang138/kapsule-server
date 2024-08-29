package net.kosa.kapsuleserver.service;

import java.util.List;

import net.kosa.kapsuleserver.dto.MemberDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.entity.Capsule;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.entity.SharedKey;
import net.kosa.kapsuleserver.entity.SharedKeyId;
import net.kosa.kapsuleserver.repository.CapsuleRepository;
import net.kosa.kapsuleserver.repository.MemberRepository;
import net.kosa.kapsuleserver.repository.SharedKeyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SharedKeyService {
	private final SharedKeyRepository sharedKeyRepository;
	private final MemberRepository memberRepository;
	private final CapsuleRepository capsuleRepository;

	/**
	 * 캡슐코드로 캡슐 정보 가져오기
	 */
	public Capsule getCapsuleByCode(String capsuleCode) {
		return capsuleRepository.findByCapsuleCode(capsuleCode)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캡슐코드입니다."));
	}

	/**
	 * 캡슐 아이디로 캡슐 정보 가져오기
	 */
	public Capsule getCapsuleById(Long capsuleId) {
		return capsuleRepository.findById(capsuleId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캡슐입니다."));
	}

	/**
	 * 멤버 아이디로 다 가져오기
	 */
	public List<CapsuleDTO> findAllByMemberId(Long memberId) {
		List<SharedKey> sharedKeyList = sharedKeyRepository.findAllByMemberId(memberId);
		return sharedKeyList.stream()
			.map(sharedKey -> CapsuleDTO.builder()
				.id(sharedKey.getCapsule().getId())
				.member(toMemberDTO(sharedKey.getMember()))
				.title(sharedKey.getCapsule().getTitle())
				.content(sharedKey.getCapsule().getContent())
				.address(sharedKey.getCapsule().getAddress())
				.longitude(sharedKey.getCapsule().getLongitude())
				.latitude(sharedKey.getCapsule().getLatitude())
				.unlockDate(sharedKey.getCapsule().getUnlockDate())
				.capsuleType(sharedKey.getCapsule().getCapsuleType())
//				.capsuleCode(sharedKey.getCapsule().getCapsuleCode())
//				.kakaoId(sharedKey.getMember().getKakaoId())
				.build())
			.toList();
	}

	private MemberDTO toMemberDTO(Member member) {
		return MemberDTO.builder()
			.id(member.getId())
			.nickname(member.getNickname())
			.kakaoId(member.getKakaoId())
			.role(member.getRole().name())
			.build();
	}

	/**
	 * 캡슐코드로 캡슐 정보에서 캡슐 ID를 가져오기
	 */
	public Long getCapsuleIdByCode(String capsuleCode) {
		Capsule capsule = getCapsuleByCode(capsuleCode);
		return capsule.getId();
	}

	/**
	 * 캡슐 ID와 멤버 ID로 SharedKey 저장하기
	 */
	@Transactional
	public void saveSharedKey(Long memberId, Long capsuleId) {
		Capsule capsule = capsuleRepository.findById(capsuleId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캡슐입니다."));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));
		SharedKeyId sharedKeyId = new SharedKeyId(member.getId(), capsule.getId());
		SharedKey sharedKey = SharedKey.builder()
			.id(sharedKeyId)
			.capsule(capsule)
			.member(member)
			.opened(false)
			.build();
		sharedKeyRepository.save(sharedKey);
	}

}
