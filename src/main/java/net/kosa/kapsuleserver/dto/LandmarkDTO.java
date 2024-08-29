package net.kosa.kapsuleserver.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    private Float longitude;
    private Float latitude;
    private MemberDTO member;
    private Content content;

    @Getter
    @Builder
    public static class Content {
        private String daterange;
        private String subtitle;
        private String text;
    }
}