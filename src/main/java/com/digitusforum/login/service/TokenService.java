package com.digitusforum.login.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import model.M;
import service.EnvironmentService;
import service.ThrowService;

import org.apache.commons.lang3.StringUtils;
import vo.UserVO;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

public class TokenService {

    //Sample method to construct a JWT
    public static String createJWTToken(ZonedDateTime expiration, UserVO userVO) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(EnvironmentService.JWT_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        //Builds the JWT and serializes it to a compact, URL-safe string
        return Jwts.builder()
                .setSubject(userVO.getUserId())
                .setIssuer("digitusForum")
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(expiration.toInstant()))
                .claim("provider", "provider")
                .claim("email", userVO.getEmail())
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }

    public UserVO validateToken(String locale, Optional<String> authorization) {
        String token = null;
        /*if (!authorization.isPresent() || StringUtils.isBlank(authorization.get())) {
            throw TokenException.MISSING_HEADER_AUTHORIZATION;
        }*/
        Jws<Claims> jwtToken = null;
        try {
            String[] tokenData = authorization.get().split(" ");
            String tokenType = tokenData[0];
            token = tokenData[1];
            if (StringUtils.isBlank(token)) {
                throw ThrowService.doIt(locale, 403, M.LOGIN_INVALID_TOKEN);
            }
            jwtToken = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(EnvironmentService.JWT_KEY))
                    .parseClaimsJws(token);

        } catch (Exception e) {
            throw ThrowService.doIt(locale, 403, M.LOGIN_INVALID_TOKEN);
        }
        if (jwtToken.getBody().getExpiration().before(Date.from(ZonedDateTime.now().toInstant()))) {
            throw ThrowService.doIt(locale, 403, M.LOGIN_EXPIRED_TOKEN);
        }
        UserVO user = new UserVO();
        user.setUserId(jwtToken.getBody().getSubject());
        if (jwtToken.getBody().get("email") != null)
            user.setEmail(jwtToken.getBody().get("email").toString());
        return user;
    }
}
