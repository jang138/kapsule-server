package net.kosa.kapsuleserver.repository;

import net.kosa.kapsuleserver.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByCapsuleId(Long capsuleId);
}
