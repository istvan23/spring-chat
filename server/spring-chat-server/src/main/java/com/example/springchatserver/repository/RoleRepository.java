package com.example.springchatserver.repository;

import com.example.springchatserver.domain.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
