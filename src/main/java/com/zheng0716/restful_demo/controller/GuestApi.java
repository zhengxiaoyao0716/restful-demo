package com.zheng0716.restful_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 游客api.
 * Created by zhengxiaoyao0716 on 2016/9/12.
 */
@RestController
@RequestMapping("/api/guest")
public class GuestApi extends BaseApi {
    @Autowired
    private HttpSession session;

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    public Map<String, Object> nowTime() {
//        request.getParameter("param");  // TODO GET方法取得URL参数
        return makeResp(DateFormat.getDateTimeInstance().format(new Date()));
    }

    @RequestMapping(value = "/time/add", method = RequestMethod.POST)
    public Map<String, Object> addTime() {
        Map<String, Object> req = getReqJson();  // TODO POST方法取得JSON参数
        if (!verify(req)) {
            return makeResp("数据签名错误，可能已被篡改");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, intFromMap(req, "year"));
        calendar.add(Calendar.MONTH, intFromMap(req, "month"));
        calendar.add(Calendar.DATE, intFromMap(req, "date"));
        return makeResp(DateFormat.getDateTimeInstance().format(calendar.getTime()));
    }

    private int intFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value instanceof Integer ? (Integer) value : 0;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestParam String account) {
        session.setAttribute("user", account);
        System.out.println("login: " + account);
        return makeResp(account, "/api/user/test");
    }

    //    @WebFilter(filterName = "guestFilter", urlPatterns = "/api/guest/*")
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
