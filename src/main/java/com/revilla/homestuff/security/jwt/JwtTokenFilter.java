package com.revilla.homestuff.security.jwt;

import com.revilla.homestuff.repository.UserRepository;
import com.revilla.homestuff.util.UserUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = this.jwtTokenProvider.getJwtTokenFromRequest(request);
            if (this.jwtTokenProvider.validateJwtToken(token)) {
                Claims body = this.jwtTokenProvider.getJwtBody(token);
                UserDetails userDetails = UserUtil.getUserDetailsOrThrow(body.getSubject(), this.userRepository);
                UsernamePasswordAuthenticationToken authentication =  new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Can't set user authentication in security context {}", ex.getMessage());
        }
        filterChain.doFilter(request, response);
    }

}
