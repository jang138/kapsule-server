package net.kosa.kapsuleserver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDTO {

    private Long id;
    private String nickname;
    private String kakaoId;
    private String role;
}
