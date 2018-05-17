/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import java.security.Security;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

/**
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
public class DKGeneratorTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private DKGenerator dkGenerator;
	
	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@DataProvider(name = "dkGenerator")
	public Object[][]  dataProviderDKGenerator() throws Exception {
		// ask in hex format
		String inputASK = "b1a2bb2c98ea10ead9024655a5d43d05";
		// postcardId in hex format
		String inputPostcardId = "3416e0e95daee6e092b94c76d6b2c99c26d04ada7005fe50b3d9e6d9c9291408";
		// dk in hex format
		String expectedDK = "5F5FB1DC95D9AC89E27EC48B0FA676A27E8069E6E8717A5E5F1A9FD706E20AB1FE9858FBC03875ABCFD83AEEA7BAEFE4";
		
		return new Object[][] { { inputASK, inputPostcardId, expectedDK } };
	}
	
	@Test(dataProvider = "dkGenerator", groups = "postcard")
	public void generateDK(String inputASK, String inputPostcardId, String expectedDK) throws PostcardNonRetriableException {
		byte[] actualDK = dkGenerator.generateDK(hexStringToByteArray(inputASK), hexStringToByteArray(inputPostcardId));
		Assert.assertEquals(actualDK, hexStringToByteArray(expectedDK));
	}
	
	@Test(expectedExceptions = { PostcardNonRetriableException.class }, groups = "postcard")
	public void testGenerateDKWithInvalidValues() throws PostcardNonRetriableException {
		byte[] actualDK = dkGenerator.generateDK(null, null);
	}
	
	@Test(dataProvider = "dkGenerator", groups = "postcard")
	public void testGenerateDKWithDifferentSecurityProvider(String inputASK, String inputPostcardId, String expectedDK) throws PostcardNonRetriableException {
		Security.removeProvider("BC");
		Security.addProvider(Security.getProvider("SUN"));
		byte[] actualDK = dkGenerator.generateDK(hexStringToByteArray(inputASK), hexStringToByteArray(inputPostcardId));
	}
	
	private static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
