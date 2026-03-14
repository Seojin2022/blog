package portfolio.myweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.myweb.domain.Post;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
    List<Post> findByOrderByCreatedAtDesc(Pageable pageable);
}