package com.example.springbootinit.jwt;


import com.example.springbootinit.domain.Authority;
import com.example.springbootinit.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidityInSeconds;

    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInSeconds = tokenValidityInSeconds * 100;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Bean이 생성되고 주입 받은 후 secret값을 Base64 Decode 해서 key 변수에 할당
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * 토큰 생성
     *
     * @return jwt secret, key 를 사용해서 생성한 jwt 토큰
     */
    public String createToken(Authentication authentication) {
        String authority = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());

        long now = new Date().getTime();

        Date validity = new Date(now + this.tokenValidityInSeconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authority)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 Authentication 객체 리턴
     *
     * @param token Jwt
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 한개의 권한이더라도 List로 변환후 파라미터로 전달
        List<SimpleGrantedAuthority> authorities = Arrays.asList(claims.get(AUTHORITIES_KEY).toString()).stream().map(SimpleGrantedAuthority::new).toList();

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰을 파싱해 발생하는 익셉션 처리
     */
    public boolean validToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT");
        } catch (IllegalArgumentException e) {
            log.error("JWT 가 잘못되었습니다.");
        }
        return false;
    }
}
