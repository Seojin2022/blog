package portfolio.myweb.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AdminPasswordFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "X-Admin-Password";
    private final String adminPassword;

    public AdminPasswordFilter(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        if (HttpMethod.OPTIONS.matches(req.getMethod())) {
            chain.doFilter(req, res);
            return;
        }

        String path = req.getRequestURI();

        // /api/admin/** 만 비밀번호 검사
        if (!path.startsWith("/api/admin")) {
            chain.doFilter(req, res);
            return;
        }

        String provided = req.getHeader(HEADER_NAME);

        if (provided == null || provided.isBlank() || !provided.equals(adminPassword)) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType("application/json; charset=utf-8");
            res.getWriter().write("{\"message\":\"Unauthorized\"}");
            return;
        }

        chain.doFilter(req, res);
    }
}