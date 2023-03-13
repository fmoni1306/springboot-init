package com.example.springbootinit.config;

import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.*;


/**
spring security 5.7.x 에서는 WebSecurityConfigurerAdapter 상속 받지 않고 사용
웹 서비스가 로드 될때 Spring Container에 의해 관리 되는 클래스 사용자의 인증, 인가에 대한 설정

인증(Authentication): 사용자가 시스템에 정보(ex jwt토큰, 유저 정보)를 제공하며, 시스템은 사용자에 대한 정보를 검증, 시스텡을 사용할 수 있는 지에 대한 확인 하는 과정
인가(Authorization): 보호된 자원(메서드 접근 혹은 요청에 대한 자원(ex 정적파일))에 대해서 접근을 허가 하거나 거부하는 기능
 */
@EnableWebSecurity
@Configuration
@Slf4j
public class WebSecurityConfig {


    /**
    정적 리소스에 대해 인증된 사용자가 정적 자원의 접근 인가 설정
    @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        // 정적 자원은 Security ignore
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    /**
     *  HTTP에 대한 인증과 인가를 담당하는 메서드 필터를 통해 인증 방식과 인증 절차에 대해 등록 및 설정을 담당하는 메서드
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {

        // csrf 설정 disable
        http.csrf().disable();

        // cors 설정 disabled
        http.cors().disable();

        // jwt 인증 방식을 사용 하기 떄문에 form 로그인방식 disabled
        http.formLogin().disable();

        // Exception 핸들링 추가

        // session을 사용하지 않기 떄문에 세션설정을 STATELESS( Spring Security will never create an HttpSession and it will never use it to obtain the SecurityContext )
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // jwt 토큰 인경우 모든 요청에 대해 허용
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());

        // jwt 토큰 방식이 아닌경우
//        http.authorizeHttpRequests(authorize -> authorize
//                // Controller 에서 파일명을 리턴해서 페이지 이동을 하는 경우 추가
//                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//                .requestMatchers("/user/**").permitAll()
//                );

        // HTTP Basic Authentication을 사용
        http.httpBasic(withDefaults());

        return http.build();
    }
}
