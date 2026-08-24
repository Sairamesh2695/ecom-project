package com.app.ecom.dto.user;

import com.app.ecom.config.enums.UserRole;
import com.app.ecom.dto.address.AddressDTO;
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
