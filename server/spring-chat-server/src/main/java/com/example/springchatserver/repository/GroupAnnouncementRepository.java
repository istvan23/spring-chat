package com.example.springchatserver.repository;

import com.example.springchatserver.domain.GroupAnnouncement;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupAnnouncementRepository extends CrudRepository<GroupAnnouncement, Long> {
}
