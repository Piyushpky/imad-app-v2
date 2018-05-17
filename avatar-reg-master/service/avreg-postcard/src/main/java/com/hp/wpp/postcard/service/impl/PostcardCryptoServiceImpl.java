/**
 * 
 */
package com.hp.wpp.postcard.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.cipher.PostcardCipher;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.common.PostcardEnums.ContentType;
import com.hp.wpp.postcard.exception.PostcardCompressionFailureException;
import com.hp.wpp.postcard.exception.PostcardDeCompressionFailureException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.exception.UnsupportedPostcardException;
import com.hp.wpp.postcard.json.schema.PostcardMessage;
import com.hp.wpp.postcard.service.PostcardCryptoService;
import com.hp.wpp.postcard.util.PostcardUtility;
import com.hp.wpp.postcard.version.factory.PostcardVersionStrategyFactory;

/**
 * @author mahammad
 *
 */
public class PostcardCryptoServiceImpl implements PostcardCryptoService {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardCryptoServiceImpl.class);
	
	@Autowired
	private PostcardCipher postcardCipher;
	@Autowired
	private PostcardVersionStrategyFactory postcardVersionFactory;

	@Override
	public PostcardData decryptAndDecompressMessageContents(String postcardVersion, List<PostcardMessage> messages, byte[] dk) throws PostcardNonRetriableException {
		PostcardData postcardData = new PostcardData();
		
		for (PostcardMessage message : messages) {
			byte[] iv = Base64.decodeBase64(message.getIv());
			if(!ArrayUtils.isEmpty(iv))
				LOG.debug("IV received in postcard: {} of size: {} bytes", PostcardUtility.returnFormattedText(iv), iv.length);
			
			String contentType = message.getContentType();
			String compressionType = message.getCompression().name();
			String encryptionAlgo = message.getEncryption().name();
			byte[] encryptedContent =  Base64.decodeBase64(message.getContent());
			
			byte[] decryptedContent = null;
			LOG.debug("Decrypting the message content with encryption/decryption algo: {}", encryptionAlgo);
			
			switch (message.getEncryption()) {
				case aes_128:
					decryptedContent = postcardCipher.decrypt(encryptedContent, getMEK(dk), iv);
					break;
				case none:
					break;
	
				default:
					throw new UnsupportedPostcardException("Unsupported encryption type received="+message.getEncryption());
			}
			
			byte[] content = null;
			LOG.debug("Uncompressing the message content with compressionType: {}", compressionType);
			try {
				content = postcardVersionFactory.getCompressionStrategy(postcardVersion).unCompress(decryptedContent, message.getCompression());

			} catch (IOException e) {
				throw new PostcardDeCompressionFailureException("IOException occured while decompressing the message content",e);
			}
			
			PostcardData.Message postcardMessage = new PostcardData().new Message();
			postcardMessage.setContentType(ContentType.getContentType(contentType));
			postcardMessage.setEncryption(message.getEncryption());
			postcardMessage.setCompression(message.getCompression());
			postcardMessage.setContent(content);
			postcardData.getMessages().add(postcardMessage);
		}
		return postcardData;
	}

	@Override
	public List<PostcardMessage> encryptAndCompressMessageContents(String postcardVersion, PostcardData postcardData, byte[] dk) throws PostcardNonRetriableException {
		List<PostcardMessage> postcardMessages = new ArrayList<>();
		List<PostcardData.Message> messages = postcardData.getMessages();
		
		for (PostcardData.Message message : messages) {
			byte[] iv = generateRandomBytes(PostcardConstants.IV_LENGTH);
			LOG.debug("IV received in postcard: {} of size: {} bytes", PostcardUtility.returnFormattedText(iv), iv.length);
			
			ContentType contentType = message.getContentType();
			
			byte[] content = message.getContent();
			byte[] compressedContent = null;
			LOG.debug("compressing the message content with compressionType: {}", message.getCompression());
			try {
				compressedContent = postcardVersionFactory.getCompressionStrategy(postcardVersion).compress(content, message.getCompression());
			} catch (IOException e) {
				throw new PostcardCompressionFailureException("IOException occured while compressing the message content",e);
			}
			
			LOG.debug("Encrypting the message content with encryption/decryption algo: {}", message.getEncryption());
			byte[] encryptedContent = null;
			switch (message.getEncryption()) {
				case aes_128:
					encryptedContent = postcardCipher.encrypt(compressedContent, getMEK(dk), iv);
					break;
				case none:
					break;
	
				default:
					throw new UnsupportedPostcardException("Unsupported encryption type received="+message.getEncryption());
			}
			
			PostcardMessage postcardMessage = new PostcardMessage();
			postcardMessage.setContentType(contentType.getValue());
			postcardMessage.setContent(Base64.encodeBase64String(encryptedContent));
			postcardMessage.setIv(Base64.encodeBase64String(iv));
			postcardMessage.setCompression(message.getCompression());
			postcardMessage.setEncryption(message.getEncryption());
			postcardMessages.add(postcardMessage);
		}
		return postcardMessages;
	}
	
	private byte[] generateRandomBytes(int size) {
		byte[] r = new byte[size];
		Random random = new Random();
		random.nextBytes(r);
		return r;
	}
	
	private byte[] getMEK(byte[] derivedKey) throws PostcardNonRetriableException {
		byte[] mek = new byte[PostcardConstants.MEK_LENGTH];
		mek = Arrays.copyOfRange(derivedKey, PostcardConstants.MAK_LENGTH, (PostcardConstants.MAK_LENGTH + PostcardConstants.MEK_LENGTH));
		LOG.debug("MEK generated: {} of size: {} bytes", PostcardUtility.returnFormattedText(mek), mek.length);
		return mek;
	}

	@Override
	public PostcardData decryptAndDecompressInstruction(String postcardVersion, PostcardMessage message, byte[] dk) throws PostcardNonRetriableException {
		List<PostcardMessage> messages = new ArrayList<>(1);
		messages.add(message);
		try {
			return decryptAndDecompressMessageContents(postcardVersion, messages, dk);
		} catch (PostcardNonRetriableException pe) {
			// as entity instruction is to for internal tracking purpose, we will ignore if any errors
			LOG.warning("PostcardException occured while decrypting entity instruction={}", pe.getMessage());
		}
		return null;
	}

}
