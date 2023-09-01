package com.thedaymarket.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.thedaymarket.controllers.filter.JwtRequestFilter;
import com.thedaymarket.service.TokenService;
import com.thedaymarket.service.impl.JwtUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class WebSecurityConfig {
  private final JwtUserDetailsService userDetailsService;
  private final TokenService tokenService;
  private final AuthEntryPoints unauthorizedHandler;

  @Bean
  public JwtRequestFilter authFilter() {
    return new JwtRequestFilter(tokenService, userDetailsService);
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    var provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/api/v1/auth/**")
                    .permitAll()
                    .requestMatchers(
                        "/web/**",
                        "/images/**",
                        "/jquery.js",
                        "/main.css",
                        "/main.js",
                        "/error",
                        "/js/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .httpBasic(withDefaults());

    http.cors(
        cors ->
            cors.configurationSource(
                request -> {
                  var config = new CorsConfiguration();
                  config.addAllowedHeader("*");
                  config.addAllowedMethod("*");
                  config.addAllowedOrigin("*");
                  config.setAllowCredentials(true);
                  return config;
                }));

    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
