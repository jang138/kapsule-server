package net.kosa.kapsuleserver.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class ImageDTO {

    private Long id;
    private String path;
    private Long capsuleId;
    private MultipartFile image;

}
