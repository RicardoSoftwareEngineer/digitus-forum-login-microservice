package com.digitusforum.login.service;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import model.M;
import service.EnvironmentService;
import service.ThrowService;
import vo.TokenVO;
import vo.UserVO;

public class TokenService {

	public String createJWTToken(ZonedDateTime expiration, UserVO userVO) {
		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(EnvironmentService.JWT_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// Let's set the JWT Claims
		// Builds the JWT and serializes it to a compact, URL-safe string
		return Jwts.builder().setSubject(String.valueOf(userVO.getUserId()))
				.setIssuer("digitus forum login microservice").setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
				.setExpiration(Date.from(expiration.toInstant())).claim("provider", "provider")
				.claim("email", userVO.getEmail()).claim("name", userVO.getName())
				.signWith(signatureAlgorithm, signingKey).compact();
	}

	public UserVO validateToken(String locale, UserVO userVO) {
		String token = null;
		Jws<Claims> jwtToken = null;
		try {
			String[] tokenData = userVO.getToken().split(" ");
			if(tokenData.length != 2)
				throw ThrowService.doIt(locale, 400, M.LOGIN_INVALID_TOKEN);
			String tokenType = tokenData[0];
			token = tokenData[1];
			jwtToken = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(EnvironmentService.JWT_KEY))
					.parseClaimsJws(token);
		} catch (MalformedJwtException e) {
			throw ThrowService.doIt(locale, 400, M.LOGIN_INVALID_TOKEN);
		} catch (ExpiredJwtException e) {
			throw ThrowService.doIt(locale, 403, M.LOGIN_EXPIRED_TOKEN);
		} catch (SignatureException e) {
			throw ThrowService.doIt(locale, 400, M.LOGIN_INVALID_TOKEN);
		} catch (ResponseStatusException e) {
			throw ThrowService.doIt(locale, 400, M.LOGIN_INVALID_TOKEN);
		} catch (Exception e) {
			throw ThrowService.doIt(locale, 500, e.getMessage());
		}

		userVO.setUserId(Integer.valueOf(jwtToken.getBody().getSubject()));
		userVO.setName(jwtToken.getBody().get("name").toString());
		userVO.setEmail(jwtToken.getBody().get("email").toString());
		return userVO;
	}
}
