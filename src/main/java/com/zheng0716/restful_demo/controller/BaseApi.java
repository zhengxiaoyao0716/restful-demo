package com.zheng0716.restful_demo.controller;

import com.zheng0716.restful_demo.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础api.
 * Created by zhengxiaoyao0716 on 2016/9/11.
 */
@RestController
@RequestMapping("/api")
@SuppressWarnings({"SpellCheckingInspection", "WeakerAccess"})
public class BaseApi {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpSession session;

    protected Map<String, Object> makeResp(Object body) {
        return makeResp(body, null);
    }

    protected Map<String, Object> makeResp(Object body, String then) {
        Map<String, Object> resp = new HashMap<String, Object>();
        resp.put("flag", true);
        resp.put("body", body);
        resp.put("then", then);
        return resp;
    }

    protected Map<String, Object> makeErr(String reas) {
        return makeErr(reas, null);
    }

    protected Map<String, Object> makeErr(String reas, String then) {
        Map<String, Object> resp = new HashMap<String, Object>();
        resp.put("flag", false);
        resp.put("reas", reas);
        resp.put("then", then);
        return resp;
    }

    @WebFilter(filterName = "baseFilter", urlPatterns = "/api/*")
    public static class BaseFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
            String accesskey = httpServletRequest.getHeader("accesskey");
            String timestamp = httpServletRequest.getHeader("timestamp");
            String authorization = httpServletRequest.getHeader("authorization");
            if (accesskey == null || timestamp == null || authorization == null) {
                ((HttpServletResponse) response).sendRedirect("/404.html");
            } else if (!authorization.equals(SecurityUtil.getInstance(accesskey).hmacSha256Hex(timestamp))) {
                ((HttpServletResponse) response).sendRedirect("/403.html");
            } else {
                chain.doFilter(request, response);
            }
        }

        @Override
        public void destroy() {

        }
    }
}
