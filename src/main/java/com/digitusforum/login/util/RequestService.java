package com.digitusforum.login.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.digitusforum.login.PerfilVO;
import com.digitusforum.login.TokenVO;
import com.digitusforum.login.UserVO;
import com.google.gson.Gson;


public class RequestService {

	private void checkUserMS() {
		if (!isUp(MicroservicesURLs.USER))
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, M.USER_MICROSERVICE_OFFLINE);
	}

	private void checkPerfilMS() {
		if (!isUp(MicroservicesURLs.PERFIL))
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, M.PERFIL_MICROSERVICE_OFFLINE);
	}

	public TokenVO getLastPerfilUsed(TokenVO tokenVO) {
		checkPerfilMS();
		PerfilVO perfilVO = new PerfilVO();
		perfilVO.setUserId(tokenVO.getUserId());
		String url = MicroservicesURLs.PERFIL_RETRIEVE_LAST_USED;
		String jsonResponse = request(url, perfilVO);
		perfilVO = new Gson().fromJson(jsonResponse, PerfilVO.class);
		tokenVO.setLastPerfilUsed(perfilVO.getId());
		tokenVO.setLastPerfilType(perfilVO.getType());
		tokenVO.setLastPerfilName(perfilVO.getName());
		return tokenVO;
	}

	public TokenVO checkEmailAndPassword(TokenVO tokenVO) {
		checkUserMS();
		UserVO userVO = new UserVO();
		userVO.setEmail(tokenVO.getEmail());
		userVO.setPassword(tokenVO.getPassword());
		String url = "http://localhost:8083/user/v1/retrieve/byEmailAndPassword";
		String jsonResponse = request(url, userVO);
		userVO = new Gson().fromJson(jsonResponse, UserVO.class);
		// tokenVO = new ModelMapper().map(userVO, TokenVO.class);
		tokenVO.setUserId(userVO.getId());
		// tokenVO.setUserName(userVO.getName());
		return tokenVO;
	}

	public boolean isUp(String endpoint) {
		String requestTimeId = TimeService.startCounting();
		try {
			request(endpoint + "/healthCheck");
		} catch (Exception e) {
			TimeService.persistElapsedTimeout(requestTimeId, endpoint);
			return false;
		}
		return true;
	}

	public String request(String endpoint) {
		return request(endpoint, Timeouts.debug, "", null);
	}

	public String request(String endpoint, Object requestEntityBody) {
		return request(endpoint, Timeouts.debug, requestEntityBody, Headers.DEFAULT());
	}

	public String request(String endpoint, int timeout, Object requestEntityBody,
			MultiValueMap<String, String> headers) {
		try {
			String requestTimeId = TimeService.startCounting();
			RestTemplate restTemplate = new RestTemplate();
			((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(timeout);
			((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(timeout);
			final HttpEntity<Object> entity = new HttpEntity<>(requestEntityBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);
			TimeService.persistElapsedTime(requestTimeId, endpoint);
			return response.getBody();
		} catch (HttpClientErrorException e) {
			String errorMessage = e.getMessage().replace("[", "").replace("]", "").substring(6);
			ErrorMessageVO errorMessageVO = new Gson().fromJson(errorMessage, ErrorMessageVO.class);
			throw new ResponseStatusException(e.getStatusCode(), errorMessageVO.getMessage());
		}
	}
}
