package portfolio.myweb.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AdminPasswordFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "X-Admin-Password";
    private final String adminPassword;

    public AdminPasswordFilter(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String uri = request.getRequestURI();

        // 관리자 API만 검사
        if (uri.startsWith("/api/admin")) {
            String password = request.getHeader("X-Admin-Password");

            if (password == null || !password.equals(adminPassword)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}