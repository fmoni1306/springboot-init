package com.example.springbootinit.service;

import com.example.springbootinit.domain.Authority;
import com.example.springbootinit.domain.Member;
import com.example.springbootinit.dto.MemberDTO;
import com.example.springbootinit.repository.MemberRepository;
import com.example.springbootinit.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberDTO getMyMemberById() {

        Member member = SecurityUtil.getCurrentUsername().flatMap(memberRepository::findByName).orElseThrow();

        return MemberDTO.builder()
                .name(member.getName())
                .authority(member.getAuthority().getAuthorityName())
                .build();
    }

    public MemberDTO getMemberById(String name) {

        Member member = memberRepository.findByName(name).get();

        return MemberDTO.builder()
                .name(member.getName())
                .authority(member.getAuthority().getAuthorityName())
                .build();
    }

    public Member setMember(MemberDTO memberDTO) {
        log.info("회원가입");

        if (memberRepository.findByName(memberDTO.getName()).orElse(null) != null) {
            throw new RuntimeException("이미 존재하는 유저");
        }

        Authority authority = new Authority("MEMBER");


        Member member = Member.builder()
                .name(memberDTO.getName())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .authority(authority)
                .isActivated(true)
                .build();

        return memberRepository.save(member);
    }
}
