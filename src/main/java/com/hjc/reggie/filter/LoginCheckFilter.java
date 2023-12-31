package com.hjc.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.hjc.reggie.common.BaseContext;
import com.hjc.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 定义拦截器，拦截某些url
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        log.info("本次请求：{}",requestURI);
        //定义不需要拦截的url
        String[] urls = {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        boolean check = check(urls, requestURI);
        if (check) {
            log.info("本次请求不用作处理");
            filterChain.doFilter(request, response);
            return;
        }

        //4-1判断用户是否登录，登录则放行
        if (request.getSession().getAttribute("employee") != null) {
            Long id = (Long) request.getSession().getAttribute("employee");
            log.info("用户已登录，本次登入的用户id为：{}",id);
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request, response);
            return;
        }
        //4-2移动端判断用户是否登录，登录则放行
        if (request.getSession().getAttribute("user") != null) {
            Long id = (Long) request.getSession().getAttribute("user");
            log.info("用户已登录，本次登入的用户id为：{}",id);
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登入");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 检查地址是否该被拦截
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return match;
            }
        }
        return false;
    }

}
