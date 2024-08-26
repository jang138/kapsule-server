package net.kosa.kapsuleserver.base.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	ROLE_ADMIN("관리자"), ROLE_FREEUSER("무료회원"), ROLE_PAIDUSER("유료회원");

	private final String value;
}
