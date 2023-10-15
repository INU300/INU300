package com.sw300.community.member.repository;

import com.sw300.community.member.model.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberCategoryRepository  extends JpaRepository<MemberCategory,Long> {

    Optional<MemberCategory> findByMember_idAndName(Long member_id,String name);
}
