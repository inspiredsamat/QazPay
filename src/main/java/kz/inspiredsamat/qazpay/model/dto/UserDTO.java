package kz.inspiredsamat.qazpay.model.dto;

import kz.inspiredsamat.qazpay.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Role role;
}
