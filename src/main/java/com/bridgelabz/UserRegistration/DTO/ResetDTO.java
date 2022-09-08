package com.bridgelabz.UserRegistration.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetDTO {
    private String emailID;
    private String old_password;
    private String new_password;
}