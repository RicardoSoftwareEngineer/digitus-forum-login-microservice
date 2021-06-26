package com.digitusforum.login.endpoint;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import vo.TokenVO;
import vo.UserVO;


@RestController
public class LoginController {
	
	@PostMapping(value = "/login/v1/loginByEmailAndPassword")
    public Object loginByEmailAndPassword(@RequestHeader(defaultValue = "en_us") String locale, @RequestBody UserVO userVO) {
		UserVO user = new UserVO();
		user.setName("sup bro aihusiuahsui");
		return user;
	}

}
