package com.example.springchatserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class AppPrivilege extends AbstractPrivilege{
    @ManyToOne
    private Role role;
}
