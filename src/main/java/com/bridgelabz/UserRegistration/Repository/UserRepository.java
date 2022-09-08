package com.bridgelabz.UserRegistration.Repository;

import com.bridgelabz.UserRegistration.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Query(value = "select * from registered_user where emailid = :emailID", nativeQuery = true)
    User getUserByEmail(String emailID);

    User findByNameContainingIgnoreCase(String name);

    User findUserByNameAndEmailID(String name, String email);
//    User findUserByNameAndVerifyTrue(String name,boolean verify);

    List<User> findUsersByVerifyTrue();
}