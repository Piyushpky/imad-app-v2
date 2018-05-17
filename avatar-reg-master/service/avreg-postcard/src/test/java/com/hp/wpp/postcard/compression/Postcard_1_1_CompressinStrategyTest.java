/**
 * 
 */
package com.hp.wpp.postcard.compression;

import org.apache.commons.codec.binary.Base64;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.postcard.json.schema.PostcardCompression;

/**
 * @author mahammad
 *
 */
public class Postcard_1_1_CompressinStrategyTest {
	
	private Postcard_1_1_CompressionStrategy postcard_1_1_CompressionStrategy;
	
	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		postcard_1_1_CompressionStrategy = new Postcard_1_1_CompressionStrategy();
	}
	
	@DataProvider(name = "compress")
	public Object[][]  dataProviderCompress() throws Exception {
		
		String input = "compress test...";
		String compressedContent = "eJxLzs8tKEotLlYoSS0u0dPTAwA2TAXX";
		
		String input1 = "Hello world...";
		String compressedContent1 = "H4sIAAAAAAAAAPNIzcnJVyjPL8pJ0dPTAwA7nAn1DgAAAA==";
		
		return new Object[][] { { PostcardCompression.gzip, input.getBytes(), Base64.decodeBase64(compressedContent) },
								{ PostcardCompression.deflate, input.getBytes(), Base64.decodeBase64(compressedContent) },
								{ PostcardCompression.http_gzip, input1.getBytes(), Base64.decodeBase64(compressedContent1) },
								{ PostcardCompression.none, input.getBytes(), input.getBytes() } };
	}
	
	@DataProvider(name = "uncompress")
	public Object[][]  dataProviderUnCompress() throws Exception {
		
		String input = "compress test...";
		String compressedContent = "eJxLzs8tKEotLlYoSS0u0dPTAwA2TAXX";
		
		return new Object[][] { { PostcardCompression.gzip, Base64.decodeBase64(compressedContent), input.getBytes() },
								{ PostcardCompression.none, input.getBytes(), input.getBytes() } };
	}
	
	@Test(dataProvider = "compress", groups = "postcard")
	public void compress(PostcardCompression postcardCompression, byte[] input, byte[] expectedValue) throws Exception {
		byte[] actualValue = postcard_1_1_CompressionStrategy.compress(input, postcardCompression);
		Assert.assertEquals(Base64.encodeBase64String(actualValue), Base64.encodeBase64String(expectedValue));
	}
	
	@Test(dataProvider = "uncompress", groups = "postcard")
	public void unCompress(PostcardCompression postcardCompression, byte[] input, byte[] expectedValue) throws Exception {
		byte[] actualValue = postcard_1_1_CompressionStrategy.unCompress(input, postcardCompression);
		Assert.assertEquals(Base64.encodeBase64String(actualValue), Base64.encodeBase64String(expectedValue));
	}
}
