package com.sw300.community.board.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JWTUtils {

    private static final String KEY = "haeun";

    public static String getIssuer(String token) {
        String issuer = JWT.require(Algorithm.HMAC512(KEY))
                .build()
                .verify(token)
                .getIssuer();

        return issuer;
    }

}
