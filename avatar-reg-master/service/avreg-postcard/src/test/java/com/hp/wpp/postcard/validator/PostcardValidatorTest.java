/**
 * 
 */
package com.hp.wpp.postcard.validator;

import java.math.BigInteger;

import org.apache.commons.codec.binary.Base64;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hp.wpp.postcard.common.PostcardConfig;
import com.hp.wpp.postcard.dao.PostcardDao;
import com.hp.wpp.postcard.entities.PostcardAdditionalInfoEntity;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardContent;
import com.hp.wpp.postcard.json.schema.PostcardContent.PostcardSignedInfo;
import com.hp.wpp.postcard.json.schema.PostcardCreator;

/**
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
public class PostcardValidatorTest extends AbstractTestNGSpringContextTests {
	
	// base64 encoded ask
	private String ask = "PHhtbD48bmFtZT50ZXN0PC9uYW1lPjwveG1sPg==";
	String entityId = "seoupthe3pk";
	String keyId = "key_id-1";
	String applicationId = "1";
	
	@InjectMocks
	@Autowired
	private PostcardValidator postcardValidator;
	@Autowired
	private PostcardDao postcardDao;
	@Mock
	private PostcardConfig postcardConfig;
	
	@BeforeMethod
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testValidateSequenceNumber_HappyPath() throws PostcardNonRetriableException {
		PostcardContent postcardContent = new PostcardContent();
		PostcardSignedInfo postcardSignedInfo = new PostcardSignedInfo();
		postcardSignedInfo.setSeqNum(new BigInteger("2"));
		postcardSignedInfo.setApplicationId(applicationId);
		postcardSignedInfo.setCreator(PostcardCreator.entity);
		postcardContent.setPostcardSignedInfo(postcardSignedInfo);
		
		PostcardEntity postcardEntity = populatePostcardAndAdditionalInfo("seoupthe3pk");
		Mockito.when(postcardConfig.isEntitySeqNumValidationRequired()).thenReturn(true);
		postcardValidator.validatePostcardAdditionalInfo(postcardContent, postcardEntity);
	}
	
	@Test()
	public void testValidateSequenceNumber_WithSameSeqNumber() throws PostcardNonRetriableException {
		PostcardContent postcardContent = new PostcardContent();
		PostcardSignedInfo postcardSignedInfo = new PostcardSignedInfo();
		postcardSignedInfo.setSeqNum(new BigInteger("1"));
		postcardSignedInfo.setApplicationId(applicationId);
		postcardSignedInfo.setCreator(PostcardCreator.entity);
		postcardContent.setPostcardSignedInfo(postcardSignedInfo);
		
		PostcardEntity postcardEntity = populatePostcardAndAdditionalInfo("seoupthe3pk1");
		Mockito.when(postcardConfig.isEntitySeqNumValidationRequired()).thenReturn(true);
		postcardValidator.validatePostcardAdditionalInfo(postcardContent, postcardEntity);
	}
	
	private PostcardEntity populatePostcardAndAdditionalInfo(String entityId) {
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId(entityId);
		postcardEntity.setKeyId(keyId);
		postcardEntity.setSecret(Base64.decodeBase64(ask));
		postcardDao.createPostcard(postcardEntity);
		populatePostcardEntityAndInfo(postcardEntity);
		return postcardEntity;
	}
	
	private void populatePostcardEntityAndInfo(PostcardEntity postcardEntity) {
		PostcardAdditionalInfoEntity postcardAdditionalInfoEntity = new PostcardAdditionalInfoEntity();
		postcardAdditionalInfoEntity.setApplicationId(applicationId);
		postcardAdditionalInfoEntity.setEntityInstruction("Instruction");
		postcardAdditionalInfoEntity.setEntityMessageId("entityMessageId");
		postcardAdditionalInfoEntity.setEntitySeqNum(1);
		postcardAdditionalInfoEntity.setEntitySignatureHash("entitySignatureHash");
		postcardAdditionalInfoEntity.setPostcardEntity(postcardEntity);
		postcardAdditionalInfoEntity.setServiceMessageId("serviceMessageId");
		postcardAdditionalInfoEntity.setServiceSeqNum(1);
		postcardAdditionalInfoEntity.setServiceSignatureHash("serviceSignatureHash");
		postcardDao.updatePostcardAdditionalInfo(postcardAdditionalInfoEntity);
	}
}
