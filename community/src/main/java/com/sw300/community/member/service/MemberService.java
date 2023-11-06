package com.sw300.community.member.service;


import com.sw300.community.member.dto.MemberCategoryDto;
import com.sw300.community.member.dto.MemberGoodDto;
import com.sw300.community.member.dto.MemberSaveDto;
import com.sw300.community.category.model.Category;
import com.sw300.community.member.model.Member;
import com.sw300.community.member.model.MemberCategory;
import com.sw300.community.model.School;
import com.sw300.community.category.repository.CategoryRepository;
import com.sw300.community.member.repository.MemberCategoryRepository;
import com.sw300.community.member.repository.MemberRepository;
import com.sw300.community.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final MemberCategoryRepository memberCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SchoolRepository schoolRepository;

    @Transactional
    public Long joinMember(MemberSaveDto memberSaveDto){
        Optional<School> school = schoolRepository.findBySchoolAndDepartment(memberSaveDto.getSchool(),memberSaveDto.getDepartment());
        memberSaveDto.setSubclass(school.get().getSubclass());
        memberSaveDto.setPassword(passwordEncoder.encode(memberSaveDto.getPassword()));
        Member newMem = memberRepository.save(memberSaveDto.toEntity());
        //임의로 자주가는 게시판설정
        for(int i =0 ; i<memberSaveDto.getCategories().size();i++) {
            String name= memberSaveDto.getCategories().get(i)+"게시판";
            viewAddCount(categoryRepository.findByName(name).get().getCno(),newMem.getEmail());
        }

        return newMem.getId();
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
            MemberCategory mem = memberCategoryRepository.save(MemberCategory.builder().member(member).name(category.getName()).build());
        }
    }

    @Transactional
    public List<MemberCategoryDto> getFavorite(String email){
        Optional<Member> member = memberRepository.findByEmail(email);
        List<MemberCategory> favorite = member.get().getFavorite();
        List<MemberCategoryDto> favoriteDto = favorite.stream()
                .map(category -> MemberCategoryDto.builder()
                        .name(category.getName())
                        .viewCount(category.getViewCount())
                        .build())
                .collect(Collectors.toList());

        //자주가는 게시판을 viewCount에 따라내림차순으로 정렬
        List<MemberCategoryDto> sortedFavoriteDto = favoriteDto.stream()
                .sorted(Comparator.comparingInt(MemberCategoryDto::getViewCount).reversed())
                .collect(Collectors.toList());
        return sortedFavoriteDto;

    }

    @Transactional
    public List<MemberGoodDto> getGoodDto(){
        List<Member> members = memberRepository.findAll(Sort.by(Sort.Direction.DESC,"upVotes"));

        List<MemberGoodDto> goodDto = members.stream().map(member -> MemberGoodDto.builder()
                .nickname(member.getNickname())
                .upVotes(member.getUpVotes())
                .build()).collect(Collectors.toList());
        return goodDto;
    }

    @Transactional
    public List<String> getSchool(){
        List<School> schools = schoolRepository.findAll();

        // 중복된 학교 이름을 제거하기 위해 Set을 사용
        Set<String> uniqueSchools = new HashSet<>();

        // 중복 제거된 학교 이름을 Set에 추가
        for (School school : schools) {
            uniqueSchools.add(school.getSchool());
        }

        // Set을 List로 변환
        List<String> schoolDto = new ArrayList<>(uniqueSchools);

        Collections.sort(schoolDto);
        return schoolDto;
    }

    @Transactional
    public List<String>getDepartment(String school){
        List<School> department= schoolRepository.findAllBySchool(school);
        List<String> departmentNames = new ArrayList<>();

        // 학과 엔티티에서 학과 이름을 추출하여 리스트에 추가
        for (School departments : department) {
            departmentNames.add(departments.getDepartment());
        }

        Collections.sort(departmentNames);
        return departmentNames;
    }

}
