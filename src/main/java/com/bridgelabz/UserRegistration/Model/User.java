package com.bridgelabz.UserRegistration.Model;

import com.bridgelabz.UserRegistration.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RegisteredUser")
public class User {
    @Id
    @GeneratedValue
    private long id;
    private String emailID;
    private String name;
    private long phone_number;
    private int age;
    private String password;
    private boolean verify;
    private int otp;

    public User(UserDTO userDTO) {
        this.emailID = userDTO.getEmailID();
        this.name = userDTO.getName();
        this.phone_number = userDTO.getPhone_number();
        this.age = userDTO.getAge();
        this.password = userDTO.getPassword();
    }
}
