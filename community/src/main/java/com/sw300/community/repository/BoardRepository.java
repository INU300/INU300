package com.sw300.community.repository;

import com.sw300.community.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>//, BoardSearch
{
}
