package com.digitusforum.login.endpoint;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitusforum.login.service.TokenService;

import microservice.UserMicroservice;
import service.TimeService;
import vo.TokenVO;
import vo.UserVO;


@RestController
public class LoginController {
	UserMicroservice userMicroservice = new UserMicroservice();
	TokenService TokenService = new TokenService();
	
	@RequestMapping(value = "/login/v1/loginByEmailAndPassword")
    public Object loginByEmailAndPassword(@RequestHeader(defaultValue = "en_us") String locale, @RequestBody UserVO userVO) {
		userVO = userMicroservice.checkEmailAndPassword(userVO, locale);
		String token = TokenService.createJWTToken(ZonedDateTime.now().plus(1, ChronoUnit.MINUTES), userVO);
		
		return new TokenVO();
	}

}
