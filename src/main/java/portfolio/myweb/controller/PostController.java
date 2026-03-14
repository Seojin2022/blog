package portfolio.myweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import portfolio.myweb.domain.Post;
import portfolio.myweb.repository.PostRepository;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    @GetMapping
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/{slug}")
    public Post getPost(@PathVariable String slug) {
        return postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }
}