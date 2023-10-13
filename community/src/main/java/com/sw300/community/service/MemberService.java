package com.sw300.community.service;


import com.sw300.community.dto.MemberSaveDto;
import com.sw300.community.model.Category;
import com.sw300.community.model.Member;
import com.sw300.community.model.MemberCategory;
import com.sw300.community.repository.CategoryRepository;
import com.sw300.community.repository.MemberCategoryRepository;
import com.sw300.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final MemberCategoryRepository memberCategoryRepository;
    private final CategoryRepository categoryRepository;

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

    @Transactional
    public boolean searchPassword(String email, String name){
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        if(!member.getName().equals(name)){
            new IllegalArgumentException("회원의 이름이 다릅니다.");
            return false;
        }
        else
            return true;

    }

    @Transactional
    public Long newPassword(String email,String newPassword){
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        member.changePassword(passwordEncoder.encode(newPassword));
        return member.getId();
    }

    @Transactional
    public void viewAddCount(Long cNo, String email){
        Category category = categoryRepository.findById(cNo).orElseThrow();
        Member member = memberRepository.findByEmail(email).orElseThrow();

        List<MemberCategory> favorite = member.getFavorite();


        boolean hasCategory = favorite.stream()
                .map(MemberCategory::getName)
                .anyMatch(name -> name!=null&&name.equals(category.getName()));

        if(hasCategory){
            MemberCategory cat = memberCategoryRepository.findByMember_idAndName(member.getId(),category.getName()).orElseThrow();
            cat.addViewCount();
        }
        else{
            memberCategoryRepository.save(MemberCategory.builder().member(member).name(category.getName()).build());
        }



    }

}
