package com.example.springchatserver.service.room;

import com.example.springchatserver.domain.Room;
import com.example.springchatserver.domain.RoomMessage;
import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.RoomMessageDto;
import com.example.springchatserver.mapper.RoomMessageMapper;
import com.example.springchatserver.repository.RoomMessageRepository;
import com.example.springchatserver.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class RoomMessageServiceImpl implements RoomMessageService {
    private final RoomMessageRepository roomMessageRepository;
    private final RoomRepository roomRepository;
    private final RoomMessageMapper roomMessageMapper;
    @Override
    public RoomMessageDto getRoomMessageById(Long id) {
        return this.roomMessageMapper.convertToDto(this.roomMessageRepository.findById(id).get());
    }

    @Override
    public List<RoomMessageDto> getAllMessageByRoomId(Long roomId) {
        return StreamSupport.stream(this.roomMessageRepository.findAll().spliterator(), false)
                .map(message -> this.roomMessageMapper.convertToDto(message)).collect(Collectors.toList());
    }

    @Override
    public List<RoomMessageDto> getMessagesByRoomIdAndTimeInterval(Long roomId, LocalDateTime from, LocalDateTime to) {
        List<RoomMessageDto> roomMessages = StreamSupport
                .stream(this.roomRepository.findById(roomId).get().getRoomMessages().stream().filter(message -> {
            if (message.getDateOfMessage().isAfter(from) && message.getDateOfMessage().isBefore(to)){
                return true;
            }
            return false;
        }).spliterator(), false).map(roomMessage -> this.roomMessageMapper.convertToDto(roomMessage)).collect(Collectors.toList());
        return roomMessages;
    }

    @Override
    public RoomMessageDto saveNewMessage(User sender, RoomMessageDto message) {
        RoomMessage roomMessage = new RoomMessage();
        Room room = this.roomRepository.findById(message.getRoomId()).get();
        roomMessage.setSender(sender);
        roomMessage.setRoom(room);
        roomMessage.setMessage(message.getMessage());
        roomMessage.setDateOfMessage(LocalDateTime.now());
        room.getRoomMessages().add(roomMessage);

        roomMessage = this.roomMessageRepository.save(roomMessage);
        return this.roomMessageMapper.convertToDto(roomMessage);
    }

    @Override
    public RoomMessageDto updateMessage(Long messageId, RoomMessageDto message) {
        RoomMessage roomMessage = this.roomMessageRepository.findById(messageId).get();
        roomMessage.setMessage(message.getMessage());
        roomMessage = this.roomMessageRepository.save(roomMessage);
        return this.roomMessageMapper.convertToDto(roomMessage);
    }

    @Override
    public void deleteMessage(Long messageId) {
        this.roomMessageRepository.deleteById(messageId);
    }
}
