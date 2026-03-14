package portfolio.myweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import portfolio.myweb.domain.Category;
import portfolio.myweb.repository.CategoryRepository;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}