package com.bridgelabz.UserRegistration.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String emailID;
    private String name;
    private long phone_number;
    private int age;
    private String password;
}