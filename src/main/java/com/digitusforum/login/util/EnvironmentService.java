package com.digitusforum.login.util;

import org.apache.commons.lang3.StringUtils;

public class EnvironmentService {
	public static String I18_SERVER_PORT = getEnvVar("I18_SERVER_PORT");
	public static String I18_SERVER_URL = getEnvVar("I18_SERVER_URL");
	public static String I18_SERVER_VERSION = getEnvVar("I18_SERVER_VERSION");
	
	public static String LOGIN_SERVER_PORT = getEnvVar("LOGIN_SERVER_PORT");
	public static String LOGIN_SERVER_URL = getEnvVar("LOGIN_SERVER_URL");
	public static String LOGIN_SERVER_VERSION = getEnvVar("LOGIN_SERVER_VERSION");
	
	public static String USER_SERVER_PORT = getEnvVar("USER_SERVER_PORT");
	public static String USER_SERVER_URL = getEnvVar("USER_SERVER_URL");
	public static String USER_SERVER_VERSION = getEnvVar("USER_SERVER_VERSION");
	
	public static String PERFIL_SERVER_PORT = getEnvVar("PERFIL_SERVER_PORT");
	public static String PERFIL_SERVER_URL = getEnvVar("PERFIL_SERVER_URL");
	public static String PERFIL_SERVER_VERSION = getEnvVar("PERFIL_SERVER_VERSION");
	
	public static String TRAIL_AND_COURSE_SERVER_PORT = getEnvVar("TRAIL_AND_COURSE_SERVER_PORT");
	public static String TRAIL_AND_COURSE_SERVER_URL = getEnvVar("TRAIL_AND_COURSE_SERVER_URL");
	public static String TRAIL_AND_COURSE_SERVER_VERSION = getEnvVar("TRAIL_AND_COURSE_SERVER_VERSION");
	
	
	public static String JWT_KEY = getEnvVar("JWT_KEY");
	public static int TOKEN_EXPIRATION_IN_SECONDS = Integer.valueOf(getEnvVar("TOKEN_EXPIRATION_IN_SECONDS"));

	private static String getEnvVar(String envVar) {
		if (StringUtils.isNotBlank(System.getProperty(envVar, System.getenv(envVar)))) {
			return System.getProperty(envVar, System.getenv(envVar));
		}

		// default values for environment variables
		switch (envVar) {
		case "I18_SERVER_PORT": return "8081";
		case "I18_SERVER_URL": return "http://localhost:";
		case "I18_SERVER_VERSION": return "/v1";
		
		case "LOGIN_SERVER_PORT": return "8082";
		case "LOGIN_SERVER_URL": return "http://localhost:";
		case "LOGIN_SERVER_VERSION": return "/v1";
		
		case "USER_SERVER_PORT": return "8083";
		case "USER_SERVER_URL": return "http://localhost:";
		case "USER_SERVER_VERSION": return "/v1";
		
		case "TRAIL_AND_COURSE_SERVER_PORT": return "8087";
		case "TRAIL_AND_COURSE_SERVER_URL": return "http://localhost:";
		case "TRAIL_AND_COURSE_SERVER_VERSION": return "/v1";
		
		case "PERFIL_SERVER_PORT": return "8088";
		case "PERFIL_SERVER_URL": return "http://localhost:";
		case "PERFIL_SERVER_VERSION": return "/v1";	
		
		case "JWT_KEY": return "a secret key";
		case "TOKEN_EXPIRATION_IN_SECONDS": return "3000";
		default: return "";
		}
	}
}
