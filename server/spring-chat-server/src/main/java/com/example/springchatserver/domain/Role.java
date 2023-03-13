package com.example.springchatserver.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AbstractRole {
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "role")
    private List<AppPrivilege> authorities;
}
