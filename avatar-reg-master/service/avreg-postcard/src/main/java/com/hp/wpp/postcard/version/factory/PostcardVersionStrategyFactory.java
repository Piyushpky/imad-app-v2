/**
 * 
 */
package com.hp.wpp.postcard.version.factory;

import org.springframework.context.annotation.Bean;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.compression.CompressionStrategy;
import com.hp.wpp.postcard.compression.Postcard_1_0_CompressionStrategy;
import com.hp.wpp.postcard.compression.Postcard_1_1_CompressionStrategy;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.exception.UnsupportedPostcardException;
import com.hp.wpp.postcard.key.generator.Postcard_1_0_SignatureSchemeStrategy;
import com.hp.wpp.postcard.key.generator.Postcard_1_1_SignatureSchemeStrategy;
import com.hp.wpp.postcard.key.generator.SignatureSchemeStrategy;

/**
 * @author mahammad
 *
 */
public class PostcardVersionStrategyFactory {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(PostcardVersionStrategyFactory.class);
	
	public CompressionStrategy getCompressionStrategy(String postcardVersion) throws PostcardNonRetriableException {
		LOG.debug("postcard version received: {}", postcardVersion);
		switch (postcardVersion) {
			case "1.0":
				return postcard_1_0_CompressionStrategy();
			case "1.1":
				return postcard_1_1_CompressionStrategy();
	
			default:
				throw new UnsupportedPostcardException("Unsupported postcard version received="+ postcardVersion);
		}
	}
	
	public SignatureSchemeStrategy getSignatureSchemeStrategy(String postcardVersion) throws PostcardNonRetriableException {
		switch (postcardVersion) {
			case "1.0":
				return postcard_1_0_SignatuteSchemeStrategy();
			case "1.1":
				return postcard_1_1_SignatuteSchemeStrategy();
	
			default:
				throw new UnsupportedPostcardException("Unsupported postcard version received="+ postcardVersion);
		}
	}
	
	@Bean
	private CompressionStrategy postcard_1_1_CompressionStrategy() {
		return new Postcard_1_1_CompressionStrategy();
	}
	
	@Bean
	private CompressionStrategy postcard_1_0_CompressionStrategy() {
		return new Postcard_1_0_CompressionStrategy();
	}
	
	@Bean
	public SignatureSchemeStrategy postcard_1_1_SignatuteSchemeStrategy() {
		return new Postcard_1_1_SignatureSchemeStrategy();
	}
	
	@Bean
	public SignatureSchemeStrategy postcard_1_0_SignatuteSchemeStrategy() {
		return new Postcard_1_0_SignatureSchemeStrategy();
	}
}
