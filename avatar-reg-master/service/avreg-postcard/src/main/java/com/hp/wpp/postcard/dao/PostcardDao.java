/**
 * 
 */
package com.hp.wpp.postcard.dao;

import java.util.List;

import com.hp.wpp.postcard.entities.PostcardAdditionalInfoEntity;
import com.hp.wpp.postcard.entities.PostcardCertificateInfoEntity;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.entities.PostcardRenegotiationInfoEntity;

/**
 * @author mahammad
 *
 */
public interface PostcardDao {
	
	/**
	 * persist the postcard key(ask) in database.
	 * 
	 * @param postcardKeyEntity
	 */
	public void createPostcard(PostcardEntity postcardEntity);
	
	/**
	 * update the postcard key(ask) in database.
	 * 
	 * @param postcardKeyEntity
	 */
	public void updatePostcard(PostcardEntity postcardEntity);
	
	/**
	 * fetch the postcard key(ask) from database based on entity uuid
	 * 
	 * @param entityId
	 * @return
	 */
	public PostcardEntity getPostcard(String entityId);
	
	/**
	 * Deletes the postcard entry.
	 * 
	 * @param postcardEntity
	 */
	public void deletePostcard(PostcardEntity postcardEntity);
	
	public PostcardAdditionalInfoEntity getPostcardAdditionalInfo(PostcardEntity postcardEntity, String applicationId);
	
	public void updatePostcardAdditionalInfo(PostcardAdditionalInfoEntity postcardAdditionalInfoEntity);
	
	public void store(PostcardRenegotiationInfoEntity postcardRenegotiationInfoEntity);
	
	public List<PostcardRenegotiationInfoEntity> getPostcardRenegotiationInfos(PostcardEntity postcardEntity, String applicationId);
	
	public PostcardCertificateInfoEntity getPostcardCertificateInfo(String publicKeySerialNumber);
}
