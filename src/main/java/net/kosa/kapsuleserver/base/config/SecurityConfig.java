//package net.kosa.kapsuleserver.base.config;
//
//import lombok.RequiredArgsConstructor;
//import net.kosa.kapsuleserver.service.CustomOAuth2UserService;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configurable
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final CustomOAuth2UserService customOAuth2UserService;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .cors(withDefaults()); // CORS 설정 활성화
//;
//        http
//                .csrf(AbstractHttpConfigurer::disable);
//
//        http
//                .formLogin(AbstractHttpConfigurer::disable);
//
//        http
//                .httpBasic(AbstractHttpConfigurer::disable);
//
////        http
////                .oauth2Login((oauth2) -> oauth2
////                        .userInfoEndpoint((userInfoEndpointConfig) ->
////                                userInfoEndpointConfig.userService(customOAuth2UserService)));
//
//
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/", "/oauth2/**", "/login/**", "/api/test").permitAll()
//                        .anyRequest().authenticated());
//
//        http
//                .sessionManagement(session -> session
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(true)
//        );
//
//        return http.build();
//    }
//}
