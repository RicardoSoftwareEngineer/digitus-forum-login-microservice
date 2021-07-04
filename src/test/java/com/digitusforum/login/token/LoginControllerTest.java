package com.digitusforum.login.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import com.digitusforum.login.service.TokenService;

import microservice.UserMicroservice;
import vo.UserVO;

@SpringBootTest
public class LoginControllerTest {
	private TokenService tokenService = new TokenService();
	private static UserMicroservice userMicroservice;

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
		userTryingToLogin = tokenService.createJWTToken(ZonedDateTime.now().plus(30, ChronoUnit.MINUTES),
				userTryingToLogin);
		assertThat(userTryingToLogin.getToken()).isNotNull();
		userTryingToLogin = tokenService.validateToken("en_us", userTryingToLogin);
		assertThat(userTryingToLogin.getName()).isEqualTo("ricardo");
	}

	@Test()
	void testExpiredToken() {
		ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
			UserVO userTryingToLogin = new UserVO("ricardo@gmail.com", "password");
			userTryingToLogin = userMicroservice.checkEmailAndPassword(userTryingToLogin, "en_us");
			userTryingToLogin = tokenService.createJWTToken(ZonedDateTime.now(), userTryingToLogin);
			userTryingToLogin = tokenService.validateToken("en_us", userTryingToLogin);
		});
		assertEquals(thrown.getRawStatusCode(), 403);
	}

	@Test()
	void testInvalidToken() {
		ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
			UserVO userTryingToLogin = new UserVO("ricardo@gmail.com", "password");
			userTryingToLogin.setToken("aaaaaaaaaaaaaaaaa");
			userTryingToLogin = tokenService.validateToken("en_us", userTryingToLogin);
		});
		assertEquals(thrown.getRawStatusCode(), 400);
	}

}
