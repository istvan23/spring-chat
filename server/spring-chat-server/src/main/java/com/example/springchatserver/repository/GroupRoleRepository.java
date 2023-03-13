package com.example.springchatserver.repository;

import com.example.springchatserver.domain.ChatGroupRole;
import org.springframework.data.repository.CrudRepository;

public interface GroupRoleRepository extends CrudRepository<ChatGroupRole, Long> {
}
