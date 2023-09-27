package com.sw300.community.service;


import com.sw300.community.dto.MemberSaveDto;
import com.sw300.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long joinMember(MemberSaveDto memberSaveDto){
        /* 소분류 저장 로직*/
        memberSaveDto.setPassword(passwordEncoder.encode(memberSaveDto.getPassword()));
        return memberRepository.save(memberSaveDto.toEntity()).getId();
    }

}
