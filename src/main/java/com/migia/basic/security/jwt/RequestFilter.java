package com.migia.basic.security.jwt;

import com.migia.basic.models.User;
import com.migia.basic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestFilter extends OncePerRequestFilter {
    final static Logger logger = LoggerFactory.getLogger(RequestFilter.class);
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            logger.info(jwt);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                System.out.println("Inside the do filter function ");
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                User userDetails = (User) userService.loadUserByUsername(username);
                logger.info("In the doFilterInternal function now...");
                logger.info(userDetails.getName());
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }
    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromRequest(request);
        return jwt;
    }
}

