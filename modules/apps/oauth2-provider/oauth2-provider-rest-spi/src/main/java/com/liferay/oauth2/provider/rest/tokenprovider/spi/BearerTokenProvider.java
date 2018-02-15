package com.liferay.oauth2.provider.rest.tokenprovider.spi;

import com.liferay.oauth2.provider.model.OAuth2Application;

import java.util.List;
import java.util.Map;

public interface BearerTokenProvider {

	public default void createAccessToken(AccessToken accessToken) {
	}

	public default void createRefreshToken(RefreshToken refreshToken) {
	}

	public boolean isValid(AccessToken accessToken);

	public default boolean isValid(RefreshToken refreshToken) {
		return true;
	}

	public static class AccessToken {
		public AccessToken(
			OAuth2Application oAuth2Application,
			List<String> audiences, String clientCodeVerifier, long expiresIn,
			Map<String, String> extraProperties, String grantCode,
			String grantType, long issuedAt, String issuer, String nonce,
			Map<String, String> parameters, String refreshToken,
			String responseType, List<String> scopes, String tokenKey,
			String tokenType, long userId, String userName) {

			_oAuth2Application = oAuth2Application;
			_audiences = audiences;
			_clientCodeVerifier = clientCodeVerifier;
			_expiresIn = expiresIn;
			_extraProperties = extraProperties;
			_grantCode = grantCode;
			_grantType = grantType;
			_issuedAt = issuedAt;
			_issuer = issuer;
			_nonce = nonce;
			_parameters = parameters;
			_refreshToken = refreshToken;
			_responseType = responseType;
			_scopes = scopes;
			_tokenKey = tokenKey;
			_tokenType = tokenType;
			_userId = userId;
			_userName = userName;
		}

		public OAuth2Application getOAuth2Application() {
			return _oAuth2Application;
		}

		public void setOAuth2Application(
			OAuth2Application oAuth2Application) {
			_oAuth2Application = oAuth2Application;
		}

		public List<String> getAudiences() {
			return _audiences;
		}

		public void setAudiences(List<String> audiences) {
			_audiences = audiences;
		}

		public String getClientCodeVerifier() {
			return _clientCodeVerifier;
		}

		public void setClientCodeVerifier(String clientCodeVerifier) {
			_clientCodeVerifier = clientCodeVerifier;
		}

		public long getExpiresIn() {
			return _expiresIn;
		}

		public void setExpiresIn(long expiresIn) {
			_expiresIn = expiresIn;
		}

		public Map<String, String> getExtraProperties() {
			return _extraProperties;
		}

		public void setExtraProperties(
			Map<String, String> extraProperties) {
			_extraProperties = extraProperties;
		}

		public String getGrantCode() {
			return _grantCode;
		}

		public void setGrantCode(String grantCode) {
			_grantCode = grantCode;
		}

		public String getGrantType() {
			return _grantType;
		}

		public void setGrantType(String grantType) {
			_grantType = grantType;
		}

		public long getIssuedAt() {
			return _issuedAt;
		}

		public void setIssuedAt(long issuedAt) {
			_issuedAt = issuedAt;
		}

		public String getIssuer() {
			return _issuer;
		}

		public void setIssuer(String issuer) {
			_issuer = issuer;
		}

		public String getNonce() {
			return _nonce;
		}

		public void setNonce(String nonce) {
			_nonce = nonce;
		}

		public Map<String, String> getParameters() {
			return _parameters;
		}

		public void setParameters(
			Map<String, String> parameters) {
			_parameters = parameters;
		}

		public String getRefreshToken() {
			return _refreshToken;
		}

		public void setRefreshToken(String refreshToken) {
			_refreshToken = refreshToken;
		}

		public String getResponseType() {
			return _responseType;
		}

		public void setResponseType(String responseType) {
			_responseType = responseType;
		}

		public List<String> getScopes() {
			return _scopes;
		}

		public void setScopes(List<String> scopes) {
			_scopes = scopes;
		}

		public String getTokenKey() {
			return _tokenKey;
		}

		public void setTokenKey(String tokenKey) {
			_tokenKey = tokenKey;
		}

		public String getTokenType() {
			return _tokenType;
		}

		public void setTokenType(String tokenType) {
			_tokenType = tokenType;
		}

		public long getUserId() {
			return _userId;
		}

		public void setUserId(long userId) {
			_userId = userId;
		}

		public String getUserName() {
			return _userName;
		}

		public void setUserName(String userName) {
			_userName = userName;
		}

		private OAuth2Application _oAuth2Application;
		private List<String> _audiences;
		private String _clientCodeVerifier;
		private long _expiresIn;
		private Map<String, String> _extraProperties;
		private String _grantCode;
		private String _grantType;
		private long _issuedAt;
		private String _issuer;
		private String _nonce;
		private Map<String, String> _parameters;
		private String _refreshToken;
		private String _responseType;
		private List<String> _scopes;
		private String _tokenKey;
		private String _tokenType;
		private long _userId;
		private String _userName;
	}

	public static class RefreshToken {
		public RefreshToken(
			OAuth2Application oAuth2Application,
			List<String> audiences, String clientCodeVerifier, long expiresIn,
			String grantType, long issuedAt, List<String> scopes,
			String tokenKey, String tokenType, long userId, String userName) {

			_oAuth2Application = oAuth2Application;
			_audiences = audiences;
			_clientCodeVerifier = clientCodeVerifier;
			_expiresIn = expiresIn;
			_grantType = grantType;
			_issuedAt = issuedAt;
			_scopes = scopes;
			_tokenKey = tokenKey;
			_tokenType = tokenType;
			_userId = userId;
			_userName = userName;
		}

		public OAuth2Application getOAuth2Application() {
			return _oAuth2Application;
		}

		public void setOAuth2Application(
			OAuth2Application oAuth2Application) {
			_oAuth2Application = oAuth2Application;
		}

		public List<String> getAudiences() {
			return _audiences;
		}

		public void setAudiences(List<String> audiences) {
			_audiences = audiences;
		}

		public String getClientCodeVerifier() {
			return _clientCodeVerifier;
		}

		public void setClientCodeVerifier(String clientCodeVerifier) {
			_clientCodeVerifier = clientCodeVerifier;
		}

		public long getExpiresIn() {
			return _expiresIn;
		}

		public void setExpiresIn(long expiresIn) {
			_expiresIn = expiresIn;
		}

		public String getGrantType() {
			return _grantType;
		}

		public void setGrantType(String grantType) {
			_grantType = grantType;
		}

		public long getIssuedAt() {
			return _issuedAt;
		}

		public void setIssuedAt(long issuedAt) {
			_issuedAt = issuedAt;
		}

		public List<String> getScopes() {
			return _scopes;
		}

		public void setScopes(List<String> scopes) {
			_scopes = scopes;
		}

		public String getTokenKey() {
			return _tokenKey;
		}

		public void setTokenKey(String tokenKey) {
			_tokenKey = tokenKey;
		}

		public String getTokenType() {
			return _tokenType;
		}

		public void setTokenType(String tokenType) {
			_tokenType = tokenType;
		}

		public long getUserId() {
			return _userId;
		}

		public void setUserId(long userId) {
			_userId = userId;
		}

		public String getUserName() {
			return _userName;
		}

		public void setUserName(String userName) {
			_userName = userName;
		}

		private OAuth2Application _oAuth2Application;
		private List<String> _audiences;
		private String _clientCodeVerifier;
		private long _expiresIn;
		private String _grantType;
		private long _issuedAt;
		private List<String> _scopes;
		private String _tokenKey;
		private String _tokenType;
		private long _userId;
		private String _userName;
	}

}