package com.sw300.community.service;


import com.sw300.community.dto.MemberSaveDto;
import com.sw300.community.model.Member;
import com.sw300.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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

    @Transactional
    public Member getMember(String email){
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent()){
            return member.get();
        }
        else{
            throw new IllegalStateException("사용자가 없습니다.");
        }
    }

    @Transactional
    public Long changeNickname(Long id,String newNickname){
         Member mem = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
         mem.changeNickname(newNickname);
        return mem.getId();
    }

    @Transactional
    public Long changePassword(Long id,String currentPassword,String newPassword){
        Member mem = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        if(!passwordEncoder.matches(currentPassword,mem.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        else{
            mem.changePassword(passwordEncoder.encode(newPassword));
        }
        return mem.getId();
    }

}
