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
public class ChatGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotBlank
    private String name;
    @OneToMany(mappedBy = "chatGroup")
    private List<Room> rooms;
    @OneToMany(mappedBy = "chatGroup", cascade = CascadeType.REMOVE)
    private List<ChatGroupMembership> members;
    @OneToMany(mappedBy = "chatGroup")
    private List<GroupAnnouncement> groupAnnouncements;
    private LocalDateTime dateOfCreation;
    @ManyToOne
    private User groupAdministrator;

}
