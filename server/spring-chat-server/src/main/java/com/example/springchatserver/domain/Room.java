package com.example.springchatserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String roomName;
    @ManyToOne
    private ChatGroup chatGroup;
    @OneToMany(mappedBy = "room")
    private List<RoomMessage> roomMessages;
    private LocalDateTime dateOfCreation;
    private LocalDateTime dateOfLastMessage;
}
