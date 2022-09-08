package com.bridgelabz.UserRegistration.Service;

import com.bridgelabz.UserRegistration.DTO.LoginDTO;
import com.bridgelabz.UserRegistration.DTO.ResetDTO;
import com.bridgelabz.UserRegistration.DTO.UserDTO;
import com.bridgelabz.UserRegistration.Exception.MultipleUsers;
import com.bridgelabz.UserRegistration.Exception.UserNotFound;
import com.bridgelabz.UserRegistration.Model.User;

import java.util.List;

public interface UserService {
    User register(UserDTO userDTO) throws MultipleUsers;

    String login(LoginDTO loginDTO) throws UserNotFound;

    List<User> getUsers(String auth) throws UserNotFound;

    User forgotPassword(LoginDTO loginDTO, String auth) throws UserNotFound;

    User checkIfEmailIsPresentInDatabase(String auth);

    User reset(ResetDTO resetDTO, String auth) throws UserNotFound;

    boolean verify(int otp, String mail) throws UserNotFound;

    User getUser(String emailID) throws UserNotFound;

    User getUserByName(String name) throws UserNotFound;

    User getUserByNameAndEmail(String name, String email) throws UserNotFound;

    User getUserByNameAndVerify(String name, boolean verify) throws UserNotFound;

    List<User> getVerifiedUser() throws UserNotFound;
}
