/**
 * 
 */
package com.hp.wpp.postcard.compression;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author mahammad
 *
 */
public class ZipCompressorTest {
	
	private Compresssor compresssor;
	
	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		compresssor = new ZipCompressor();
	}
	
	@DataProvider(name = "compress")
	public Object[][]  dataProviderCompress() throws Exception {
		
		String input = "compress test...";
		String compressedContent = "eJxLzs8tKEotLlYoSS0u0dPTAwA2TAXX";
		
		
		return new Object[][] { { input.getBytes(), Base64.decodeBase64(compressedContent) } };
	}
	
	@DataProvider(name = "uncompress")
	public Object[][]  dataProviderunCompress() throws Exception {
		
		String input = "compress test...";
		String compressedContent = "eJxLzs8tKEotLlYoSS0u0dPTAwA2TAXX";
		
		return new Object[][] { { Base64.decodeBase64(compressedContent), input.getBytes() } };
	}
	
	@Test(dataProvider = "compress", groups = "postcard")
	public void compress(byte[] input, byte[] expectedValue) throws IOException {
		byte[] actualValue = compresssor.compressByteArray(input);
		Assert.assertEquals(actualValue, expectedValue);
	}
	
	@Test(dataProvider = "uncompress", groups = "postcard")
	public void unCompress(byte[] input, byte[] expectedValue) throws IOException {
		byte[] actualValue = compresssor.uncompressByteArray(input);
		Assert.assertEquals(actualValue, expectedValue);
	}
}
