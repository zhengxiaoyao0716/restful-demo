package com.zheng0716.restful_demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 用户api.
 * Created by zhengxiaoyao0716 on 2016/9/11.
 */
@RestController
@RequestMapping("/api/user")
public class UserApi extends BaseApi {
    @RequestMapping(value = "/test")
    public Map<String, Object> testUser(@RequestParam(value = "param", required = false, defaultValue = "Default") String param) {
        return makeResp("fin, param=" + param);
    }

    @WebFilter(filterName = "userFilter", urlPatterns = "/api/user/*")
    public static class UserFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
            String user = (String) httpServletRequest.getSession().getAttribute("user");
            if (user == null) {
                ((HttpServletResponse) response).sendRedirect("/401.html");
            } else {
                System.out.println("user: " + user);
                chain.doFilter(request, response);
            }
        }

        @Override
        public void destroy() {

        }
    }
}
