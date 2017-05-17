package com.cj.weiboCrawler.cookie.comCookie;

import org.apache.http.client.HttpClient;

/**
 * status=1 无法获得cookie,一般是指无法登陆
 * 
 * status=2 可以获得cookie
 * 
 * status=3 可以获得cookie,但无法获得accessToken
 * 
 * status=4 帐号可以获得accessToken,即一切正常的帐号
 */
public class LoginPojo implements java.io.Serializable {
	private Integer id;
	private String uid;
	private String username;
	private String password;
	private HttpClient httpClient;
	private String type;
	private String status;
	private String cookie;
	private String accessToken;

	public LoginPojo() {
	}

	public LoginPojo(Integer id) {
		this.id = id;
	}

	public LoginPojo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public LoginPojo(Integer id, String uid, String username, String password,
			String status, String type) {
		this.id = id;
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.status = status;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}