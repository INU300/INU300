package com.sw300.community.repository;

import com.sw300.community.dto.MemberCategoryDto;
import com.sw300.community.model.Category;
import com.sw300.community.model.Member;
import com.sw300.community.model.MemberCategory;
import com.sw300.community.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
    @Transactional
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
                .name("1게시판")
                .build();

        Category category1 = Category.builder()
                .name("2게시판")
                .build();

        Category category2 = Category.builder()
                .name("3게시판")
                .build();



        Category result = categoryRepository.save(category);
        Category result1 = categoryRepository.save(category1);
        Category result2= categoryRepository.save(category2);

        System.out.println(result.getCno());
        System.out.println(result1.getCno());
        System.out.println(result2.getCno());

        Member member = Member.builder()
                .name("이혜성")
                .school("인천대학교")
                .email("aaa@naver.com")
                .department("컴퓨터공학부")
                .password("123")
                .nickname("혜")
                .build();

        memberRepository.save(member);
        memberService.viewAddCount(result.getCno(),"aaa@naver.com");
        memberService.viewAddCount(result1.getCno(),"aaa@naver.com");
        memberService.viewAddCount(result1.getCno(),"aaa@naver.com");
        memberService.viewAddCount(result2.getCno(),"aaa@naver.com");
        memberService.viewAddCount(result2.getCno(),"aaa@naver.com");
        memberService.viewAddCount(result2.getCno(),"aaa@naver.com");

        MemberCategory memberCategory = memberCategoryRepository.findById(1L).orElseThrow();
        MemberCategory memberCategory1 = memberCategoryRepository.findById(2L).orElseThrow();
        MemberCategory memberCategory2 = memberCategoryRepository.findById(3L).orElseThrow();
        Assertions.assertThat(memberCategory.getViewCount()==1);
        Assertions.assertThat(memberCategory1.getViewCount()==2);
        Assertions.assertThat(memberCategory2.getViewCount()==3);

        List<MemberCategoryDto> memberCategoryDto = memberService.getFavorite("aaa@naver.com");
        System.out.println(memberCategoryDto.get(0).getName());
        System.out.println(memberCategoryDto.get(1).getName());
        System.out.println(memberCategoryDto.get(2).getName());
    }

    @Test
    @Transactional
    public void subclassTest(){
        Member member = Member.builder()
                .name("이혜성")
                .school("인천대학교")
                .email("hen71@naver.com")
                .department("컴퓨터공학부")
                .password("123")
                .nickname("혜")
                .build();

        memberRepository.save(member);

        Optional<Member> mem = memberRepository.findById(1L);
        if(mem.isPresent())
            Assertions.assertThat(mem.get().getSubclass().equals("전산학ㆍ컴퓨터공학"));
    }

    @Test
    public void hallTest(){
        Member member1 = Member.builder()
                .name("이혜성")
                .school("인천대학교")
                .email("aaaaa@naver.com")
                .department("컴퓨터공학부")
                .password("123")
                .nickname("혜")
                .build();
        member1.setUpVotes(10);
        memberRepository.save(member1);

        Member member2 = Member.builder()
                .name("이혜성")
                .school("인천대학교")
                .email("bbbbb@naver.com")
                .department("컴퓨터공학부")
                .password("123")
                .nickname("혜")
                .build();

        member2.setUpVotes(50);
        memberRepository.save(member2);

        Member member3 = Member.builder()
                .name("이혜성")
                .school("인천대학교")
                .email("ccccc@naver.com")
                .department("컴퓨터공학부")
                .password("123")
                .nickname("혜")
                .build();
        member3.setUpVotes(15);
        memberRepository.save(member3);
    }

    @Test
    public void insertCategory(){
        Category category = Category.builder()
                .name("1게시판")
                .build();

        Category category1 = Category.builder()
                .name("2게시판")
                .build();

        Category category2 = Category.builder()
                .name("3게시판")
                .build();
        Category category3 = Category.builder()
                .name("4게시판")
                .build();

        Category category4 = Category.builder()
                .name("5게시판")
                .build();

        Category category5 = Category.builder()
                .name("6게시판")
                .build();
        Category category6 = Category.builder()
                .name("7게시판")
                .build();

        Category category7 = Category.builder()
                .name("8게시판")
                .build();

        Category category8 = Category.builder()
                .name("9게시판")
                .build();

        Category category9 = Category.builder()
                .name("10게시판")
                .build();


        categoryRepository.save(category);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);
        categoryRepository.save(category5);
        categoryRepository.save(category6);
        categoryRepository.save(category7);
        categoryRepository.save(category8);
        categoryRepository.save(category9);
    }

}