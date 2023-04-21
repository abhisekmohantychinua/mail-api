package dev.abhisek.mailsenderapi.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//    1.get token
        String authHeader = request.getHeader("Authorization");
        System.out.println("request token" + authHeader);

        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = this.jwtTokenHelper.getUsernameFromToken(token);


            } catch (IllegalArgumentException e) {

                System.out.println("Unable to get jwt token");

            } catch (ExpiredJwtException e) {

                System.out.println("Token has expired");

            } catch (MalformedJwtException e) {

                System.out.println("Invalid token");

            }
        }

        //validate
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            System.out.println("filter "+userDetails);
            if (this.jwtTokenHelper.validateToken(token, userDetails)) {
                System.out.println("validated");
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null);

                usernamePasswordAuthenticationToken
                        .setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
        }

        filterChain.doFilter(request, response);

    }
}
