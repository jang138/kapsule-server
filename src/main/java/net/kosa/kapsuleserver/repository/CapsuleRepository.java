package net.kosa.kapsuleserver.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import net.kosa.kapsuleserver.entity.Capsule;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
	boolean existsByCapsuleCode(String capsuleCode);
	List<Capsule> findAllByMemberId(Long id);
}
