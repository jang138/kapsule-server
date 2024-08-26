package net.kosa.kapsuleserver;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.kosa.kapsuleserver.base.entity.Role;
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

	@BeforeEach
	// @Transactional
	void setUp() {
		member = memberRepository.save(Member.builder()
			.kakaoId("Kakao_TMP_05")
			.nickname("테스트 5")
			.role(Role.ROLE_FREEUSER)
			.build());
	}

	@Test
	// @Transactional
	@DisplayName("타임캡슐 생성하기")
	void saveCapsule() {
		List<Capsule> capsuleList_1 = capsuleRepository.findAll();

		String code = capsuleService.createRandomCode(8);

		capsuleRepository.save(Capsule.builder()
			.title("캡슐 6")
			.content("타임캡슐 입니다.")
			.member(member)
			.capsuleCode(code)
			.address("서울특별시 종로구 대학로12길 38 (동숭동)")
			.latitude(37.582412965088F)
			.longitude(127.00378236901F)
			.unlockDate(LocalDate.now().plusDays(3))
			.build());

		List<Capsule> capsuleList_2 = capsuleRepository.findAll();

		assertThat(capsuleList_2.size() - capsuleList_1.size()).isEqualTo(1);
	}
}
