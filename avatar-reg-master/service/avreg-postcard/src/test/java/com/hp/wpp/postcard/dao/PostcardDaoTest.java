/**
 * 
 */
package com.hp.wpp.postcard.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hp.wpp.postcard.common.PostcardEnums.KeyAgreementScheme;
import com.hp.wpp.postcard.entities.PostcardAdditionalInfoEntity;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.entities.PostcardRenegotiationInfoEntity;

/**
 * @author mahammad
 *
 */
@ContextConfiguration(locations = "/applicationContext-postcard-test.xml")
public class PostcardDaoTest extends AbstractTestNGSpringContextTests {
	
	private static final String APPLICATION_ID = "svs_connectivity";
	private byte[] ask = "ask".getBytes();
	private String deviceId = "deviceId";
	private int applicationId = 1;
	private String keyId = "keyId";
	
	@Autowired
	PostcardDao dao;
	
	@BeforeMethod
	public void setUp() {
		// load the entity for each method
		PostcardEntity postcardEntity = new PostcardEntity();
		postcardEntity.setEntityId(deviceId);
		postcardEntity.setKeyId(keyId);
		postcardEntity.setKeyAgreementScheme(KeyAgreementScheme.RSA_KEM.getKeyAgreementSchemeName());
		postcardEntity.setSecret(ask);
		dao.createPostcard(postcardEntity);
	}
	
	@AfterMethod
	public void tearDown() {
		// load the entity for each method
		PostcardEntity postcardEntity = dao.getPostcard(deviceId);
		if(postcardEntity != null) {
			dao.deletePostcard(postcardEntity);
		}
	}
	
	@Test
	public void test_GetPostcard() {
		PostcardEntity postcardEntity = dao.getPostcard(deviceId);
		Assert.assertEquals(postcardEntity.getSecret(), ask);
	}
	
	@Test
	public void test_UpdatePostcard() {
		byte[] askUpdated = "abc".getBytes();
		PostcardEntity postcardEntity = dao.getPostcard(deviceId);
		postcardEntity.setSecret(askUpdated);
		dao.updatePostcard(postcardEntity);
		Assert.assertEquals(dao.getPostcard(deviceId).getSecret(), askUpdated);
	}
	
	@Test
	public void test_getPostcard_WithNonExistingDevice_Id() {
		PostcardEntity postcardEntity = dao.getPostcard("printer_Id");
		Assert.assertNull(postcardEntity);
	}
	
	@Test
	public void test_DeletePostcard() {
		PostcardEntity postcardEntity = dao.getPostcard(deviceId);
		dao.deletePostcard(postcardEntity);
		Assert.assertNull(dao.getPostcard(deviceId));
	}
	
	@Test
	public void test_UpdatePostcardAdditionalInfo() {
		PostcardEntity postcardEntity = dao.getPostcard(deviceId);
		updatePostcardAdditionalInfo(postcardEntity);
		PostcardAdditionalInfoEntity postcardAdditionalInfoEntity = dao.getPostcardAdditionalInfo(postcardEntity, APPLICATION_ID);
		Assert.assertEquals(postcardAdditionalInfoEntity.getApplicationId(), APPLICATION_ID);
	}

	@Test
	public void test_getPostcardAdditionalInfo() {
		PostcardEntity postcardEntity = dao.getPostcard(deviceId);
		updatePostcardAdditionalInfo(postcardEntity);
		
		PostcardAdditionalInfoEntity postcardAdditionalInfoEntity = dao.getPostcardAdditionalInfo(postcardEntity, APPLICATION_ID);
		Assert.assertNotNull(postcardAdditionalInfoEntity);
	}
	
	@Test
	public void test_StorePostcardRenegotiationInfo() {
		PostcardEntity postcardEntity = dao.getPostcard(deviceId);
		updatePostcardAdditionalInfo(postcardEntity);
		
		dao.store(populatePostcardRenegotiationInfo(postcardEntity));
		List<PostcardRenegotiationInfoEntity> postcardRenegotiationInfoEntities = dao.getPostcardRenegotiationInfos(postcardEntity, APPLICATION_ID);
		Assert.assertNotNull(postcardRenegotiationInfoEntities);
	}
	
	private void updatePostcardAdditionalInfo(PostcardEntity postcardEntity) {
		PostcardAdditionalInfoEntity postcardAdditionalInfoEntity = new PostcardAdditionalInfoEntity();
		postcardAdditionalInfoEntity.setApplicationId(APPLICATION_ID);
		postcardAdditionalInfoEntity.setEntityInstruction("Instruction");
		postcardAdditionalInfoEntity.setEntityMessageId("entityMessageId");
		postcardAdditionalInfoEntity.setEntitySeqNum(0);
		postcardAdditionalInfoEntity.setEntitySignatureHash("entitySignatureHash");
		postcardAdditionalInfoEntity.setPostcardEntity(postcardEntity);
		postcardAdditionalInfoEntity.setServiceMessageId("serviceMessageId");
		postcardAdditionalInfoEntity.setServiceSeqNum(0);
		postcardAdditionalInfoEntity.setServiceSignatureHash("serviceSignatureHash");
		dao.updatePostcardAdditionalInfo(postcardAdditionalInfoEntity);
	}
	
	private PostcardRenegotiationInfoEntity populatePostcardRenegotiationInfo(PostcardEntity postcardEntity) {
		PostcardRenegotiationInfoEntity postcardRenogotiationInfoEntity = new PostcardRenegotiationInfoEntity();
		postcardRenogotiationInfoEntity.setApplicationId(APPLICATION_ID);
		postcardRenogotiationInfoEntity.setPostcardEntity(postcardEntity);
		String credentialRefreshPayload = "{ \"version\": 1.0, \"credential_refresh_reason\": \"application_key_mismatch\", \"application_id\": \"svc_connectivity\", \"firmware_version\": { \"revision\": \"PAL1358AR\", \"date\": \"01-01-1970\" } }";
		postcardRenogotiationInfoEntity.setCredentialRefreshInfo(credentialRefreshPayload.getBytes());
		return postcardRenogotiationInfoEntity;
	}

}
