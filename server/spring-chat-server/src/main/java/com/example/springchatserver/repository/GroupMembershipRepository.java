package com.example.springchatserver.repository;

import com.example.springchatserver.domain.ChatGroupMembership;
import org.springframework.data.repository.CrudRepository;

public interface GroupMembershipRepository extends CrudRepository<ChatGroupMembership, Long> {
}
