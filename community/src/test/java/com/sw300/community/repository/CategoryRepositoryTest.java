package com.sw300.community.repository;

import com.sw300.community.model.Category;
import com.sw300.community.model.Member;
import com.sw300.community.model.MemberCategory;
import com.sw300.community.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberCategoryRepository memberCategoryRepository;

    @Test
    public void testInsert() {
        Category category = Category.builder()
                .name("썰게시판")
                .build();

        Category result = categoryRepository.save(category);
        System.out.println(result.getCno());
    }

    @Test
    public void testInsert2() {
        Category category = Category.builder()
                .name("썰게시판")
                .build();

        Category result = categoryRepository.save(category);

        Member member = Member.builder()
                .name("이혜성")
                .school("린천대")
                .email("hen715@naver.com")
                .subclass("몰라")
                .password("123")
                .nickname("혜")
                .build();

        memberRepository.save(member);

        memberService.viewAddCount(1L,"hen715@naver.com");

        MemberCategory memberCategory = memberCategoryRepository.findById(1L).orElseThrow();
        Assertions.assertThat(memberCategory.getViewCount()==1);

        memberService.viewAddCount(1L,"hen715@naver.com");
        Assertions.assertThat(memberCategory.getViewCount()==2);


    }

}