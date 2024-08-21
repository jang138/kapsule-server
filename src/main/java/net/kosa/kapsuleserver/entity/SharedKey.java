package net.kosa.kapsuleserver.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SHAREDKEYS")
@SuperBuilder(toBuilder = true)
public class SharedKey implements Serializable {

	@EmbeddedId
	@Getter(AccessLevel.NONE)
	private SharedKeyId id;

	@Column(nullable = false)
	private boolean opened;

	@ManyToOne
	@MapsId("memberId")
	@JoinColumn(name = "MEMBER_ID", nullable = false)
	private Member member;

	@ManyToOne
	@MapsId("capsuleId")
	@JoinColumn(name = "CAPSULE_ID", nullable = false)
	private Capsule capsule;

}
