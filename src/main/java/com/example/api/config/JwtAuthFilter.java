package com.example.api.config;

import com.example.api.util.JwtUtil;
import io.jsonwebtoken.Claims;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

import com.example.api.util.ResponseHandler;
public class JwtAuthFilter implements Filter {

    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // You can leave this empty if no initialization is needed
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getRequestURI().startsWith("/api/auth")) {
            chain.doFilter(req, res);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            sendJsonError(response, "Missing Authorization header");
            return;
        }

        try {
            Claims claims = JwtUtil.parse(header.substring(7));
            if (!"access".equals(claims.get("type"))) {
                sendJsonError(response, "Invalid token");
                return;
            }
        } catch (Exception e) {
            sendJsonError(response, "Invalid or expired token");
            return;
        }

        chain.doFilter(req, res);
    }
    @Override
    public void destroy() {
        // Clean up resources if needed; otherwise, leave empty
    }
    
private void sendJsonError(HttpServletResponse response, String message) throws IOException {
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // Build response using ResponseHandler
    ResponseHandler.ApiResponseBody body = 
        new ResponseHandler.ApiResponseBody(false, message, null);

    // Serialize to JSON
    response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(body));
}
}

