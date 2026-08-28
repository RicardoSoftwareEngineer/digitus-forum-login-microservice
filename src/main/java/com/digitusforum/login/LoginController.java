package com.digitusforum.login;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.digitusforum.login.util.M;
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

		if (StringUtils.isNotBlank(tokenVO.getPassword())) {
			// legado: email+senha. Produto não usa (REGRA-CREDS-1 revogado).
			tokenVO = requestService.checkEmailAndPassword(tokenVO);
		} else {
			if (StringUtils.isBlank(tokenVO.getEmail()) && StringUtils.isBlank(tokenVO.getUserId()))
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, M.LOGIN_MISSING_EMAIL);
		}
		tokenVO = tokenService.createToken(tokenVO);
		tokenVO.setPassword(null);
		return tokenVO;
	}

}
