package net.kosa.kapsuleserver;

import jakarta.transaction.Transactional;
import net.kosa.kapsuleserver.base.entity.Role;
import net.kosa.kapsuleserver.dto.LandmarkDTO;
import net.kosa.kapsuleserver.entity.Capsule;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.CapsuleRepository;
import net.kosa.kapsuleserver.repository.MemberRepository;
import net.kosa.kapsuleserver.service.LandmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LandmarkTests {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired private LandmarkService landmarkService;
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
    @DisplayName("랜드마크 생성 테스트")
    void createLandmarkTest() {
        Member nowMember = memberRepository.findByKakaoId(kakoIdByMember)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        LandmarkDTO landmarkDTO = LandmarkDTO.builder()
                .capsuleType(2)
                .title("애버랜드")
                .address("경기도 용인시 에버랜드로 199")
                .content(null) // 나중에 수정 예정
                .coordinates(LandmarkDTO.Coordinates.builder()
                        .lat(37.29310247591812F)
                        .lng(127.20219323036818F)
                        .build())
                .unlockDate(LocalDate.now().plusDays(3))
                .build();

        // 랜드마크 저장
        landmarkService.saveLandmark(landmarkDTO, nowMember);

        List<Capsule> landmarks = capsuleRepository.findByCapsuleType(2);
        assertThat(landmarks).isNotEmpty();
        assertThat(landmarks.get(0).getTitle()).isEqualTo("애버랜드");
    }

}