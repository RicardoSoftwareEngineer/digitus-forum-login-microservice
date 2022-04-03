package com.digitusforum.login.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
	
	@RequestMapping("/test")
	public String test() {
		return "login";
	}

	@RequestMapping(value = "/login/v1/healthCheck")
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.status(HttpStatus.OK).body("ok");
	}
	
	@RequestMapping(value = "/healthCheck")
	public ResponseEntity<String> healthCheck2() {
		return ResponseEntity.status(HttpStatus.OK).body("ok");
	}
}
