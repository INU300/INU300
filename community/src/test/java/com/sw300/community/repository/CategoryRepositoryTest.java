package com.sw300.community.repository;

import com.sw300.community.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInsert() {
        Category category = Category.builder()
                .name("썰게시판")
                .build();

        Category result = categoryRepository.save(category);
        System.out.println(result.getCno());
    }

}