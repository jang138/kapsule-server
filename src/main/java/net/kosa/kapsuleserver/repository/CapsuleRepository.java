package net.kosa.kapsuleserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import net.kosa.kapsuleserver.entity.Capsule;

/**
 * @author dayoung
 * CapsuleRepository는 타임캡슐과 관련된 데이터를 DB에서 가져옵니다.
 */
public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
	boolean existsByCapsuleCode(String capsuleCode);
}
