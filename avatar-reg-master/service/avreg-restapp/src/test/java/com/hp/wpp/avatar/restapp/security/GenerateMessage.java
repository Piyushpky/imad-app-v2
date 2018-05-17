package com.hp.wpp.avatar.restapp.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.testng.annotations.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hp.wpp.avatar.framework.exceptions.EntityValidationException;
import com.hp.wpp.avatar.framework.exceptions.InvalidRequestException;
import com.hp.wpp.avatar.restmodel.json.schema.MessageValidationEntity;
import com.hp.wpp.postcard.common.PostcardConstants;

public class GenerateMessage {

	String cid = "AQAAAAFci0hEJQAAAAEbMpsw";
	String aid = "svc_connectivity";
	String printerKey = "dZ3dBdnFLYED3XAkqvF62Aag4JlbuuXmIIz/jTheKLo=";
	String message = "Testing SmartUX";

	//@Test
	public void generateMessage() throws EntityValidationException,
			InvalidRequestException {
		MessageValidationEntity messageValidationEntity = new MessageValidationEntity();
		messageValidationEntity.setVersion("1.0");
		messageValidationEntity.setCloudId(cid);
		messageValidationEntity.setApplicationId(aid);

		byte[] msg = message.getBytes();
		byte[] signature = encryptHMAC(msg, printerKey.getBytes());

		messageValidationEntity.setMessage(Base64
				.encodeBase64URLSafeString(msg));
		messageValidationEntity.setSignature(Base64
				.encodeBase64URLSafeString(signature));

		String payload = marshal(messageValidationEntity);

		System.out.println("=======Payload========");
		System.out.println(payload);
	}

	private byte[] encryptHMAC(byte[] content, byte[] key)
			throws EntityValidationException {
		byte[] encryptedData = null;
		Mac mac;
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key,
					PostcardConstants.HMAC_SHA256);
			mac = getMacInstance();
			mac.init(signingKey);
			encryptedData = mac.doFinal(content);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			System.out.println("Exception while generating hash: " + e);
			throw new EntityValidationException(e);
		}
		return encryptedData;
	}

	/**
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	protected Mac getMacInstance() throws NoSuchAlgorithmException {
		return Mac.getInstance(PostcardConstants.HMAC_SHA256);
	}

	public <T> Object unmarshal(Class<T> pojoClass, String jsonText)
			throws InvalidRequestException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonText, pojoClass);
		} catch (IOException e) {
			System.out.println("Exception while parsing json string: " + e);
			throw new InvalidRequestException(e);
		}
	}

	public <T> String marshal(T object) throws InvalidRequestException {
		try {
			ObjectWriter writter = getObjectWriter();
			return writter.writeValueAsString(object);
		} catch (IOException e) {
			System.out.println("Exception while serializing to json string: "
					+ e);
			throw new InvalidRequestException(e);
		}
	}

	protected ObjectWriter getObjectWriter() throws IOException {
		return new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY)
				.writer().withDefaultPrettyPrinter();
	}

}
