package com.example.springchatserver.repository;

import com.example.springchatserver.domain.RoomMessage;
import org.springframework.data.repository.CrudRepository;

public interface RoomMessageRepository extends CrudRepository<RoomMessage, Long> {
}
