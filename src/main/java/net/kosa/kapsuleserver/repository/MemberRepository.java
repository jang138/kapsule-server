package net.kosa.kapsuleserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.kosa.kapsuleserver.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
