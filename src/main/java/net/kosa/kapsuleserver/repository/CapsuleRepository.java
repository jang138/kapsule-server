package net.kosa.kapsuleserver.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import net.kosa.kapsuleserver.entity.Capsule;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
	boolean existsByCapsuleCode(String capsuleCode);

	List<Capsule> findAllByMemberId(Long memberId);
	Optional<Capsule> findByCapsuleCode(String capsuleCode);

	// 랜드마크 데이터인지 구분
	List<Capsule> findByCapsuleType(int capsuleType);
}
