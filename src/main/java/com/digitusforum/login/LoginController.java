package com.digitusforum.login;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitusforum.login.util.RequestService;

@RestController
public class LoginController {
	TokenService tokenService = new TokenService();

	RequestService requestService = new RequestService();

	@CrossOrigin
	@RequestMapping(value = "/login/v1/createToken")
	public Object createToken(@RequestBody TokenVO tokenVO) {
		if (StringUtils.isBlank(tokenVO.getTokenType()))
			tokenVO.setTokenType("uuid");

		if (StringUtils.isBlank(tokenVO.getGrantType()))
			tokenVO.setGrantType("password");

		tokenVO = requestService.checkEmailAndPassword(tokenVO);
		tokenVO = tokenService.createToken(tokenVO);
		// tokenVO = requestService.getLastPerfilUsed(tokenVO);
		//tokenVO.setPassword(null); this is done in FirewallLoginService.getFromCache
		return tokenVO;
	}

}
