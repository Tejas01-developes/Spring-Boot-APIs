package com.practice.keepgowing.filter;

import com.practice.keepgowing.generet.generat;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class filter implements Filter {
    @Autowired
    private generat generat;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

      HttpServletResponse resp=(HttpServletResponse) response;
      HttpServletRequest req=(HttpServletRequest) request;

      String path=req.getRequestURI();

      if(path.contains("/register") || path.contains("/login")){

          chain.doFilter(req,resp);
      }

      String authheader=req.getHeader("Authorization");
      if(authheader.startsWith("Bearer") && authheader!=null){
          String substring=authheader.substring(7);

          if(generat.verifytoken(substring)){
              String username= generat.extractusername(substring);
              req.setAttribute("username",username);
          }
          chain.doFilter(req,resp);

      }


        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        resp.getWriter().write("unauthorized token or invalid token");





    }
}
