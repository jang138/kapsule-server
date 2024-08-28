package net.kosa.kapsuleserver.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Lob;
import net.kosa.kapsuleserver.entity.Member;

import lombok.Builder;
import lombok.Getter;


// landmarkDTO는 타임캡슐과 관련된 데이터를 전달합니다.
@Getter
@Builder
public class LandmarkDTO {

    @JsonProperty("location")
    private String address;

    private Long id;
    private String title;
    private LocalDate unlockDate;
    private String capsuleCode;
    private int capsuleType;
    private String image;

    private Member member;
    private Coordinates coordinates;  // 좌표 정보 추가
    private Content content;
// content 필드를 String으로 유지하여, HTML로 처리
//@Column(nullable = false)
//@Lob
//private String content;

    // 내부 클래스 정의
    @Getter
    @Builder
    public static class Coordinates {
        private Float lat;
        private Float lng;
    }

    @Getter
    @Builder
    public static class Content {
        private String daterange;
        private String subtitle;
        private String text;
    }
}
