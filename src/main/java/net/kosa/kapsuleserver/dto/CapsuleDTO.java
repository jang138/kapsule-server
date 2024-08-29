package net.kosa.kapsuleserver.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

/**
 * @author dayoung
 * CapsuleDTO는 타임캡슐과 관련된 데이터를 전달합니다.
 */
@Getter
@Builder
public class CapsuleDTO {

	private Long id;
	private MemberDTO member;
	private String title;
	private String content;
	private String address;
	private Float longitude;
	private Float latitude;
	private LocalDate unlockDate;
	private String capsuleCode;
	private String kakaoId;

}
