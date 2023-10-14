package com.sw300.community.repository;

import com.sw300.community.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School,Long> {
    Optional<School> findBySchoolAndDepartment(String school,String department);
}
