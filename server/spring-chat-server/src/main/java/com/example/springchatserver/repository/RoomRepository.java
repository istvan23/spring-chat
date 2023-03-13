package com.example.springchatserver.repository;

import com.example.springchatserver.domain.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Long> {
}
