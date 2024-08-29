package net.kosa.kapsuleserver.controller;

import java.time.LocalDate;
import java.util.List;

import net.kosa.kapsuleserver.base.entity.Role;
import net.kosa.kapsuleserver.dto.CapsuleDTO;
import net.kosa.kapsuleserver.dto.MemberDTO;
import net.kosa.kapsuleserver.service.MemberService;
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
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;


// 랜드마크와 관련된 요청 처리
@RestController
@RequestMapping("/landmark")
@RequiredArgsConstructor
public class LandmarkController {

    private static final Logger log = LoggerFactory.getLogger(LandmarkController.class);
    private final LandmarkService landmarkService;
    private final LoginUtil loginUtil;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

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

    /*
    // 새 랜드마크 생성
    @PostMapping("/create")
    public ResponseEntity<String> saveCapsule(@RequestBody LandmarkDTO landmarkDTO) {
        try {
            if(!loginUtil.isLogin()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인 상태를 확인해주세요.");
            }

            Member member = loginUtil.getMember();

            landmarkService.saveLandmark(landmarkDTO, member);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("타임캡슐이 성공적으로 저장되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("타임캠슐 저장 중 오류가 발생했습니다.");
        }
    }*/
    @PostMapping("/create")
    public ResponseEntity<String> saveLandmark(
            @RequestPart("title") String title,
            @RequestPart("content") String contentJson,
            @RequestPart("unlockDate") String unlockDate,
            @RequestPart("address") String address,
            @RequestPart("latitude") String latitude,
            @RequestPart("longitude") String longitude,
            @RequestPart("kakaoId") String kakaoId,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            // 로그인 여부 확인
            if (kakaoId == null || kakaoId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인 상태를 확인해주세요.");
            }

            // Member 가져오기
            Member member = memberService.getMemberByKakaoId(kakaoId);
            if (member == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("유효하지 않은 사용자입니다.");
            }

            // JSON 문자열 content를 LandmarkDTO.Content 객체로 변환
            LandmarkDTO.Content content = objectMapper.readValue(contentJson, LandmarkDTO.Content.class);

            // MemberDTO로 변환
            MemberDTO memberDTO = MemberDTO.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .kakaoId(member.getKakaoId())
                    .role(String.valueOf(member.getRole()))
                    .build();

            // LandmarkDTO 생성
            LandmarkDTO landmarkDTO = LandmarkDTO.builder()
                    .title(title)
                    .content(content)
                    .unlockDate(LocalDate.parse(unlockDate))
                    .address(address)
                    .latitude(Float.parseFloat(latitude))
                    .longitude(Float.parseFloat(longitude))
                    .member(memberDTO)
                    .images(images)
                    .build();

            // 랜드마크 저장
            landmarkService.saveLandmark(landmarkDTO, member);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("랜드마크가 성공적으로 저장되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("랜드마크 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 랜드마크 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCapsule(@RequestAttribute("kakaoId") String kakaoId, @PathVariable("id") Long capsuleId) {
        System.out.println("카카오아이디 " + kakaoId );

        Member member = memberService.getMemberByKakaoId(kakaoId);

        landmarkService.deleteLandmark(capsuleId, member);

        return ResponseEntity.noContent().build();

    }

    // 랜드마크 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLandmark(@PathVariable Long id, @RequestBody LandmarkDTO updatedLandmarkDTO) {
        try {
            if (!loginUtil.isLogin()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인 상태를 확인해주세요.");
            }

            Member member = loginUtil.getMember();

            LandmarkDTO updatedLandmark = landmarkService.updateLandmark(id, updatedLandmarkDTO, member);
            return ResponseEntity.ok(updatedLandmark);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("해당 랜드마크가 존재하지 않습니다.");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("수정할 권한이 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("랜드마크 수정 중 오류가 발생했습니다.");
        }
    }
}