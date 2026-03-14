package portfolio.myweb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}