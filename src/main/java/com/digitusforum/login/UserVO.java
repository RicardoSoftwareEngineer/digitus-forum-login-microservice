package com.digitusforum.login;

public class UserVO {
	private String id;
	private String name;
	private String email;
	private String type;
	private String password;
	private String token;
	private boolean deleted;

	public UserVO() {
	}

	public UserVO(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public UserVO(String userId, String name, String email, String password) {
		this.id = userId;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
