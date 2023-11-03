package com.sw300.community.message.repository;

import com.sw300.community.member.model.Member;
import com.sw300.community.message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByReceiver(Member member);
    List<Message> findAllBySender(Member member);
}
