package com.digitusforum.login.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.digitusforum.login.service.TokenService;

import microservice.UserMicroservice;
import service.EnvironmentService;
import vo.UserVO;

@SpringBootTest
public class LoginControllerTest {
	private TokenService tokenService = new TokenService();
	private static UserMicroservice userMicroservice;
	int expireIn = Integer.valueOf(EnvironmentService.TOKEN_EXPIRATION_IN_MINUTES);

	@BeforeAll
	public static void setupMock() {
		userMicroservice = mock(UserMicroservice.class);
		when(userMicroservice.checkEmailAndPassword(Mockito.any(UserVO.class), Mockito.anyString()))
				.thenReturn(new UserVO(2, "ricardo", "ricardo@gmail.com", "password"));
	}

	@Test
	void contextLoads() {
		assertThat(userMicroservice).isNotNull();
	}

	@Test
	void testTokenCreationAndValidation() {
		UserVO userTryingToLogin = new UserVO("ricardo@gmail.com", "password");
		userTryingToLogin = userMicroservice.checkEmailAndPassword(userTryingToLogin, "en_us");
		userTryingToLogin = tokenService.createJWTToken(ZonedDateTime.now().plus(expireIn, ChronoUnit.MINUTES), userTryingToLogin);
		assertThat(userTryingToLogin.getToken()).isNotNull();
		userTryingToLogin = tokenService.validateToken("en_us", userTryingToLogin);
		assertThat(userTryingToLogin.getName()).isEqualTo("ricardo");
	}

}
