package portfolio.myweb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    private String title;
    private String slug;
    private String content;
    private String thumbnail;
    private Long categoryId;
}