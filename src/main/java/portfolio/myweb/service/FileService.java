package portfolio.myweb.service;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private final Path uploadDir;
    private final S3Client s3Client;
    private final boolean useS3;
    private final String s3Bucket;
    private final String s3PublicBaseUrl;

    public FileService(
            @Value("${app.upload-dir:uploads}") String uploadDirName,
            @Value("${app.s3.endpoint:}") String s3Endpoint,
            @Value("${app.s3.region:auto}") String s3Region,
            @Value("${app.s3.bucket:}") String s3Bucket,
            @Value("${app.s3.access-key:}") String s3AccessKey,
            @Value("${app.s3.secret-key:}") String s3SecretKey,
            @Value("${app.s3.public-base-url:}") String s3PublicBaseUrl
    ) {
        this.s3Bucket = s3Bucket;
        this.s3PublicBaseUrl = s3PublicBaseUrl;

        if (StringUtils.hasText(s3Bucket)) {
            if (!StringUtils.hasText(s3AccessKey)
                    || !StringUtils.hasText(s3SecretKey)
                    || !StringUtils.hasText(s3PublicBaseUrl)) {
                throw new IllegalStateException(
                        "S3_BUCKET 사용 시 S3_ACCESS_KEY, S3_SECRET_KEY, S3_PUBLIC_BASE_URL도 필요합니다.");
            }
            this.useS3 = true;
            this.uploadDir = null;
            this.s3Client = buildS3Client(s3Endpoint, s3Region, s3AccessKey, s3SecretKey);
        } else {
            this.useS3 = false;
            this.s3Client = null;
            Path dir = Paths.get(uploadDirName).toAbsolutePath().normalize();
            this.uploadDir = dir;
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new RuntimeException("업로드 디렉토리 생성 실패: " + dir, e);
            }
        }
    }

    private static S3Client buildS3Client(String endpoint, String region, String accessKey, String secretKey) {
        var creds = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
        String r = StringUtils.hasText(region) ? region : "auto";
        var builder = S3Client.builder()
                .credentialsProvider(creds)
                .region(Region.of(r));
        if (StringUtils.hasText(endpoint)) {
            builder.endpointOverride(URI.create(endpoint))
                    .serviceConfiguration(
                            S3Configuration.builder().pathStyleAccessEnabled(true).build());
        }
        return builder.build();
    }

    /**
     * 파일을 저장하고 접근 가능한 URL 경로를 반환합니다.
     * 로컬: "/uploads/abc-123.png", S3: 공개 URL 전체
     */
    public String saveAndReturnUrl(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }
        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.'))
                : "";
        String savedName = UUID.randomUUID() + ext;
        String key = "uploads/" + savedName;

        if (useS3) {
            String contentType = StringUtils.hasText(file.getContentType())
                    ? file.getContentType()
                    : "application/octet-stream";
            PutObjectRequest put = PutObjectRequest.builder()
                    .bucket(s3Bucket)
                    .key(key)
                    .contentType(contentType)
                    .build();
            try (var in = file.getInputStream()) {
                s3Client.putObject(put, RequestBody.fromInputStream(in, file.getSize()));
            }
            return stripTrailingSlash(s3PublicBaseUrl) + "/" + key;
        }

        Path target = uploadDir.resolve(savedName);
        Files.copy(file.getInputStream(), target);
        return "/uploads/" + savedName;
    }

    private static String stripTrailingSlash(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

    @PreDestroy
    public void closeS3() {
        if (s3Client != null) {
            s3Client.close();
        }
    }
}
