package com.example.springchatserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupAnnouncement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    @Lob
    @NotBlank
    private String text;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    private LocalDateTime dateOfCreation;
    private LocalDateTime dateOfLastModification;
    @ManyToOne
    private ChatGroup chatGroup;
}
