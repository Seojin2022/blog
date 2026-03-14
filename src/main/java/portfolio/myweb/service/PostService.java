package portfolio.myweb.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import portfolio.myweb.dto.RecentPostDto;
import portfolio.myweb.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<RecentPostDto> findRecent(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        return postRepository.findByOrderByCreatedAtDesc(pageable)
                .stream()
                .map(p -> new RecentPostDto(p.getTitle(), p.getSlug(), p.getCreatedAt()))
                .collect(Collectors.toList());
    }
}