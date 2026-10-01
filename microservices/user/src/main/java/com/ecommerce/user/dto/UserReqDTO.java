package com.ecommerce.user.dto;

import lombok.Data;

@Data
public class UserReqDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressDTO address;
}
