package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Message;


public interface MessageRepository extends JpaRepository<Message, Integer> {

    // @Modifying
    // @Query("DELETE FROM Message m WHERE m.id := id")
    // int deleteByIdRowsAffected(@Param("id") Integer id);
}
