package com.zheng0716.restful_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * 游客api.
 * Created by zhengxiaoyao0716 on 2016/9/12.
 */
@RestController
@RequestMapping("/api/user")
public class GuestApi extends BaseApi {
    @Autowired
    private HttpSession session;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestParam String account) {
        session.setAttribute("user", account);
        System.out.println("login: " + account);
        return makeResp(account, "/api/user/test");
    }

    @WebFilter(filterName = "guestFilter", urlPatterns = "/api/guest/*")
    public static class GuestFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {

        }
    }
}
