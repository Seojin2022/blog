package portfolio.myweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.myweb.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}