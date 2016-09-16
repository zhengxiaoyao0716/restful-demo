package com.zheng0716.restful_demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
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

    /**
     * 校验数据是否有效（是否未被篡改）.
     *
     * @param content 要校验的数据
     * @return 是否有效
     */
    protected boolean verify(Map<String, Object> content) {
        SecurityUtil securityUtil = (SecurityUtil) request.getAttribute("securityUtil");
        return securityUtil.verify(content);
    }

    /**
     * 获取Json数据.
     *
     * @return Json数据（null if failed.）
     */
    protected Map<String, Object> getReqJson() {
        Map<String, Object> reqJson = (Map<String, Object>) request.getAttribute("reqJson");
        if (reqJson != null) {
            return reqJson;
        }

//        if (!"POST".equalsIgnoreCase(request.getMethod())) {
//            return null;
//        }
//        String contentType = request.getHeader("content-type");
//        if (contentType == null || !contentType.contains("application/json")) {
//            return null;
//        }
        try {
            reqJson = JSON.parseObject(request.getInputStream(), JSONObject.class);
        } catch (JSONException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        request.setAttribute("reqJson", reqJson);
        return reqJson;
    }

    @WebFilter(filterName = "baseFilter", urlPatterns = "/api/*")
    public static class BaseFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
            SecurityUtil securityUtil = SecurityUtil.getInstance(httpServletRequest.getHeader("accesskey"));
            String timestamp = httpServletRequest.getHeader("timestamp");
            String authorization = httpServletRequest.getHeader("authorization");
            if (securityUtil == null || timestamp == null || authorization == null) {
                ((HttpServletResponse) response).sendRedirect("/404.html");
            } else if (!authorization.equals(securityUtil.hmacSha256Hex(timestamp))) {
                ((HttpServletResponse) response).sendRedirect("/403.html");
            } else {
                request.setAttribute("securityUtil", securityUtil);
                chain.doFilter(request, response);
            }
        }

        @Override
        public void destroy() {

        }
    }
}
