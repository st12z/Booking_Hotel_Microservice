package com.thuc.users.dto.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {
    private Integer id;
    private String name;
}
