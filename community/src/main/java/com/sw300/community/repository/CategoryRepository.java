package com.sw300.community.repository;

import com.sw300.community.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c ORDER BY c.dailyVisitors DESC")
    List<Category> findByDailyVisitors(Pageable pageable);
}
