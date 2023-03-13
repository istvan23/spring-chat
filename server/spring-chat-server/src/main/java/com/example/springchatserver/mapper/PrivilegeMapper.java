package com.example.springchatserver.mapper;

import com.example.springchatserver.domain.AbstractPrivilege;
import com.example.springchatserver.dto.PrivilegeDto;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface PrivilegeMapper {
    PrivilegeDto convertToDto(AbstractPrivilege privilege);
}
