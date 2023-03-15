package com.example.springbootinit.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class CustomInterceptor implements HandlerInterceptor {

    /**
     * Controller로 보내기전 이벤트
     * false일 경우 실행 종료(Controller 진입 X)
     * Object hander는 HandlerMapping이 찾은 Controller Class 객체
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("interceptor before controller -> {}", request.getRequestURI());

        // api -> HandlerMethod 다운캐스팅
        // modelAndView -> ParameterizableViewController 다운캐스팅

        return true;
    }

    /**
     * Controller 진입 후 View가 Rendering 되기 전에 수행
     * ModelAndView modelAndView를 통해 화면 단에 들어가는 Data 등의 조작이 가능
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("interceptor before rendering -> {}", request.getRequestURI());
    }

    /**
     * Controller 진입 후 View가 정상적으로 Rendering 된 후 수행
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("interceptor after rendering -> {}", request.getRequestURI());
    }
}
