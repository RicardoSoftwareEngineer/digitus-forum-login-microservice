package com.digitusforum.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitusforum.login.util.RequestService;

import request.RequestServiceDEPRECATED;
import user.UserMicroservice;
import user.UserVO;
import util.EnvironmentService;

@RestController
public class LoginController {
	UserMicroservice userMicroservice = new UserMicroservice(new RequestServiceDEPRECATED());
	TokenService tokenService = new TokenService(EnvironmentService.TOKEN_EXPIRATION_IN_SECONDS);
	
	RequestService requestService = new RequestService();

	@CrossOrigin
	@RequestMapping(value = "/login/v1/createToken")
	public Object createToken(@RequestBody TokenVO tokenVO) {
		// public Object createToken(@RequestParam Map<String, String> body) {

		/*
		 * TokenVO tokenVO = new TokenVO(); for (Map.Entry<String, String> entry :
		 * body.entrySet()) { if(entry.getKey().equals("inputEmail"))
		 * tokenVO.setUserEmail(entry.getValue());
		 * if(entry.getKey().equals("inputSenha"))
		 * tokenVO.setUserPassword(entry.getValue());
		 * 
		 * System.out.println(entry.getKey() + "/" + entry.getValue()); }
		 */

		if (StringUtils.isBlank(tokenVO.getTokenType()))
			tokenVO.setTokenType("uuid");

		if (StringUtils.isBlank(tokenVO.getGrantType()))
			tokenVO.setGrantType("password");

		TokenVO tokenCache = tokenService.checkCache(tokenVO);
		if(tokenCache != null)
			return tokenCache;
		
		tokenVO = requestService.checkEmailAndPassword(tokenVO);
		tokenVO = tokenService.createToken(tokenVO);
		tokenVO = requestService.getLastPerfilUsed(tokenVO);
		tokenService.updateCache(tokenVO);
		tokenVO.setPassword(null);
		
		
		return tokenVO;
		
		//HttpHeaders responseHeaders = new HttpHeaders();
		//responseHeaders.set("Content-Type", "application/json");
		//return new ResponseEntity<TokenVO>(tokenVO, responseHeaders, HttpStatus.CREATED);

		// return tokenService.createToken(tokenVO);
	}

	@RequestMapping(value = "/login/v1/validateToken")
	public TokenVO validateToken(@RequestBody TokenVO tokenVO) {
		if (StringUtils.isBlank(tokenVO.getTokenType()))
			tokenVO.setTokenType("uuid");

		if (StringUtils.isBlank(tokenVO.getGrantType()))
			tokenVO.setGrantType("password");

		tokenVO = tokenService.validateToken(tokenVO);
		return tokenVO;
	}

	@RequestMapping(value = "/login/v1/loginByEmailAndPassword")
	public Object loginByEmailAndPassword(@RequestHeader(defaultValue = "en_us") String locale,
			@RequestBody UserVO userVO) {
		userVO = userMicroservice.checkEmailAndPassword(userVO, locale);
		// tokenService.createJWTToken(userVO);
		return userVO;
	}

	@RequestMapping(value = "/login/v1/login")
	public Object login(String locale, @RequestBody UserVO userVO) {
		userVO = userMicroservice.checkEmailAndPassword(userVO, locale);
		// tokenService.createJWTToken(userVO);
		return userVO;
	}

}
