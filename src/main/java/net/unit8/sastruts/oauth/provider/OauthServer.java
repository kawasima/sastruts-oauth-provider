package net.unit8.sastruts.oauth.provider;

public class OauthServer {
	private String baseUrl;
	private String requestTokenPath = "/oauth/request_token";
	private String authorizePath = "/oauth/authorize";
	private String accessTokenPath = "/oauth/auth_token";

	public OauthServer(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getRequestTokenPath() {
		return requestTokenPath;
	}

	public void setRequestTokenPath(String requestTokenPath) {
		this.requestTokenPath = requestTokenPath;
	}

	public String getAuthorizePath() {
		return authorizePath;
	}

	public void setAuthorizePath(String authorizePath) {
		this.authorizePath = authorizePath;
	}

	public String getAccessTokenPath() {
		return accessTokenPath;
	}

	public void setAccessTokenPath(String accessTokenPath) {
		this.accessTokenPath = accessTokenPath;
	}

}
