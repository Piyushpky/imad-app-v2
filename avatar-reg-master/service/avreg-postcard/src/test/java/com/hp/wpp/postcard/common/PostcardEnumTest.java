/**
 * 
 */
package com.hp.wpp.postcard.common;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.hp.wpp.postcard.common.PostcardEnums.ApplicationType;
import com.hp.wpp.postcard.common.PostcardEnums.Compression;
import com.hp.wpp.postcard.common.PostcardEnums.ContentType;
import com.hp.wpp.postcard.common.PostcardEnums.Encryption;
import com.hp.wpp.postcard.common.PostcardEnums.KeyAgreementScheme;

/**
 * @author mahammad
 *
 */
public class PostcardEnumTest {
	
	@Test
	public void testPostcardEnums() {
		Assert.assertEquals(ContentType.getContentType("abcd"), ContentType.APPLICATION_OTHER);
		Assert.assertEquals(ContentType.getContentType("application/json"), ContentType.APPLICATION_JSON);
		
		Assert.assertEquals(Compression.getCompression("abcd"), Compression.NONE);
		Assert.assertEquals(Compression.getCompression("gzip"), Compression.GZIP);
		
		Assert.assertEquals(Encryption.getEncryption("abcd"), Encryption.NONE);
		Assert.assertEquals(Encryption.getEncryption("aes_128"), Encryption.AES_128);
		
		Assert.assertEquals(ApplicationType.getApplicationType(0), ApplicationType.OTHER);
		Assert.assertEquals(ApplicationType.getApplicationType(1), ApplicationType.AVATAR_REGISTRATION);
		
		Assert.assertEquals(KeyAgreementScheme.getKeyAgreementScheme("abcd"), null);
		Assert.assertEquals(KeyAgreementScheme.getKeyAgreementScheme("RSA_KEM"), KeyAgreementScheme.RSA_KEM);
	}

}
