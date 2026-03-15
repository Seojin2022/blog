package portfolio.myweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portfolio.myweb.domain.Category;
import portfolio.myweb.domain.Post;
import portfolio.myweb.repository.CategoryRepository;
import portfolio.myweb.repository.PostRepository;
import portfolio.myweb.service.FileService;
import portfolio.myweb.service.SettingService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final SettingService settingService;
    private final FileService fileService;

    @GetMapping("/check")
    public ResponseEntity<Void> check() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings")
    public ResponseEntity<Void> saveSettings(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        Map<String, String> settings = (Map<String, String>) body.get("settings");
        if (settings != null) {
            settingService.saveAll(settings);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody Post post) {
        return postRepository.save(post);
    }

    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post post) {
        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        findPost.setTitle(post.getTitle());
        findPost.setSlug(post.getSlug());
        findPost.setContent(post.getContent());
        findPost.setThumbnail(post.getThumbnail());
        findPost.setCategoryId(post.getCategoryId());

        return postRepository.save(findPost);
    }

    @DeleteMapping("/posts/{id}")
    public String deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
        return "삭제 완료";
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/categories/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category findCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        findCategory.setName(category.getName());
        findCategory.setSlug(category.getSlug());

        return categoryRepository.save(findCategory);
    }

    @DeleteMapping("/categories/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "삭제 완료";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        // 인증은 AdminPasswordFilter에서 X-Admin-Password 헤더로 처리됨
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 없습니다.");
        }
        try {
            String url = fileService.saveAndReturnUrl(file);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 저장 실패");
        }
    }
}