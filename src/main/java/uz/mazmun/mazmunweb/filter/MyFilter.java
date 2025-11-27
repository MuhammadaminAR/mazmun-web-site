package uz.mazmun.mazmunweb.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.mazmun.mazmunweb.security.CustomUserDetailsService;
import uz.mazmun.mazmunweb.service.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MyFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    // <<— MUHIM: public GET va login/refresh so‘rovlarida filterni umuman ishlatmaymiz
    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String m = req.getMethod();
        String u = req.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(m)) return true;
        if (u.equals("/api/auth/login") || u.equals("/api/auth/refresh")) return true;

        boolean publicGet =
                "GET".equalsIgnoreCase(m) && (
                        u.startsWith("/api/services")
                                || u.startsWith("/api/posts")
                                || u.startsWith("/api/projects")
                                || u.startsWith("/api/categories")
                                || u.startsWith("/api/media")
                                || u.startsWith("/files")
                                || u.startsWith("/api/auth/login")
                                || u.startsWith("/frontend")
                );
        return publicGet;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        // Allaqachon authenticated bo‘lsa — o‘tkazamiz
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(req, res);
            return;
        }

        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            chain.doFilter(req, res); // anonym davom etadi
            return;
        }

        String token = auth.substring(7);
        try {
            if (!jwtService.validate(token)) {
                // Muhim o‘zgarish: 401 qaytarmaymiz, anonym sifatida davom etsin.
                chain.doFilter(req, res);
                return;
            }

            String username = jwtService.getUsername(token);
            UserDetails ud = userDetailsService.loadUserByUsername(username);

            var at = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
            at.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(at);

            chain.doFilter(req, res);
        } catch (Exception e) {
            // Xuddi shunday: xato bo‘lsa ham anonym davom etsin — secured route’larda baribir 401 chiqadi
            chain.doFilter(req, res);
        }
    }
}
