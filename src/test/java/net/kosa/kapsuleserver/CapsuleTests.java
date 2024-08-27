package net.kosa.kapsuleserver;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.kosa.kapsuleserver.base.entity.Role;
import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.entity.Capsule;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.CapsuleRepository;
import net.kosa.kapsuleserver.repository.MemberRepository;
import net.kosa.kapsuleserver.service.CapsuleService;

import jakarta.transaction.Transactional;

@SpringBootTest
public class CapsuleTests {

	@Autowired private MemberRepository memberRepository;
	@Autowired private CapsuleService capsuleService;
	@Autowired private CapsuleRepository capsuleRepository;

	Member member;
	String kakoIdByMember = "Kakao_TMP_06";

	@BeforeEach
	@Transactional
	void setUp() {
		member = memberRepository.save(Member.builder()
			.kakaoId(kakoIdByMember)
			.nickname("관리자")
			.role(Role.ROLE_ADMIN)
			.build());
	}

	@Test
	@Transactional
	@DisplayName("타임캡슐 생성하기")
	void saveCapsule() {
		List<Capsule> capsuleList_1 = capsuleRepository.findAll();

		String code = capsuleService.createRandomCode(8);
		Member nowMember = memberRepository.findByKakaoId(kakoIdByMember)
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
		Optional<Member> nowMember = memberRepository.findByKakaoId(kakoIdByMember);
		if(nowMember.isPresent()) {
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
		Member nowMember = memberRepository.findByKakaoId(kakoIdByMember)
				.orElseThrow(IllegalArgumentException::new);

		List<CapsuleDTO> myCapsule_before = capsuleService.findMyCapsule(nowMember.getId());
		capsuleService.deleteCapsule(myCapsule_before.get(0).getId(), nowMember);
		List<CapsuleDTO> myCapsule_after = capsuleService.findMyCapsule(nowMember.getId());

		assertThat(myCapsule_before.size() - myCapsule_after.size()).isEqualTo(1);
	}
}
