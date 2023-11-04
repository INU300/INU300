package com.sw300.community.category.repository;

import com.sw300.community.category.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.isSubclass = false ORDER BY c.dailyVisitors DESC")
    List<Category> findByDailyVisitors(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.isSubclass = false")
    List<Category> findBySubclassFalse();

    Optional<Category> findByName(String name);
}
