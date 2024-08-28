package net.kosa.kapsuleserver.controller;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.kosa.kapsuleserver.base.entity.Role;
import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.entity.Capsule;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.entity.SharedKey;
import net.kosa.kapsuleserver.entity.SharedKeyId;
import net.kosa.kapsuleserver.repository.CapsuleRepository;
import net.kosa.kapsuleserver.repository.MemberRepository;
import net.kosa.kapsuleserver.repository.SharedKeyRepository;
import net.kosa.kapsuleserver.service.CapsuleService;
import net.kosa.kapsuleserver.service.MemberService;
import net.kosa.kapsuleserver.service.SharedKeyService;

import jakarta.transaction.Transactional;

@SpringBootTest
public class CapsuleTests {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private CapsuleService capsuleService;
	@Autowired
	private CapsuleRepository capsuleRepository;
	@Autowired
	private SharedKeyRepository sharedKeyRepository;

	Member member;
	String kakaoIdByMember = "Kakao_TMP_05";
	@Autowired
	private MemberService memberService;
	@Autowired
	private SharedKeyService sharedKeyService;

	void setUp() {
		member = memberRepository.save(Member.builder()
			.kakaoId(kakaoIdByMember)
			.nickname("테스트 5")
			.role(Role.ROLE_FREEUSER)
			.build());
	}

	@Test
	@DisplayName("타임캡슐 생성하기")
	void saveCapsule() {
		List<Capsule> capsuleList_1 = capsuleRepository.findAll();

		String code = capsuleService.createRandomCode(8);
		Member nowMember = memberRepository.findByKakaoId(kakaoIdByMember)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

		capsuleRepository.save(Capsule.builder()
			.title("캡슐 6")
			.content("타임캡슐 입니다.")
			.member(nowMember)
			.capsuleCode(code)
			.address("서울특별시 종로구 대학로12길 38 (동숭동)")
			.latitude(37.582412965088F)
			.longitude(127.00378236901F)
			.unlockDate(LocalDate.now().plusDays(3))
			.capsuleType(Role.ROLE_ADMIN == nowMember.getRole() ? 2 : 1)
			.build());

		List<Capsule> capsuleList_2 = capsuleRepository.findAll();

		assertThat(capsuleList_2.size() - capsuleList_1.size()).isEqualTo(1);
	}

	@Test
	@Transactional
	@DisplayName("나의 타임캡슐 정보 가져오기")
	void findMyCapsule() {
		Optional<Member> nowMember = memberRepository.findByKakaoId(kakaoIdByMember);
		if (nowMember.isPresent()) {
			List<CapsuleDTO> myCapsule = capsuleService.findMyCapsule(nowMember.get().getId());

			// Then
			assertThat(myCapsule).isNotEmpty();  // myCapsule이 비어있지 않은지 검증
			assertThat(myCapsule).hasSizeGreaterThan(0);  // myCapsule 리스트의 크기가 0보다 큰지 검증

			assertThat(myCapsule.get(0).getTitle()).isEqualTo("캡슐 6");
		}
	}

	@Test
	@Transactional
	@DisplayName("타임캡슐 삭제하기")
	void deleteCapsule() {
		Member nowMember = memberRepository.findByKakaoId(kakaoIdByMember)
			.orElseThrow(IllegalArgumentException::new);

		List<CapsuleDTO> myCapsule_before = capsuleService.findMyCapsule(nowMember.getId());
		capsuleService.deleteCapsule(myCapsule_before.get(0).getId(), nowMember);
		List<CapsuleDTO> myCapsule_after = capsuleService.findMyCapsule(nowMember.getId());

		assertThat(myCapsule_before.size() - myCapsule_after.size()).isEqualTo(1);
	}

	@Test
	// @Transactional
	@DisplayName("캡슐코드로 SharedKey 저장하기")
	void saveSharedKey() {

		String capsuleCode = "93SGC6ZZ";  // 위에서 저장한 캡슐 코드와 동일

		Long capsuleId = sharedKeyService.getCapsuleIdByCode(capsuleCode);
		Long memberId = memberService.getIdByKakaoId(kakaoIdByMember);

		// 3. SharedKey를 저장합니다.
		sharedKeyService.saveSharedKey(memberId, capsuleId);

		// 4. 저장된 SharedKey를 검증합니다.
		SharedKeyId sharedKeyId = new SharedKeyId(memberId, capsuleId);
		SharedKey savedSharedKey = sharedKeyRepository.findAllByMemberId(memberId);

		// 5. 검증
		assertThat(savedSharedKey).isNotNull();
		assertThat(sharedKeyRepository.findAll().getFirst().getCapsule().getId()).isEqualTo(capsuleId);
	}
}
