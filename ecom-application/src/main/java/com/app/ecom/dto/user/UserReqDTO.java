package com.app.ecom.dto.user;

import com.app.ecom.dto.address.AddressDTO;
import lombok.Data;

@Data
public class UserReqDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressDTO address;
}
