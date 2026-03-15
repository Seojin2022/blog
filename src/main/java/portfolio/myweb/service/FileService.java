package portfolio.myweb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private final Path uploadDir;

    public FileService(@Value("${app.upload-dir:uploads}") String uploadDirName) {
        this.uploadDir = Paths.get(uploadDirName).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("업로드 디렉토리 생성 실패: " + this.uploadDir, e);
        }
    }

    /**
     * 파일을 저장하고 접근 가능한 URL 경로를 반환합니다.
     * 예: "/uploads/abc-123.png"
     */
    public String saveAndReturnUrl(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }
        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.'))
                : "";
        String savedName = UUID.randomUUID().toString() + ext;
        Path target = uploadDir.resolve(savedName);
        Files.copy(file.getInputStream(), target);
        return "/uploads/" + savedName;
    }
}
