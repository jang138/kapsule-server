package net.kosa.kapsuleserver.entity;

import java.util.ArrayList;
import java.util.List;

import net.kosa.kapsuleserver.base.entity.BaseEntity;
import net.kosa.kapsuleserver.base.entity.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBERS")
@SuperBuilder(toBuilder = true)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false, unique = true)
	private String kakaoId;

	@Column(nullable = false, length = 15)
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<SharedKey> sharedKeys;
}
