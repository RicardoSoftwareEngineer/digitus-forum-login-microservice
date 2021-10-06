package com.digitusforum.login;

import java.time.ZonedDateTime;

public class TokenVO {
	private int userId;
	private String userName;
	private String userEmail;
	private String userPassword;
	private String tokenType;
	private String grantType;
	private String token;
	private long stillValidForSeconds;
	private ZonedDateTime createdIn;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public ZonedDateTime getCreatedIn() {
		return createdIn;
	}
	public void setCreatedIn(ZonedDateTime createdIn) {
		this.createdIn = createdIn;
	}
	public long getStillValidForSeconds() {
		return stillValidForSeconds;
	}
	public void setStillValidForSeconds(long stillValidForSeconds) {
		this.stillValidForSeconds = stillValidForSeconds;
	}


	

}
