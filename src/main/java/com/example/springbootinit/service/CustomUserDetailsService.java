package com.example.springbootinit.service;

import com.example.springbootinit.domain.Member;
import com.example.springbootinit.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Autowired
    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 로그인시에 DB에서 가져온 유저정보와 권한정보를 기반으로 userdetails.User 객체를 생성해서 리턴
     * @param  name the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return memberRepository.findByName(name)
                .map(member -> createMember(name, member))
                .orElseThrow(() -> new UsernameNotFoundException(name + "-> 데이터베이스에서 찾을 수 없습니다."));
    }

    private User createMember(String name, Member member) {
        // 예시로 넣은 부분
        if (member.getIsActivated() == false) {
            throw new RuntimeException(name + "-> 활성화되어 있지 않습니다.");
        }

        List<SimpleGrantedAuthority> grantedAuthorities = Arrays.asList(new SimpleGrantedAuthority(member.getAuthority().getAuthorityName()));

        return new User(member.getName(), member.getPassword(), grantedAuthorities);
    }
}
