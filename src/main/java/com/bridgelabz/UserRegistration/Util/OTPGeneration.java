package com.bridgelabz.UserRegistration.Util;

@Component
public class OTPGeneration {
    public int generateOTP() {
        return (int) Math.floor(Math.random() * (9999 - 1000 + 1) + 1000);
    }
}
