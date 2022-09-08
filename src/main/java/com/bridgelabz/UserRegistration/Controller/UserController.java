package com.bridgelabz.UserRegistration.Controller;

import com.bridgelabz.UserRegistration.DTO.*;
import com.bridgelabz.UserRegistration.Exception.MultipleUsers;
import com.bridgelabz.UserRegistration.Exception.UserNotFound;
import com.bridgelabz.UserRegistration.Model.User;
import com.bridgelabz.UserRegistration.Service.EmailSenderService;
import com.bridgelabz.UserRegistration.Service.UserService;
import com.bridgelabz.UserRegistration.Util.TokenGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenGeneration tokenGeneration;

    @GetMapping("/welcome")
    private String welcome() {
        return "Welcome";
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody UserDTO userDTO) throws MultipleUsers {
        User user = userService.register(userDTO);
        ResponseDTO responseDTO = new ResponseDTO("User registered successfully", userDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginDTO loginDTO) throws UserNotFound {
        String user = userService.login(loginDTO);
        ResponseDTO responseDTO = new ResponseDTO("Login successful!!", user);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/all-users")
    public ResponseEntity<ResponseDTO> getUsers(@RequestHeader(value = "Authorization") String auth) throws UserNotFound {
        List<User> list = userService.getUsers(auth);
        ResponseDTO responseDTO = new ResponseDTO("Authorization successful!!", list);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestHeader(value = "Authorization") String auth, @RequestBody LoginDTO loginDTO) throws UserNotFound {
        User user = userService.forgotPassword(loginDTO, auth);
        ResponseDTO responseDTO = new ResponseDTO("Password changed successful!!", user);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestHeader(value = "Authorization") String auth, @RequestBody ResetDTO resetDTO) throws UserNotFound {
        ResponseDTO responseDTO = new ResponseDTO("Password reset successful!!", userService.reset(resetDTO, auth));
        return ResponseEntity.ok().body(responseDTO);
    }

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/verify-user")
    public ResponseEntity<ResponseDTO> verifyUser(@RequestBody OtpVerificationDTO otpDTO) throws UserNotFound {
        ResponseDTO responseDTO = new ResponseDTO(messageSource.getMessage("user.verified", null, Locale.ENGLISH), userService.verify(otpDTO.getOtp(), otpDTO.getEmailID()));
        return ResponseEntity.ok().body(responseDTO);
    }

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/send-mail")
    public void sendMail(@RequestBody EmailDTO emailDTO) {
        emailSenderService.sendEmail(emailDTO.getToEmail(), emailDTO.getSubject(), emailDTO.getBody());
    }

    @GetMapping("/get-user/{emailID}")
    public User getUser(@PathVariable String emailID) throws UserNotFound {
        User user = userService.getUser(emailID);
        return user;
    }

    @GetMapping("/get-user-by-name/{name}")
    public ResponseEntity<ResponseDTO> getUserByNameController(@PathVariable String name) throws UserNotFound {
        User user = userService.getUserByName(name);
        ResponseDTO responseDTO = new ResponseDTO("User with name :" + name, user);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/get-user-by-name-email/")
    public ResponseEntity<ResponseDTO> getUserNameAndEmail(@RequestParam String name, @RequestParam String email) throws UserNotFound {
        User user = userService.getUserByNameAndEmail(name, email);
        ResponseDTO responseDTO = new ResponseDTO("User with name :" + name, user);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/get-user-by-name-verify/")
    public ResponseEntity<ResponseDTO> getVerifiedUser() throws UserNotFound {
        List<User> user = userService.getVerifiedUser();
        ResponseDTO responseDTO = new ResponseDTO("Verified users :", user);
        return ResponseEntity.ok().body(responseDTO);
    }
}