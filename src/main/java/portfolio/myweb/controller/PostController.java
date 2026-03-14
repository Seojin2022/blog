package portfolio.myweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.myweb.domain.Post;
import portfolio.myweb.dto.RecentPostDto;
import portfolio.myweb.repository.PostRepository;
import portfolio.myweb.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    @GetMapping
    public List<Post> getPosts(@RequestParam(required = false) Integer limit) {
        List<Post> posts = postRepository.findAll();

        if (limit != null && limit < posts.size()) {
            return posts.subList(0, limit);
        }

        return posts;
    }

    @GetMapping("/{slug}")
    public Post getPost(@PathVariable String slug) {
        return postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<RecentPostDto>> getRecent(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(postService.findRecent(limit));
    }
}