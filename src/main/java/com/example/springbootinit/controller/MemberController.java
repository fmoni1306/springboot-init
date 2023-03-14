package com.example.springbootinit.controller;

import com.example.springbootinit.domain.Member;
import com.example.springbootinit.dto.MemberDTO;
import com.example.springbootinit.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MEMBER')")
    public ResponseEntity<MemberDTO> getMyMemberInfo() {
        return ResponseEntity.ok(memberService.getMyMemberById());
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<MemberDTO> getMemberInfo(@PathVariable @Valid String name) {
        return ResponseEntity.ok(memberService.getMemberById(name));
    }

    @PostMapping
    public ResponseEntity<Member> setMember(@RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok(memberService.setMember(memberDTO));
    }
}
