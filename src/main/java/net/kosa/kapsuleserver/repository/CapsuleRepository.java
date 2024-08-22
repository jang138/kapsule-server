package net.kosa.kapsuleserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.kosa.kapsuleserver.entity.Capsule;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
	boolean existsByCapsuleCode(String capsuleCode);
}
