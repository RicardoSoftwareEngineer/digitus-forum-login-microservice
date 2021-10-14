package com.digitusforum.login;

import java.time.ZonedDateTime;

public class TokenVO {
	private String userId;
	private String userName;
	private String email;
	private String password;
	private String tokenType;
	private String grantType;
	private String token;
	private String lastPerfilUsed;
	private String lastPerfilType;
	private String lastPerfilName;
	private long stillValidForSeconds;
	private ZonedDateTime createdIn;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getLastPerfilUsed() {
		return lastPerfilUsed;
	}

	public void setLastPerfilUsed(String lastPerfilUsed) {
		this.lastPerfilUsed = lastPerfilUsed;
	}

	public String getLastPerfilType() {
		return lastPerfilType;
	}

	public void setLastPerfilType(String lastPerfilType) {
		this.lastPerfilType = lastPerfilType;
	}

	public String getLastPerfilName() {
		return lastPerfilName;
	}

	public void setLastPerfilName(String lastPerfilName) {
		this.lastPerfilName = lastPerfilName;
	}

	public long getStillValidForSeconds() {
		return stillValidForSeconds;
	}

	public void setStillValidForSeconds(long stillValidForSeconds) {
		this.stillValidForSeconds = stillValidForSeconds;
	}

	public ZonedDateTime getCreatedIn() {
		return createdIn;
	}

	public void setCreatedIn(ZonedDateTime createdIn) {
		this.createdIn = createdIn;
	}

}
