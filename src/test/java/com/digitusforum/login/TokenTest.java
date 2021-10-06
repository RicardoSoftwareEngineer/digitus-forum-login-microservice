package com.digitusforum.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import request.Headers;
import request.MicroservicesURLs;
import request.RequestServiceDEPRECATED;
import request.Timeouts;
import user.UserMicroservice;
import user.UserVO;
import util.EnvironmentService;

@SpringBootTest
public class TokenTest {
	private TokenService tokenService = new TokenService(EnvironmentService.TOKEN_EXPIRATION_IN_SECONDS);
	private static UserMicroservice userMicroservice;

	@BeforeAll
	public static void setupMock() {
		UserVO user = new UserVO(2, "ricardo", "ricardo@gmail.com", "password");
		ResponseEntity<UserVO> response = new ResponseEntity<UserVO>(user, HttpStatus.ACCEPTED);

		RequestServiceDEPRECATED requestService = mock(RequestServiceDEPRECATED.class);
		when(requestService.isUp(Mockito.anyString())).thenReturn(true);
		when(requestService.hitIt(Mockito.eq(MicroservicesURLs.USER_RETRIEVE_BY_EMAIL_AND_PASSWORD),
				Mockito.eq(Timeouts.ideal), Mockito.any(UserVO.class), Mockito.eq(Headers.DEFAULT("en_us"))))
						.thenReturn(response);
		userMicroservice = new UserMicroservice(requestService);
	}

	@Test
	void contextLoads() {
		assertThat(userMicroservice).isNotNull();
	}

	@Test
	void testTokenCreationAndValidation() {
		UserVO userTryingToLogin = new UserVO("ricardo@gmail.com", "password");
		userTryingToLogin = userMicroservice.checkEmailAndPassword(userTryingToLogin, "en_us");
		// userTryingToLogin = tokenService.createJWTToken(userTryingToLogin);
		assertThat(userTryingToLogin.getToken()).isNotNull();
		// userTryingToLogin = tokenService.validateToken("en_us", userTryingToLogin);
		assertThat(userTryingToLogin.getUserId()).isEqualTo(2);
	}

	@Test()
	void testExpiredToken() {
		ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
			TokenService tokenService = new TokenService(0);
			UserVO userTryingToLogin = new UserVO("ricardo@gmail.com", "password");
			userTryingToLogin = userMicroservice.checkEmailAndPassword(userTryingToLogin, "en_us");
			// userTryingToLogin = tokenService.createJWTToken(userTryingToLogin);
			// userTryingToLogin = tokenService.validateToken("en_us", userTryingToLogin);
		});
		assertEquals(thrown.getRawStatusCode(), 403);
	}

	@Test()
	void testInvalidToken() {
		ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
			UserVO userTryingToLogin = new UserVO("ricardo@gmail.com", "password");
			userTryingToLogin.setToken("aaaaaaaaaaaaaaaaa");
			// userTryingToLogin = tokenService.validateToken("en_us", userTryingToLogin);
		});
		assertEquals(thrown.getRawStatusCode(), 400);
	}

}
