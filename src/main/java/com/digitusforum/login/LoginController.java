package com.digitusforum.login;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitusforum.login.util.EnvironmentService;
import com.digitusforum.login.util.RequestService;

@RestController
public class LoginController {
	TokenService tokenService = new TokenService(EnvironmentService.TOKEN_EXPIRATION_IN_SECONDS);

	RequestService requestService = new RequestService();

	@CrossOrigin
	@RequestMapping(value = "/login/v1/createToken")
	public Object createToken(@RequestBody TokenVO tokenVO) {
		if (StringUtils.isBlank(tokenVO.getTokenType()))
			tokenVO.setTokenType("uuid");

		if (StringUtils.isBlank(tokenVO.getGrantType()))
			tokenVO.setGrantType("password");

		TokenVO tokenCache = tokenService.checkCache(tokenVO);
		if (tokenCache != null)
			return tokenCache;

		tokenVO = requestService.checkEmailAndPassword(tokenVO);
		tokenVO = tokenService.createToken(tokenVO);
		tokenVO = requestService.getLastPerfilUsed(tokenVO);
		tokenService.updateCache(tokenVO);
		tokenVO.setPassword(null);
		return tokenVO;
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
}
