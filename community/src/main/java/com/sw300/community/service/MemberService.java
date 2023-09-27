package com.sw300.community.service;


import com.sw300.community.dto.MemberSaveDto;
import com.sw300.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;

    @Transactional
    public Long joinMember(MemberSaveDto memberSaveDto){
        /* 소분류 저장 로직*/
        return memberRepository.save(memberSaveDto.toEntity()).getId();
    }

}
