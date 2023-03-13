package com.example.springchatserver.repository;

import com.example.springchatserver.domain.ChatGroupPrivilege;
import org.springframework.data.repository.CrudRepository;

public interface GroupPrivilegeRepository extends CrudRepository<ChatGroupPrivilege, Long> {
}
