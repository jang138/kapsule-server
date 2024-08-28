package net.kosa.kapsuleserver.controller;

import java.util.List;

import net.kosa.kapsuleserver.base.entity.Role;
import net.kosa.kapsuleserver.dto.CapsuleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.dto.LandmarkDTO;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.service.LandmarkService;
import net.kosa.kapsuleserver.base.util.LoginUtil;


// 랜드마크와 관련된 요청 처리
@RestController
@RequestMapping("/landmark")
@RequiredArgsConstructor
public class LandmarkController {

    private static final Logger log = LoggerFactory.getLogger(LandmarkController.class);
    private final LandmarkService landmarkService;
    private final LoginUtil loginUtil;

    // 모든 랜드마크 조회
    @GetMapping
    public ResponseEntity<List<LandmarkDTO>> getAllLandmarks() {
        try {
            List<LandmarkDTO> landmarks = landmarkService.findAllLandmarks();
            return ResponseEntity.ok(landmarks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // 특정 랜드마크 조회 (id를 Long으로 사용)
    @GetMapping("/{id}")
    public ResponseEntity<?> getLandmarkById(@PathVariable Long id) {
        try {
            LandmarkDTO landmark = landmarkService.findLandmarkById(id);
            return ResponseEntity.ok(landmark);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("랜드마크 조회 중 오류가 발생했습니다.");
        }
    }

    // 새 랜드마크 생성
    @PostMapping("/create")
    public ResponseEntity<String> saveCapsule(@RequestBody LandmarkDTO landmarkDTO) {
        try{
/*            if(!loginUtil.isLogin()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인 상태를 확인해주세요.");
            }

            Member member = loginUtil.getMember();*/

            // 임시 멤버 정보를 사용하여 저장
            Member member = Member.builder()
                    .id(65L)  // 기본 멤버 ID
                    .nickname("관리자")  // 기본 닉네임
                    .kakaoId("Kakao_TMP_06")  // 임시 카카오 ID
                    .role(Role.ROLE_ADMIN)  // 기본 역할
                    .build();

            landmarkService.saveLandmark(landmarkDTO, member);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("타임캡슐이 성공적으로 저장되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("타임캠슐 저장 중 오류가 발생했습니다.");
        }
    }


    // 랜드마크 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCapsule(@PathVariable("id") Long capsuleId) {
        try {
//            if (!loginUtil.isLogin()) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("로그인 상태를 확인해주세요.");
//            }
//
//            Member member = loginUtil.getMember();

            // 임시 멤버 정보를 사용하여 저장
            Member member = Member.builder()
                    .id(65L)  // 기본 멤버 ID
                    .nickname("관리자")  // 기본 닉네임
                    .kakaoId("Kakao_TMP_06")  // 임시 카카오 ID
                    .role(Role.ROLE_ADMIN)  // 기본 역할
                    .build();

            landmarkService.deleteLandmark(capsuleId, member);

            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("타임캡슐 삭제 중 오류가 발생했습니다.");
        }
    }
}
