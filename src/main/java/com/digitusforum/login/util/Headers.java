package com.digitusforum.login.util;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

public class Headers {
	public static MultiValueMap<String, String> DEFAULT(String locale){
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("locale", locale);
        headers.add("Content-Type", "application/json");
        return headers;
    }
	
	public static MultiValueMap<String, String> DEFAULT(){
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }

}
