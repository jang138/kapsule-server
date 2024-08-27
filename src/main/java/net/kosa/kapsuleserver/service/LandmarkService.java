package net.kosa.kapsuleserver.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.kosa.kapsuleserver.base.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.kosa.kapsuleserver.dto.CapsuleDTO;
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
    @Transactional(readOnly = true)
    public List<CapsuleDTO> findAllLandmarks() {
        // capsuleType이 2인 데이터를 조회
        List<Capsule> landmarks = capsuleRepository.findByCapsuleType(2);
        return convertToDTO(landmarks);
    }

    // 특정 랜드마크 조회
    @Transactional(readOnly = true)
    public CapsuleDTO findLandmarkById(Long id) {
        Capsule landmark = capsuleRepository.findById(id)
                .filter(capsule -> capsule.getCapsuleType() == 2) // capsuleType이 2인지 확인
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 랜드마크입니다."));
        return convertToDTO(landmark);
    }

    // 새 랜드마크 생성
    @Transactional
    public void saveLandmark(CapsuleDTO capsuleDTO, Member member) {
        Capsule capsule = Capsule.builder()
                .member(member)
                .title(capsuleDTO.getTitle())
                .content("{\"daterange\": \"" + capsuleDTO.getContent().getDaterange() + "\","
                        + " \"subtitle\": \"" + capsuleDTO.getContent().getSubtitle() + "\","
                        + " \"text\": \"" + capsuleDTO.getContent().getText() + "\"}")
                .address(capsuleDTO.getAddress())
                .longitude(capsuleDTO.getCoordinates().getLng())
                .latitude(capsuleDTO.getCoordinates().getLat())
                .unlockDate(capsuleDTO.getUnlockDate())
                .capsuleCode(capsuleService.createRandomCode(8))
                .capsuleType(member.getRole() == Role.ROLE_ADMIN ? 2 : 1)
                .build();

        capsuleRepository.save(capsule);
    }

    // 랜드마크 수정
    @Transactional
    public CapsuleDTO updateLandmark(Long id, CapsuleDTO updatedCapsuleDTO) {
        Capsule capsule = capsuleRepository.findById(id)
                .filter(c -> c.getCapsuleType() == 2)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 랜드마크입니다."));

        try {
            String contentJson = objectMapper.writeValueAsString(updatedCapsuleDTO.getContent());

            capsule = capsule.toBuilder()
                    .title(updatedCapsuleDTO.getTitle())
                    .content(contentJson)
                    .address(updatedCapsuleDTO.getAddress())
                    .longitude(updatedCapsuleDTO.getCoordinates().getLng())
                    .latitude(updatedCapsuleDTO.getCoordinates().getLat())
                    .unlockDate(updatedCapsuleDTO.getUnlockDate())
                    .build();

            capsule = capsuleRepository.save(capsule);
            return convertToDTO(capsule);
        } catch (Exception e) {
            throw new RuntimeException("랜드마크 수정 중 오류가 발생했습니다.", e);
        }
    }

     // 랜드마크 삭제
    @Transactional
    public void deleteLandmark(Long id) {
        Capsule capsule = capsuleRepository.findById(id)
                .filter(c -> c.getCapsuleType() == 2) // capsuleType이 2인지 확인
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 랜드마크입니다."));

        capsuleRepository.delete(capsule);
    }

    // 캡슐 리스트를 DTO로 변환
    private List<CapsuleDTO> convertToDTO(List<Capsule> capsuleList) {
        return capsuleList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 단일 캡슐을 DTO로 변환
    private CapsuleDTO convertToDTO(Capsule capsule) {
        try {
            CapsuleDTO.Content content = objectMapper.readValue(capsule.getContent(), CapsuleDTO.Content.class);

            return CapsuleDTO.builder()
                    .id(capsule.getId())
                    .title(capsule.getTitle())
                    .content(content)
                    .address(capsule.getAddress())
                    .capsuleCode(capsule.getCapsuleCode())
                    .capsuleType(capsule.getCapsuleType())
                    .unlockDate(capsule.getUnlockDate())
                    .coordinates(CapsuleDTO.Coordinates.builder()
                            .lat(capsule.getLatitude())
                            .lng(capsule.getLongitude())
                            .build())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("DTO 변환 중 오류가 발생했습니다.", e);
        }
    }
}