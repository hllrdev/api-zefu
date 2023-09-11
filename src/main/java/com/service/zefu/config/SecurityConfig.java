package com.service.zefu.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.service.zefu.security.jwt.AuthenticationFilterJwt;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private AuthenticationFilterJwt authenticationFilterJwt;

    // private CustomOAuth2Service customOAuth2Service;

    public SecurityConfig(AuthenticationFilterJwt authenticationFilterJwt)
    // , CustomOAuth2Service customOAuth2Service) 
    {
        this.authenticationFilterJwt = authenticationFilterJwt;
        // this.customOAuth2Service = customOAuth2Service;
    }

   @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors((cors) -> cors.disable())
            .csrf((csrf) -> csrf.disable())
            .authorizeHttpRequests((request) -> 
                request
                .requestMatchers("/", "/auth/**").permitAll()
                .requestMatchers("/auth/reset").authenticated()
                .requestMatchers(("/static/images/*")).permitAll()
                .requestMatchers("/email").permitAll()
                .requestMatchers("/products/main", "/products").permitAll()
                .anyRequest().authenticated())
                // .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(authenticationFilterJwt, UsernamePasswordAuthenticationFilter.class
            );

            // .oauth2Login((login) -> 

            //     login.userInfoEndpoint((userInfo) -> userInfo.userService(customOAuth2Service))
            //         .successHandler((request, response, authentication) -> {
            //             DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
            //             Map<String, Object> attributes = oidcUser.getAttributes();
            //             String name = attributes.get("name").toString();
            //             String email = attributes.get("email").toString();
            //             customOAuth2Service.processOAuth2Login(email, name);
            //             response.sendRedirect("http://localhost:3000/calculator");
            // }));
            
        return http.build();
    }

    @Bean
	public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public CorsFilter corsFilter() {
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     CorsConfiguration config = new CorsConfiguration();
    //     config.setAllowCredentials(true);
    //     config.setAllowedOriginPatterns(List.of("http://localhost:3000", "https://www.zefu.com.br"));
    //     config.addAllowedHeader("*");
    //     config.addAllowedMethod("*");
    //     source.registerCorsConfiguration("/**", config);
    //     return new CorsFilter(source);
    // }
}
