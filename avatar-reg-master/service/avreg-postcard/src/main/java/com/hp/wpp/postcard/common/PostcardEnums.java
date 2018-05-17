/**
 * 
 */
package com.hp.wpp.postcard.common;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.io.ByteStreams;
import com.hp.wpp.postcard.compression.Compresssor;
import com.hp.wpp.postcard.compression.GZipCompressor;
import com.hp.wpp.postcard.compression.ZipCompressor;

/**
 * @author mahammad
 *
 */
public class PostcardEnums {

	public enum Creator {

		SERVICE("service"), ENTITY("entity");

		private final String creator;

		Creator(String value) {
			this.creator = value;
		}

		public String getCreatorName() {
			return creator;
		}
		
		@JsonCreator
		public static Creator getCreator(String creator) {

			Creator[] creators = Creator.values();

			for (Creator type : creators) {
				if (type.getCreatorName().equalsIgnoreCase(creator)) {
					return type;
				}
			}
			return ENTITY;
		}
	}
	
	public enum ApplicationType {

		AVATAR_REGISTRATION(1), OTHER(0);

		private final int applicationId;

		ApplicationType(int applicationId) {
			this.applicationId = applicationId;
		}

		public int getApplicationId() {
			return applicationId;
		}
		
		@JsonCreator
		public static ApplicationType getApplicationType(int applicationId) {

			ApplicationType[] applicationTypes = ApplicationType.values();

			for (ApplicationType type : applicationTypes) {
				if (type.getApplicationId() == applicationId) {
					return type;
				}
			}
			return OTHER;
		}
	}
	
	public enum Compression {

		NONE("none") {
			@Override
			public byte[] compress(byte[] bytes) throws IOException {
				return bytes;
			}

			@Override
			public byte[] unCompress(byte[] bytes) throws IOException {
				return bytes;
			}
		}, GZIP("gzip") {

			Compresssor compresssor = new ZipCompressor();

			@Override
			public byte[] compress(byte[] bytes) throws IOException {
				return compresssor.compressByteArray(bytes);
			}

			@Override
			public byte[] unCompress(byte[] bytes) throws IOException {
				return compresssor.uncompressByteArray(bytes);
			}

		}, DEFLATE("deflate") {
			
			Compresssor compresssor = new ZipCompressor();

			@Override
			public byte[] compress(byte[] bytes) throws IOException {
				return compresssor.compressByteArray(bytes);
			}

			@Override
			public byte[] unCompress(byte[] bytes) throws IOException {
				return compresssor.uncompressByteArray(bytes);
			}
			
		}, HTTP_GZIP("http_gzip") {
			
			Compresssor compresssor = new GZipCompressor();

			@Override
			public byte[] compress(byte[] bytes) throws IOException {
				return compresssor.compressByteArray(bytes);
			}

			@Override
			public byte[] unCompress(byte[] bytes) throws IOException {
				return compresssor.uncompressByteArray(bytes);
			}
			
		};
		public abstract byte[] compress(byte[] bytes) throws IOException;

		public abstract byte[] unCompress(byte[] bytes) throws IOException;
		
		private final String value;

		Compression(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		@JsonCreator
		public static Compression getCompression(String compression) {

			Compression[] compressionTypes = Compression.values();

			for (Compression type : compressionTypes) {
				if (!StringUtils.isBlank(compression) && type.getValue().equalsIgnoreCase(compression)) {
					return type;
				}
			}
			return NONE;
		}
	}
	
	public enum Encryption {

		AES_128("aes_128"), NONE("none");

		private final String value;
		
		Encryption(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		@JsonCreator
		public static Encryption getEncryption(String encryption) {

			Encryption[] encryptionTypes = Encryption.values();

			for (Encryption type : encryptionTypes) {
				if (!StringUtils.isBlank(encryption) && type.getValue().equalsIgnoreCase(encryption)) {
					return type;
				}
			}
			return NONE;
		}
	}
	
	public enum ContentType {

		APPLICATION_JSON("application/json"), 
		APPLICATION_XML("application/xml"), 
		APPLICATION_IPP("application/ipp"), 
		APPLICATION_TEXT("application/text"), 
		APPLICATION_OTHER("application/other"),
		APPLICATION_SERVICE_INSTRUCTION("application/vnd.hpi.postcard.service_instruction+json");

