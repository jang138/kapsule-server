package net.kosa.kapsuleserver.repository;

import java.util.List;

import net.kosa.kapsuleserver.entity.Capsule;
import net.kosa.kapsuleserver.entity.Member;
import net.kosa.kapsuleserver.entity.SharedKey;
import net.kosa.kapsuleserver.entity.SharedKeyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedKeyRepository extends JpaRepository<SharedKey, SharedKeyId> {
	boolean existsById(SharedKeyId id);

	boolean existsByCapsuleAndMember(Capsule capsule, Member member);

	List<SharedKey> findAllByMemberId(Long memberId);
}
