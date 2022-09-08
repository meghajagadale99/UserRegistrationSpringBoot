package com.bridgelabz.UserRegistration.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.springframework.stereotype.Component;
@Component
public class TokenGeneration {
    private static final String secret = "Megha";

    public String createToken(String emailID) {
        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            token = JWT.create().withClaim("user_email_id", emailID).sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return token;
    }

    public String decodeToken(String token) {
        String emailID;
        Verification verification = null;
        try {
            verification = JWT.require(Algorithm.HMAC256(secret));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        JWTVerifier jwtVerifier = verification.build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Claim claim = decodedJWT.getClaim("user_email_id");
        emailID = claim.asString();
        return emailID;
    }
}
