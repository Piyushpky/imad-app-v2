/**
 * 
 */
package com.hp.wpp.postcard.compression;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.common.PostcardEnums.Compression;
import com.hp.wpp.postcard.exception.PostcardCompressionMismatchException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardCompression;

/**
 * @author mahammad
 *
 */
@Component
public class Postcard_1_1_CompressionStrategy implements CompressionStrategy {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(Postcard_1_1_CompressionStrategy.class);

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.compression.CompressionStrategy#compress(byte[], com.hp.wpp.postcard.json.schema.PostcardCompression)
	 */
	@Override
	public byte[] compress(byte[] data, PostcardCompression postcardCompression) throws IOException, PostcardNonRetriableException {
		switch (postcardCompression) {
			case gzip:
				return Compression.GZIP.compress(data);
			case none:
				return Compression.NONE.compress(data);
			case deflate:
				return Compression.DEFLATE.compress(data);
			case http_gzip:
				return Compression.HTTP_GZIP.compress(data);
	
			default:
				throw new PostcardCompressionMismatchException("Unsupported compression type received="+postcardCompression+", for the requested postcard version");
		}
	}

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.compression.CompressionStrategy#unCompress(byte[], com.hp.wpp.postcard.json.schema.PostcardCompression)
	 */
	@Override
	public byte[] unCompress(byte[] data, PostcardCompression postcardCompression) throws IOException, PostcardNonRetriableException {
		switch (postcardCompression) {
		case gzip:
			return Compression.GZIP.unCompress(data);
		case none:
			return Compression.NONE.unCompress(data);
		case deflate:
			return Compression.DEFLATE.unCompress(data);
		case http_gzip:
			return Compression.HTTP_GZIP.unCompress(data);

		default:
			throw new PostcardCompressionMismatchException("Unsupported compression type received="+postcardCompression+", for the requested postcard version");
	}
	}

}
