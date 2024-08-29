package net.kosa.kapsuleserver.repository;

import net.kosa.kapsuleserver.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
