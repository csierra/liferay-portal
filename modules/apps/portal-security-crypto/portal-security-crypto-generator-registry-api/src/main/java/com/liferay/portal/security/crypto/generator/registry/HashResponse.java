package com.liferay.portal.security.crypto.generator.registry;

import java.util.Optional;

public class HashResponse {

	public String algorithmMeta;
	public byte[] hash;
	public Optional<byte[]> salt;

}
