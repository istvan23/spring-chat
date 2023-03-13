package com.example.springchatserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatGroupRole extends AbstractRole {
    @OneToOne
    private ChatGroupMembership userMembership;
    @OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE)
    private List<ChatGroupPrivilege> authorities;
}
