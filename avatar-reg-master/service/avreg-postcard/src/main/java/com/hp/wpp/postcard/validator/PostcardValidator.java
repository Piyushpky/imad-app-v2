/**
 * 
 */
package com.hp.wpp.postcard.validator;

import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.common.PostcardEnums.Creator;
import com.hp.wpp.postcard.entities.PostcardEntity;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.json.schema.PostcardContent;

/**
 * @author mahammad
 *
 */
public interface PostcardValidator {
	
	public void validatePostcardAdditionalInfo(PostcardContent postcardContent, PostcardEntity postcardEntity) throws PostcardNonRetriableException;
	
	public void populateAndStorePostcardAdditionalInfo(PostcardEntity postcardEntity, PostcardContent postcardContent, Creator creator, boolean isSecretRefresh, String entityInstruction) throws PostcardNonRetriableException;
	
	public void storePostcardRenegotiationInfo(PostcardEntity postcardEntity, PostcardData postcardData) throws PostcardNonRetriableException;
}
