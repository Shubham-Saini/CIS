package com.drive.oauth;

public class DriveUser 
{
	private String name;
	private String userId;
	private String email;
	private String accessToken;
	private String refreshToken;
	private String server;
	public DriveUser()
	{
		
	}
	public DriveUser(String name, String userId, String email,
			String accessToken, String refreshToken, String server) {
		super();
		this.name = name;
		this.userId = userId;
		this.email = email;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.server = server;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
}
