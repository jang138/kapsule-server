package net.kosa.kapsuleserver.service;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.kosa.kapsuleserver.base.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.kosa.kapsuleserver.dto.LandmarkDTO;
import net.kosa.kapsuleserver.entity.Capsule;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.repository.CapsuleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LandmarkService {

    private final CapsuleRepository capsuleRepository;
    private final CapsuleService capsuleService;
    private final ObjectMapper objectMapper; // JSON 변환용 ObjectMapper 추가

    // 모든 랜드마크 조회
    @Transactional
    public List<LandmarkDTO> findAllLandmarks() {
        // capsuleType이 2인 데이터를 조회
        List<Capsule> landmarks = capsuleRepository.findByCapsuleType(2);
        return convertToDTO(landmarks);
    }

    // 특정 랜드마크 조회
    @Transactional
    public LandmarkDTO findLandmarkById(Long id) {
        Capsule landmark = capsuleRepository.findById(id)
//                .filter(capsule -> capsule.getCapsuleType() == 2) // capsuleType이 2인지 확인
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 랜드마크입니다."));
        return convertToDTO(landmark);
    }

    // 새 랜드마크 생성
    @Transactional
    public void saveLandmark(LandmarkDTO landmarkDTO, Member member) {
        try {
            String contentJson = objectMapper.writeValueAsString(landmarkDTO.getContent());

            Capsule capsule = Capsule.builder()
                    .member(member)
                    .title(landmarkDTO.getTitle())
                    .content(contentJson)
                    .address(landmarkDTO.getAddress())
                    .longitude(landmarkDTO.getCoordinates().getLng())
                    .latitude(landmarkDTO.getCoordinates().getLat())
                    .unlockDate(landmarkDTO.getUnlockDate())
                    .capsuleCode(capsuleService.createRandomCode(8))
                    .capsuleType(member.getRole() == Role.ROLE_ADMIN ? 2 : 1)
                    .build();

            capsuleRepository.save(capsule);
        } catch (Exception e) {
            throw new RuntimeException("랜드마크 생성 중 오류가 발생했습니다.", e);
        }
    }

//    // 랜드마크 수정
//    @Transactional
//    public LandmarkDTO updateLandmark(Long id, LandmarkDTO updatedLandmarkDTO) {
//        Capsule capsule = capsuleRepository.findById(id)
//                .filter(c -> c.getCapsuleType() == 2)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 랜드마크입니다."));
//
//        try {
//            String contentJson = objectMapper.writeValueAsString(updatedLandmarkDTO.getContent());
//
//            capsule = capsule.toBuilder()
//                    .title(updatedLandmarkDTO.getTitle())
//                    .content(contentJson)
//                    .address(updatedLandmarkDTO.getAddress())
//                    .longitude(updatedLandmarkDTO.getCoordinates().getLng())
//                    .latitude(updatedLandmarkDTO.getCoordinates().getLat())
//                    .unlockDate(updatedLandmarkDTO.getUnlockDate())
//                    .build();
//
//            capsule = capsuleRepository.save(capsule);
//            return convertToDTO(capsule);
//        } catch (Exception e) {
//            throw new RuntimeException("랜드마크 수정 중 오류가 발생했습니다.", e);
//        }
//    }

    // 랜드마크 삭제
    @Transactional
    public void deleteLandmark(Long capsuleId, Member member) {
        Capsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 랜드마크입니다."));

        if (!capsule.getMember().getKakaoId().equals(member.getKakaoId())) {
            throw new SecurityException("랜드마크를 삭제할 권한이 없습니다.");
        }

        capsuleRepository.deleteById(capsuleId);
    }

    // 캡슐 리스트를 DTO로 변환
    private List<LandmarkDTO> convertToDTO(List<Capsule> capsuleList) {
        return capsuleList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /// 단일 캡슐을 DTO로 변환
    private LandmarkDTO convertToDTO(Capsule capsule) {
        LandmarkDTO.Content content = null;
        try {
            // JSON 파싱 시도
            content = objectMapper.readValue(capsule.getContent(), LandmarkDTO.Content.class);
        } catch (Exception e) {
        }

        return LandmarkDTO.builder()
                .id(capsule.getId())
                .title(capsule.getTitle())
                .content(content) // 파싱된 content가 있으면 사용, 아니면 null
                .address(capsule.getAddress())
                .capsuleCode(capsule.getCapsuleCode())
                .capsuleType(capsule.getCapsuleType())
                .unlockDate(capsule.getUnlockDate())
                .coordinates(LandmarkDTO.Coordinates.builder()
                        .lat(capsule.getLatitude())
                        .lng(capsule.getLongitude())
                        .build())
                .build();
    }
}