package com.bridgelabz.UserRegistration.Service;

import com.bridgelabz.UserRegistration.DTO.LoginDTO;
import com.bridgelabz.UserRegistration.DTO.ResetDTO;
import com.bridgelabz.UserRegistration.DTO.UserDTO;
import com.bridgelabz.UserRegistration.Exception.MultipleUsers;
import com.bridgelabz.UserRegistration.Exception.UserNotFound;
import com.bridgelabz.UserRegistration.Model.User;
import com.bridgelabz.UserRegistration.Repository.UserRepository;
import com.bridgelabz.UserRegistration.Util.OTPGeneration;
import com.bridgelabz.UserRegistration.Util.TokenGeneration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private TokenGeneration tokenGeneration;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private OTPGeneration otpGeneration;

    @Override
    public User register(UserDTO userDTO) throws MultipleUsers {
        User user = new User(userDTO);
        if (userRepository.getUserByEmail(user.getEmailID()) != null) {
            throw new MultipleUsers("User with email id:" + user.getEmailID() + " is already registered. Please try with different email id.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        int otp = otpGeneration.generateOTP();
        emailSenderService.sendEmail(user.getEmailID(), "You have been successfully registered to User-Registration App", "Your OTP is:" + otp);
        user.setOtp(otp);
        user.setVerify(false);
        return userRepository.save(user);
    }

    @Override
    public String login(LoginDTO loginDTO) throws UserNotFound {
        User user = userRepository.getUserByEmail(loginDTO.getEmailID());
        if (userRepository.getUserByEmail(loginDTO.getEmailID()) != null) {
            if (bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                if (user.isVerify() == true) {
                    return tokenGeneration.createToken(loginDTO.getEmailID());
                }
            }
        } else {
            throw new UserNotFound("User not found!!");
        }
        return null;
    }

    @Override
    public List<User> getUsers(String auth) throws UserNotFound {
        List<User> list = userRepository.findAll();
        if (list == null) {
            throw new UserNotFound("No user were found.");
        }
        for (User u : list) {
            if (tokenGeneration.decodeToken(auth).compareToIgnoreCase(u.getEmailID()) == 0) {
                return userRepository.findAll();
            }
        }

        return null;
    }

    @Override
    public User checkIfEmailIsPresentInDatabase(String auth) {
        List<User> list = userRepository.findAll();
        for (User u : list) {
            if (tokenGeneration.decodeToken(auth).compareToIgnoreCase(u.getEmailID()) == 0 && u.isVerify() == true) {
                return userRepository.getUserByEmail(u.getEmailID());
            }
        }
        return null;
    }

    @Override
    public User reset(ResetDTO resetDTO, String auth) throws UserNotFound {
        User user = userRepository.getUserByEmail(resetDTO.getEmailID());
        System.out.println("Password matches : " + bCryptPasswordEncoder.matches(resetDTO.getOld_password(), user.getPassword()));
        System.out.println("Email Present : " + checkIfEmailIsPresentInDatabase(auth));
        if (user == null) {
            throw new UserNotFound("User with email id: " + user.getEmailID() + " not found!!");
        }
        if (bCryptPasswordEncoder.matches(resetDTO.getOld_password(), user.getPassword()) && checkIfEmailIsPresentInDatabase(auth) != null) {
            user.setPassword(bCryptPasswordEncoder.encode(resetDTO.getNew_password()));
            return userRepository.save(user);
        }

        return null;
    }

    @Override
    public boolean verify(int otp, String email) throws UserNotFound {
        User user = userRepository.getUserByEmail(email);
        log.info("User is: " + user);
        log.info("Otp: " + otp + " Stored otp is:" + user.getOtp());
        if (user == null) {
            throw new UserNotFound("User with email id: " + user.getEmailID() + " not found!!");
        }
        if (otp == user.getOtp() && user.isVerify() == false && user != null) {
            user.setVerify(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User getUser(String emailID) throws UserNotFound {
        if (userRepository.getUserByEmail(emailID) == null) {
            throw new UserNotFound("User not found!!");
        }
        return userRepository.getUserByEmail(emailID);
    }

    @Override
    public User forgotPassword(LoginDTO loginDTO, String auth) throws UserNotFound {
        User user = userRepository.getUserByEmail(loginDTO.getEmailID());
        if (checkIfEmailIsPresentInDatabase(auth) != null) {
            if (userRepository.getUserByEmail(loginDTO.getEmailID()) != null) {
                user.setPassword(bCryptPasswordEncoder.encode(loginDTO.getPassword()));
                return userRepository.save(user);
            }
        } else {
            throw new UserNotFound("User with email id: " + user.getEmailID() + " not found!!");
        }
        return null;
    }

    public User getUserByName(String name) throws UserNotFound {
        User user = userRepository.findByNameContainingIgnoreCase(name);
        if (user == null) {
            throw new UserNotFound("User with name:" + user.getName() + " not found!!");
        }
        return user;
    }

    @Override
    public User getUserByNameAndEmail(String name, String email) throws UserNotFound {
        User user = userRepository.findUserByNameAndEmailID(name, email);
        if (user == null) {
            throw new UserNotFound("User with name:" + user.getName() + " not found!!");
        }
        return user;
    }

    @Override
    public User getUserByNameAndVerify(String name, boolean verify) throws UserNotFound {
        User user = userRepository.findByNameContainingIgnoreCase(name);
        if (user == null) {
            throw new UserNotFound("User with name:" + user.getName() + " not found!!");
        }
        return user;
    }

    @Override
    public List<User> getVerifiedUser() throws UserNotFound {
        List<User> user = userRepository.findUsersByVerifyTrue();
        if (user == null) {
            throw new UserNotFound("No users found");
        }
        return user;
    }
}