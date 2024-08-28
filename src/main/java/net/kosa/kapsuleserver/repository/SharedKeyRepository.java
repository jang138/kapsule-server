package net.kosa.kapsuleserver.repository;

import java.util.List;

import net.kosa.kapsuleserver.entity.SharedKey;
import net.kosa.kapsuleserver.entity.SharedKeyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedKeyRepository extends JpaRepository<SharedKey, SharedKeyId> {
	boolean existsById(SharedKeyId id);

	List<SharedKey> findAllByMemberId(Long memberId);
}
