package portfolio.myweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.myweb.domain.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
}