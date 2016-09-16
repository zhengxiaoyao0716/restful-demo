package com.zheng0716.restful_demo.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Web监听器.
 * Created by zhengxiaoyao0716 on 2016/8/31.
 */
@javax.servlet.annotation.WebListener
public class WebListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
//        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
//        Enumeration<String> headerIterator = request.getHeaderNames();
//        while (headerIterator.hasMoreElements()) {
//            String headerName = headerIterator.nextElement();
//            System.out.println(headerName + ": " + request.getHeader(headerName));
//        }
    }
}
