//package net.kosa.kapsuleserver;
//
//import jakarta.transaction.Transactional;
//import net.kosa.kapsuleserver.base.entity.Role;
//import net.kosa.kapsuleserver.dto.CapsuleDTO;
//import net.kosa.kapsuleserver.entity.Capsule;
//import net.kosa.kapsuleserver.entity.Member;
//import net.kosa.kapsuleserver.repository.CapsuleRepository;
//import net.kosa.kapsuleserver.repository.MemberRepository;
//import net.kosa.kapsuleserver.service.CapsuleService;
//import net.kosa.kapsuleserver.service.LandmarkService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class LandmarkTests {
//
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired private LandmarkService landmarkService;
//    @Autowired private CapsuleRepository capsuleRepository;
//
//    Member member;
//    String kakoIdByMember = "Kakao_TMP_06";
//
//    @BeforeEach
//    @Transactional
//    void setUp() {
//        member = memberRepository.save(Member.builder()
//                .kakaoId(kakoIdByMember)
//                .nickname("관리자")
//                .role(Role.ROLE_ADMIN)
//                .build());
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("랜드마크 생성 테스트")
//    void createLandmarkTest() {
//        Member nowMember = memberRepository.findByKakaoId(kakoIdByMember)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
//
//        CapsuleDTO capsuleDTO = CapsuleDTO.builder()
//                .capsuleType(2)
//                .title("애버랜드")
//                .content("<p style=\"text-align: center;\"><strong>경기도 용인시 처인구 포곡읍에 위치한 테마파크. 삼성이 설립, 운영하는 대한민국 최대 규모의 테마파크이다.</strong></p> 1976년 3월 26일 용인자연농원(龍仁自然農園)으로 처음 개장한 이래 대한민국의 테마파크 중 가장 큰 규모를 자랑한다. 주요 시설물로는 테마파크 에버랜드, 워터파크 캐리비안 베이, 숙박시설 홈브리지 등이 있다. 1996년 1월에 에버랜드로 BI를 변경했고, 2006년 1월에 에버랜드 리조트로 BI를 다시 변경하였다. 삼성 계열사인 삼성에버랜드㈜가 운영하다가 2014년 제일모직㈜으로, 2015년 삼성물산㈜으로 운영사가 변경되었습니다. 2013년에는 용인 경전철 에버라인이 개통되어 기흥역에서 에버랜드까지 연결되었고, 대중교통으로는 분당선과 여러 버스를 통해 접근할 수 있습니다.")
//                .address("경기도 용인시 에버랜드로 199")
//                .coordinates(CapsuleDTO.Coordinates.builder()
//                        .lat(37.29310247591812F)
//                        .lng(127.20219323036818F)
//                        .build())
//                .unlockDate(LocalDate.now().plusDays(3))
//                .build();
//
//        // 랜드마크 저장
//        landmarkService.saveLandmark(capsuleDTO, nowMember);
//
//        List<Capsule> landmarks = capsuleRepository.findByCapsuleType(2);
//        assertThat(landmarks).isNotEmpty();
//        assertThat(landmarks.get(0).getTitle()).isEqualTo("애버랜드");
//    }
//
//}
