package com.example.springchatserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatGroupMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private ChatGroup chatGroup;
    @OneToOne(mappedBy = "userMembership", cascade = CascadeType.REMOVE)
    private ChatGroupRole role;
    private LocalDateTime dateOfJoin;
}
