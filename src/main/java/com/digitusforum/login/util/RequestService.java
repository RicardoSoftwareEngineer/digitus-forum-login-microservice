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

import com.digitusforum.login.TokenVO;
import com.google.gson.Gson;

import i18.M;
import request.MicroservicesURLs;

public class RequestService {

	private void checkUserMS() {
		if (!isUp(MicroservicesURLs.USER))
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, M.USER_MICROSERVICE_OFFLINE);
	}

	public TokenVO checkEmailAndPassword(TokenVO tokenVO) {
		checkUserMS();
		String url = "http://localhost:8083/user/v1/retrieve/byEmailAndPassword";
		String jsonResponse = request(url, tokenVO);
		tokenVO = new Gson().fromJson(jsonResponse, TokenVO.class);
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
