package uz.mazmun.mazmunweb.security;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import uz.mazmun.mazmunweb.filter.MyFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final MyFilter myFilter;
    private final org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfigurationSource))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Ichki forward'lar (Controller -> forward:/frontend/...) ruxsat
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()

                        // Preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"api/public/messages").permitAll()

                        // Auth API (REST)
                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/login",
                                "/api/auth/refresh").permitAll()

                        // Umumiy statiklar (spring-common, uploads, favicon)
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/files/**", "/uploads/**", "/favicon.ico").permitAll()

                        // FRONTEND HTML — to'g'ridan-to'g'ri ochishni bloklaymiz (faqat GET)
                        .requestMatchers(new RegexRequestMatcher("^/frontend/.+\\.html$", "GET")).denyAll()

                        // FRONTEND assetlar (css/js/img/font va hokazo) — barchasi ochiq
                        .requestMatchers("/frontend/**").permitAll()

                        // Public GET API'lar
                        .requestMatchers(HttpMethod.GET,
                                "/api/services/**",
                                "/api/posts/**",
                                "/api/projects/**",
                                "/api/categories/**",
                                "/api/media/**",
                                "/api/trusted-user-logos",
                                "/api/team-members",
                                "/api/homepage-video",
                                "api/project/count",
                                "api/home/kpis",
                                "/api/pricing",
                                "/api/pricing/matrix"
                        ).permitAll()

                        // Admin API
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                        .requestMatchers("/api/admin/homepage-video").hasAnyRole("ADMIN","SUPER_ADMIN")
                        .requestMatchers("/api/admin/pricing/**").hasAnyRole("ADMIN","SUPER_ADMIN")


                        // Qolgan barcha /api/** — auth talab qiladi
                        .requestMatchers("/api/**").authenticated()

                        // Qolgan so'rovlar — ochiq
                        .anyRequest().permitAll()
                )

                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req,res,ex) -> res.sendError(401))
                        .accessDeniedHandler((req,res,ex) -> res.sendError(403))
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider());
    }
}
