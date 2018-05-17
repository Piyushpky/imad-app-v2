/**
 * 
 */
package com.hp.wpp.postcard.instruction;

import com.hp.wpp.postcard.PostcardData;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;

/**
 * @author mahammad
 *
 */
public interface PostcardInstructionGenerator {
	
	public String generateServiceInstruction(PostcardData postcardData) throws PostcardNonRetriableException;

}
