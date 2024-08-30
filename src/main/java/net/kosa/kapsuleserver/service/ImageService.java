package net.kosa.kapsuleserver.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.dto.ImageDTO;
import net.kosa.kapsuleserver.entity.Capsule;
import net.kosa.kapsuleserver.entity.Image;
import net.kosa.kapsuleserver.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${image.upload.dir}")
    private String imageUploadDir;
    private final ImageRepository imageRepository;

    /**
     * 이미지 dir 조회 및 생성 초기화
     */
    @PostConstruct
    public void init() {
        File uploadDir = new File(imageUploadDir);
        if(!uploadDir.exists()) {
            boolean dirCreated = uploadDir.mkdirs();

            if(dirCreated) {
                throw new RuntimeException("Image upload directory could not be created");
            }
        }
    }

    /**
     * 이미지 저장
     */
    @Transactional(readOnly = true)
    public ResponseEntity<?> save(Capsule capsule, List<MultipartFile> images) {
        try {
            for(MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String fileName = makeFileName(image.getOriginalFilename() != null
                                                    ? image.getOriginalFilename() : "temp");
                    Path savePath = Paths.get(imageUploadDir, fileName);

                    Files.copy(image.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

                    Image saveImage = Image.builder()
                            .capsule(capsule)
                            .path("images/" + fileName)
                            .build();

                    imageRepository.save(saveImage);
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("파일 업로드 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 이미지 조회
     */
    @Transactional(readOnly = true)
    public List<Image> getImage(Long capsuleId) {
        return imageRepository.findByCapsuleId(capsuleId);
    }

    /**
     * 이미지 파일 이름 생성 메소드
     */
    private String makeFileName(String originName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String uniqueID = UUID.randomUUID().toString();
        String extension = originName.substring(originName.lastIndexOf("."));

        return timestamp + "_" + uniqueID.substring(0, uniqueID.indexOf("-")) + extension;
    }
}
