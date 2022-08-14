package com.digitusforum.login.util;


public class MicroservicesURLs {

	public static String USER = EnvironmentService.USER_SERVER_URL + EnvironmentService.USER_SERVER_PORT + "/user" + EnvironmentService.USER_SERVER_VERSION;
	public static String USER_RETRIEVE_BY_EMAIL_AND_PASSWORD = USER + "/retrieve/byEmailAndPassword"; 
	public static String USER_CREATE = USER + "/create"; 
	public static String USER_RETRIEVE_USERS = USER + "/retrieve"; 
	public static String USER_RETRIEVE_BY_ID = USER + "/%s/retrieve"; 
	public static String USER_UPDATE = USER + "/%s/update"; 
	public static String USER_DELETE = USER + "/%s/delete"; 
	
	public static String PERFIL = EnvironmentService.PERFIL_SERVER_URL + EnvironmentService.PERFIL_SERVER_PORT + "/perfil" + EnvironmentService.PERFIL_SERVER_VERSION;
	public static String PERFIL_RETRIEVE_LAST_USED = PERFIL + "/retrieve/lastUsed"; 
	

}
