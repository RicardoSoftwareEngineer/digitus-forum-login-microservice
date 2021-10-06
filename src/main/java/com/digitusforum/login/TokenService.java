package com.digitusforum.login;

import java.security.Key;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import i18.M;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import util.EnvironmentService;

public class TokenService {
	private int expirationInSeconds;
	private Map<String, TokenVO> uuidTokens = new HashMap<>();

	public TokenService(int expirationInSeconds) {
		this.expirationInSeconds = expirationInSeconds;
	}

	public TokenVO createToken(TokenVO tokenVO) {
		if (tokenVO.getTokenType().equalsIgnoreCase("bearer"))
			return createJWTToken(tokenVO);

		if (tokenVO.getTokenType().equalsIgnoreCase("uuid"))
			return createUuidToken(tokenVO);
		return tokenVO;
	}

	public TokenVO validateToken(TokenVO tokenVO) {
		if (tokenVO.getTokenType().equalsIgnoreCase("bearer"))
			return validateBearerToken(tokenVO);

		if (tokenVO.getTokenType().equalsIgnoreCase("uuid"))
			return validateUuidToken(tokenVO);
		return tokenVO;
	}

	public TokenVO createUuidToken(TokenVO tokenVO) {
		tokenVO.setCreatedIn(ZonedDateTime.now());
		tokenVO.setToken(UUID.randomUUID().toString());
		uuidTokens.put(tokenVO.getToken(), tokenVO);
		return tokenVO;
	}

	public TokenVO validateUuidToken(TokenVO tokenVO) {
		tokenVO = uuidTokens.get(tokenVO.getToken());
		if (tokenVO == null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, M.LOGIN_INVALID_TOKEN);

		long tokenAgeInSeconds = Duration.between(tokenVO.getCreatedIn(), ZonedDateTime.now()).getSeconds();
		if (tokenAgeInSeconds > expirationInSeconds)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, M.LOGIN_EXPIRED_TOKEN);

		tokenVO.setStillValidForSeconds(expirationInSeconds - tokenAgeInSeconds);
		return tokenVO;
	}

	public TokenVO createJWTToken(TokenVO tokenVO) {
		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(EnvironmentService.JWT_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		ZonedDateTime expiration = ZonedDateTime.now().plus(expirationInSeconds, ChronoUnit.SECONDS);

		// Let's set the JWT Claims
		// Builds the JWT and serializes it to a compact, URL-safe string
		String token = Jwts.builder().setSubject(String.valueOf(tokenVO.getUserId()))
				.setIssuer("digitus forum login microservice").setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
				.setExpiration(Date.from(expiration.toInstant())).claim("provider", "provider")
				.claim("email", tokenVO.getUserEmail()).claim("name", tokenVO.getUserName())
				.claim("id", tokenVO.getUserId()).signWith(signatureAlgorithm, signingKey).compact();
		tokenVO.setToken(token);
		tokenVO.setUserId(0);
		return tokenVO;
	}

	private TokenVO validateBearerToken(TokenVO tokenVO) {
		String token = null;
		Jws<Claims> jwtToken = null;
		try {
			// String[] tokenData = tokenVO.getToken().split(" ");
			// if (tokenData.length != 2)
			// throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
			// M.LOGIN_INVALID_TOKEN);
			// String tokenType = tokenData[0];
			// token = tokenData[1];
			token = tokenVO.getToken();
			jwtToken = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(EnvironmentService.JWT_KEY))
					.parseClaimsJws(token);
		} catch (MalformedJwtException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, M.LOGIN_INVALID_TOKEN);
		} catch (ExpiredJwtException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, M.LOGIN_EXPIRED_TOKEN);
		} catch (SignatureException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, M.LOGIN_INVALID_TOKEN);
		} catch (ResponseStatusException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, M.LOGIN_INVALID_TOKEN);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, M.INTERNAL_SERVER_ERROR);
		}

		tokenVO.setUserId(Integer.valueOf(jwtToken.getBody().getSubject()));
		tokenVO.setUserEmail(jwtToken.getBody().get("email").toString());
		return tokenVO;
	}

}
