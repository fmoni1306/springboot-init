package com.example.springbootinit.config.security;

import com.example.springbootinit.jwt.JwtAccessDeniedHandler;
import com.example.springbootinit.jwt.JwtAuthenticationEntryPoint;
import com.example.springbootinit.jwt.JwtFilter;
import com.example.springbootinit.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


/**
spring security 5.7.x 에서는 WebSecurityConfigurerAdapter 상속 받지 않고 사용
웹 서비스가 로드 될때 Spring Container에 의해 관리 되는 클래스 사용자의 인증, 인가에 대한 설정

인증(Authentication): 사용자가 시스템에 정보(ex jwt토큰, 유저 정보)를 제공하며, 시스템은 사용자에 대한 정보를 검증, 시스텡을 사용할 수 있는 지에 대한 확인 하는 과정
인가(Authorization): 보호된 자원(메서드 접근 혹은 요청에 대한 자원(ex 정적파일))에 대해서 접근을 허가 하거나 거부하는 기능
 */
@EnableWebSecurity(debug = false) // debug를 통해 security filter 확인 가능
@EnableMethodSecurity // preauthorize 어노테이션 사용하기위해
@Configuration
@Slf4j
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public static final String[] WHITELIST = {"/favicon.ico", "/error", "/", "/login"};

    public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }


    /**
     * webSecurity를 통해 설정한 endpoints는 Spring Security Filter Chain을 거치지 않고 인증, 인가 관련(jwt) 필터를 타지 않음.
     * please use permitAll via HttpSecurity#authorizeHttpRequests instead. spring에서 권장하지 않는 방법
     * @return WebSecurityCustomizer
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

        // ID, Password 문자열을 Base64로 인코딩하여 전달하는 구조
        http.httpBasic().disable();

        // csrf 설정 disable 쿠키 기반이 아닌 JWT 기반이기 떄문에 미사용
        http.csrf().disable();

        http.cors().configurationSource(corsConfigurationSource());

        // session을 사용하지 않기 떄문에 세션설정을 STATELESS( Spring Security will never create an HttpSession and it will never use it to obtain the SecurityContext )
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // jwt 인증 방식을 사용 하기 떄문에 form 로그인방식 disabled
        http.formLogin().disable();

        // Exception 핸들링 추가
        http.exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);

        http.authorizeHttpRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(WHITELIST).permitAll()
                .anyRequest().authenticated();

        /*
         * custom 필터 추가
         * custom filter 를 component 로 등록 시 전역적으로 filter에 등록 됨
         * 전역으로 사용할거면 bean으로 등록 security에서 사용할거면 addfilter
         */
//        http.addFilterBefore(new CustomFilter(), UsernamePasswordAuthenticationFilter.class);

        // JwtFilter를 Security 로직 필터에 등록
        http.addFilterBefore(new JwtFilter(tokenProvider, WHITELIST), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
