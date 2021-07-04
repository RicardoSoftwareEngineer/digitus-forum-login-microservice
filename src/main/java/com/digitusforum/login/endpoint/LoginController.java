package com.digitusforum.login.endpoint;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitusforum.login.service.TokenService;

import microservice.UserMicroservice;
import service.EnvironmentService;
import service.RequestService;
import vo.TokenVO;
import vo.UserVO;

@RestController
public class LoginController {
	UserMicroservice userMicroservice = new UserMicroservice();
	TokenService tokenService = new TokenService(EnvironmentService.TOKEN_EXPIRATION_IN_MINUTES);

	@RequestMapping(value = "/login/v1/loginByEmailAndPassword")
	public Object loginByEmailAndPassword(@RequestHeader(defaultValue = "en_us") String locale,
			@RequestBody UserVO userVO) {
		userVO = userMicroservice.checkEmailAndPassword(userVO, locale);
		userVO = tokenService.createJWTToken(userVO);
		return userVO;
	}

	@RequestMapping(value = "/login/v1/validateToken")
	public Object validateToken(@RequestHeader(defaultValue = "en_us") String locale, @RequestBody UserVO userVO) {
		return tokenService.validateToken(locale, userVO);
	}

}
