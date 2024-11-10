package com.example.lo7nim3ak.repository;

import com.example.lo7nim3ak.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId1 AND m.receiver.id = :userId2) " +
            "OR (m.sender.id = :userId2 AND m.receiver.id = :userId1) ORDER BY m.timestamp ASC")
    List<Message> findMessagesBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Query("SELECT m FROM Message m WHERE m.isRead = false AND m.sender.id = :senderId AND m.receiver.id = :receiverId")
    List<Message> findUnreadMessagesBetweenUsers(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}
