package com.digitusforum.login;

import org.apache.commons.lang3.StringUtils;
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

	@RequestMapping(value = "/login/v1/createToken")
	public TokenVO createToken(@RequestBody TokenVO tokenVO) {
		if (StringUtils.isBlank(tokenVO.getTokenType()))
			tokenVO.setTokenType("uuid");

		if (StringUtils.isBlank(tokenVO.getGrantType()))
			tokenVO.setGrantType("password");

		tokenVO = requestService.checkEmailAndPassword(tokenVO);
		return tokenService.createToken(tokenVO);
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
