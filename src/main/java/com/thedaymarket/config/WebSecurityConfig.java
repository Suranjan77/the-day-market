package com.thedaymarket.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.thedaymarket.service.TokenService;
import com.thedaymarket.service.impl.JwtUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
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
  public DelegatingSecurityContextAsyncTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(100);
    executor.setQueueCapacity(50);
    executor.setThreadNamePrefix("async-");
    executor.initialize();
    return new DelegatingSecurityContextAsyncTaskExecutor(executor);
  }

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
        .sessionManagement(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/stream/**")
                    .permitAll()
                    .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/web/**",
                        "/api/v1/image",
                        "/images/**",
                        "/jquery.js",
                        "/main.css",
                        "/main.js",
                        "/error",
                        "/lib/**",
                        "/favicon.ico",
                        "api/v1/admin/**",
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
