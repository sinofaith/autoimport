package cn.com.sinofaith.util;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class LoginInterceptor implements HandlerInterceptor {
    final String match = "30128102a1220a2a608da0ed782ee281e46cfcea";//localhost:8080
//    final String match = "a58391bed4d09cb50fb14e2e6b253ace836fb1e8";//http://47.103.33.233:8080
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String url = Encrypt.md5AndSha(Arrays.asList(request.getRequestURL().toString().split("/")).get(2));

        Object user = request.getSession().getAttribute("user");
        if(user == null||!url.equals(match)){
            //如果request.getHeader("X-Requested-With") 返回的是"XMLHttpRequest"说明就是ajax请求，需要特殊处理 否则直接重定向就可以了
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                //告诉ajax我是重定向
                response.setHeader("REDIRECT", "REDIRECT");
                //告诉ajax我重定向的路径
                response.setHeader("CONTEXTPATH", "/SINOFAITH");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }else {
                response.sendRedirect("/SINOFAITH");
            }
            return false;
        }
        return true;
    }
    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception{
    }
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception{
    }
}
