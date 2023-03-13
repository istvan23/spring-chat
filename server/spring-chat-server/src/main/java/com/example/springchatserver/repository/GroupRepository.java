package com.example.springchatserver.repository;

import com.example.springchatserver.domain.ChatGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<ChatGroup, Long> {
    Iterable<ChatGroup> findByNameContains(String name);
}
