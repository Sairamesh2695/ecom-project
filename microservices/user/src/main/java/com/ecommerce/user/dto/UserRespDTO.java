package com.ecommerce.user.dto;

import com.ecommerce.user.enums.UserRole;
import lombok.Data;

@Data
public class UserRespDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO address;
}
