package com.liferay.oauth2.provider.scopes.spi;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface TokenProvider {

	public String createTokenString(TokenRequest tokenRequest);

	public boolean validateToken(
		String tokenString, GrantedTokenRequest tokenRequest);

	public static class TokenRequest {

		private final String _clientId;
		private final List<String> _scopes = new LinkedList<String>();
		private final String _grantType;
		private final long _userId;
		private final List<String> _audiences = new LinkedList<String>();

		public TokenRequest(
			String clientId, String grantType, long userId, String nonce,
			String responseType, String grantCode, long lifeTime) {

			_clientId = clientId;
			_grantType = grantType;
			_userId = userId;
			_nonce = nonce;
			_responseType = responseType;
			_grantCode = grantCode;
			_lifeTime = lifeTime;
		}

		private final String _nonce;
		private final String _responseType;
		private final String _grantCode;

		public String getClientId() {
			return _clientId;
		}

		public List<String> getScopes() {
			return _scopes;
		}

		public String getGrantType() {
			return _grantType;
		}

		public long getUserId() {
			return _userId;
		}

		public List<String> getAudiences() {
			return _audiences;
		}

		public String getNonce() {
			return _nonce;
		}

		public String getResponseType() {
			return _responseType;
		}

		public String getGrantCode() {
			return _grantCode;
		}

		public long getLifeTime() {
			return _lifeTime;
		}

		public Map<String, String> getExtraProperties() {
			return extraProperties;
		}

		private final long _lifeTime;
		private final Map<String, String> extraProperties = new LinkedHashMap<String, String>();

	}

	public static class GrantedTokenRequest extends TokenRequest {

		@Override
		public long getLifeTime() {
			return _lifeTime;
		}

		private final long _lifeTime;

		public long getIssuedAt() {
			return _issuedAt;
		}

		private final long _issuedAt;


		public GrantedTokenRequest(
			String clientId, String grantType, long userId, String nonce,
			String responseType, String grantCode, long lifeTime, long issuedAt) {

			super(clientId, grantType, userId, nonce, responseType, grantCode,
				lifeTime);

			_lifeTime = lifeTime;
			_issuedAt = issuedAt;
		}

	}

}
