package net.kosa.kapsuleserver.entity;

import net.kosa.kapsuleserver.base.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "IMAGES")
@SuperBuilder(toBuilder = true)
public class Image extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String path;

	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "CAPSULE_ID", nullable = false)
	private Capsule capsule;
}
