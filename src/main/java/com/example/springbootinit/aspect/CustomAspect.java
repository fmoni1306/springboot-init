package com.example.springbootinit.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class CustomAspect {

    /**
     * pointcut  execution 표현식 https://wpunch2000.tistory.com/22
     */
    @Pointcut("execution(public * com.example.springbootinit.controller..*(..))")
    private void publicTarget() {}

    @Around("publicTarget()")
    public Object controllerAdvice(ProceedingJoinPoint pjp) throws Throwable {
        log.info("성능 측정을 시작합니다.");
        StopWatch sw = new StopWatch();
        sw.start();

        // 비즈니스 로직 (메인 로직)
        Object result = pjp.proceed();

        sw.stop();
        log.info("성능측정 종료: {} ms", sw.getLastTaskTimeMillis());
        return result;
    }

    @Before("publicTarget()")
    public void beforeControllerAdvice() {
        log.info("aspect before controller");
    }

    @After("publicTarget()")
    public void afterControllerAdvice() {
        log.info("aspect after controller");
    }

    @AfterReturning(value = "publicTarget()", returning = "returnValue")
    public void returnControllerAdvice(Object returnValue) {
        log.info("aspect return 이후 적용 ->  {}", returnValue);
    }

    @AfterThrowing(value = "publicTarget()", throwing = "exception")
    public void throwControllerAdvice(Exception exception) {
        log.info("aspect controller 예외 발생 -> {}", exception.getMessage());
    }

}
