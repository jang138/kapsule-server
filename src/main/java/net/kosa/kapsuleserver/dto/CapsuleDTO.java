package net.kosa.kapsuleserver.dto;

import java.time.LocalDate;

import net.kosa.kapsuleserver.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CapsuleDTO {

	private Long id;
	private Member member;
	private String title;
	private String content;
	private String address;
	private Float longitude;
	private Float latitude;
	private LocalDate unlockDate;
	private String capsuleCode;

}
