package com.thedaymarket.config;

import com.thedaymarket.service.TokenService;
import com.thedaymarket.service.impl.JwtUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

  private final TokenService tokenService;
  private final JwtUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    final String header = req.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(req, res);
      return;
    }

    final String token = header.substring(7);
    String username;
    try {
      username = tokenService.validateTokenAndGetUserName(token);
      if (username == null || !userDetailsService.isTokenValid(token)) {
        chain.doFilter(req, res);
        return;
      }
    } catch (Exception e) {
      chain.doFilter(req, res);
      return;
    }

    var userDetails = userDetailsService.loadUserByUsername(username);
    var authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    chain.doFilter(req, res);
  }
}
