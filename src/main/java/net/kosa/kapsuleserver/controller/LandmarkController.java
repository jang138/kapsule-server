package net.kosa.kapsuleserver.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.service.LandmarkService;
import net.kosa.kapsuleserver.base.util.LoginUtil;


// 랜드마크와 관련된 요청 처리
@RestController
@RequestMapping("/api/landmark")
@RequiredArgsConstructor
public class LandmarkController {

    private static final Logger log = LoggerFactory.getLogger(LandmarkController.class);
    private final LandmarkService landmarkService;
    private final LoginUtil loginUtil;

    // 모든 랜드마크 조회
    @GetMapping
    public ResponseEntity<List<CapsuleDTO>> getAllLandmarks() {
        try {
            List<CapsuleDTO> landmarks = landmarkService.findAllLandmarks();
            return ResponseEntity.ok(landmarks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // 특정 랜드마크 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getLandmarkById(@PathVariable Long id) {
        try {
            CapsuleDTO landmark = landmarkService.findLandmarkById(id);
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
    public ResponseEntity<String> createLandmark(@RequestBody CapsuleDTO capsuleDTO) {
        log.info("Received request to create landmark: {}", capsuleDTO);
        try {
        /*
        if (!loginUtil.isLogin()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 상태를 확인해주세요.");
        }

        Member member = loginUtil.getMember();
        */

            // 임시 멤버 데이터 사용, 나중에 로그인 로직 구현 시 수정해야 함
            Member member = Member.builder()
                    .id(capsuleDTO.getMember().getId())
                    .nickname(capsuleDTO.getMember().getNickname())
                    .kakaoId(capsuleDTO.getMember().getKakaoId())
                    .role(capsuleDTO.getMember().getRole())
                    .build();

            landmarkService.saveLandmark(capsuleDTO, member);
            log.info("Landmark created successfully: {}", capsuleDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("랜드마크가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            log.error("Error occurred while creating landmark", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("랜드마크 저장 중 오류가 발생했습니다.");
        }
    }


    // 랜드마크 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLandmark(@PathVariable Long id) {
        try {
            landmarkService.deleteLandmark(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("랜드마크 삭제 중 오류가 발생했습니다.");
        }
    }
}
