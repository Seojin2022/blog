package portfolio.myweb.dto;

import java.time.Instant;

public record RecentPostDto(String title, String slug, Instant created_at) {}