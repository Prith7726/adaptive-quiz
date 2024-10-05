package com.adaptive.quiz;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.adaptive.quiz.controller.RegisterController.SESSION_REGISTER;

@Component
public class EntryFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if (isSessionFreeURI(request)) {
//            filterChain.doFilter(request, response);
//        }
//
//        if (request.getSession().getAttribute(SESSION_REGISTER) == null) {
//            response.sendRedirect("register");
//        }

        filterChain.doFilter(request, response);
    }

    private boolean isSessionFreeURI(HttpServletRequest request) {
        return request.getRequestURI().equals("/index") || request.getRequestURI().equals("/register");
    }
}
