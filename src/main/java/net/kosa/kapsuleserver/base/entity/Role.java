package net.kosa.kapsuleserver.base.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	ADMIN("관리자"), FREE_USER("무료회원"), PAID_USER("유료회원");

	private final String value;
}