		private final String value;

		ContentType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		@JsonCreator
		public static ContentType getContentType(String contentType) {

			ContentType[] contentTypes = ContentType.values();

			for (ContentType type : contentTypes) {
				if (!StringUtils.isBlank(contentType) && type.getValue().equalsIgnoreCase(contentType)) {
					return type;
				}
			}
			return APPLICATION_OTHER;
		}
	}
	
	
	public enum KeyAgreementScheme {
		
		RSA_KEM("RSA_KEM");
//		, SMART_SENSE_ACUMEN("SmartSenseAcumen"), SMART_SENSE_GAUSS("SmartSenseGauss");
		
		private final String schemeName;

		KeyAgreementScheme(String value) {
			this.schemeName = value;
		}

		public String getKeyAgreementSchemeName() {
			return schemeName;
		}
		
		public static KeyAgreementScheme getKeyAgreementScheme(String keyAggrementSchemeName) {

			KeyAgreementScheme[] schemes = KeyAgreementScheme.values();

			for (KeyAgreementScheme type : schemes) {
				if (!StringUtils.isBlank(keyAggrementSchemeName) && type.getKeyAgreementSchemeName().equalsIgnoreCase(keyAggrementSchemeName)) {
					return type;
				}
			}
			return null;
		}
	}
	
	public enum ClientType {

		JAM("jam") {
			@Override
			public byte[] getPublicCertificate() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream(PostcardConstants.JAMC_CERT));
			}
			
			@Override
			public byte[] getPrivateKey() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream(PostcardConstants.JAMC_PRIVATEKEY_PEM));
			}

		}, DEVICE("device") {
			@Override
			public byte[] getPublicCertificate() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream(PostcardConstants.DEVICE_CERT));
			}
			
			@Override
			public byte[] getPrivateKey() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream(PostcardConstants.DEVICE_PRIVATEKEY_PEM));
			}

		};
		public abstract byte[] getPublicCertificate() throws IOException;
		
		public abstract byte[] getPrivateKey() throws IOException;
		
		private final String value;

		ClientType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		@JsonCreator
		public static ClientType getClientType(String clientType) {

			ClientType[] clientTypes = ClientType.values();

			for (ClientType type : clientTypes) {
				if (!StringUtils.isBlank(clientType) && type.getValue().equalsIgnoreCase(clientType)) {
					return type;
				}
			}
			return JAM;
		}
	}
	
	public enum EnvironmentType {

		DEV("dev") {
			@Override
			public byte[] getPublicCertificate() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream("certs/server/dev/server_cert.pem"));
			}
			
			@Override
			public byte[] getPublicKey() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream("certs/server/dev/server_publickey.pem"));
			}

		}, PIE("pie") {
			@Override
			public byte[] getPublicCertificate() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream("certs/server/pie/server_cert.pem"));
			}
			
			@Override
			public byte[] getPublicKey() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream("certs/server/pie/server_publickey.pem"));
			}

		}, STAGE("stage") {
			@Override
			public byte[] getPublicCertificate() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream("certs/server/stage/server_cert.pem"));
			}
			
			@Override
			public byte[] getPublicKey() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream("certs/server/stage/server_publickey.pem"));
			}

		}
		, PROD("prod_confidential") {
			@Override
			public byte[] getPublicCertificate() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream("certs/server/prod/server_cert.pem"));
			}
			
			@Override
			public byte[] getPublicKey() throws IOException {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				return ByteStreams.toByteArray(loader.getResourceAsStream("certs/server/prod/server_publickey.pem"));
			}

		}
		;
		
		public abstract byte[] getPublicCertificate() throws IOException;
		
		public abstract byte[] getPublicKey() throws IOException;
		
		private final String value;

		EnvironmentType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		@JsonCreator
		public static EnvironmentType getEnvironmentType(String environmentType) {

			EnvironmentType[] environmentTypes = EnvironmentType.values();

			for (EnvironmentType type : environmentTypes) {
				if (!StringUtils.isBlank(environmentType) && type.getValue().equalsIgnoreCase(environmentType)) {
					System.out.println("environmentType: " + type);
					return type;
				}
			}
			return PIE;
		}
	}

}
