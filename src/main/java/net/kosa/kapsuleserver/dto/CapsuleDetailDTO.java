package net.kosa.kapsuleserver.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

/* capsuleType을 포함하여 필요한 필드를 담는 새로운 DTO 클래스 */
@Getter
@Builder
public class CapsuleDetailDTO {

    private Long id;
    private String title;
    private String content;
    private String address;
    private Float longitude;
    private Float latitude;
    private LocalDate unlockDate;
    private Integer capsuleType;
    private List<String> images;

}