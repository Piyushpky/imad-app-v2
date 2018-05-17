/**
 * 
 */
package com.hp.wpp.postcard.key.generator;

import java.security.Security;

import org.apache.commons.lang.ArrayUtils;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.hp.wpp.postcard.common.PostcardConstants;
import com.hp.wpp.postcard.exception.EmptyCipherInputsException;
import com.hp.wpp.postcard.exception.PostcardNonRetriableException;
import com.hp.wpp.postcard.util.PostcardUtility;

/**
 * @author mahammad
 *
 */
public class LinearDKGenerator implements DKGenerator {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(LinearDKGenerator.class);
	
	static {
		// TODO find a way to change the Provider dynamically
		Security.addProvider(new BouncyCastleProvider());
	}

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.key.generator.DKGenerator#generateDK(byte[], byte[])
	 */
	@Override
	public byte[] generateDK(byte[] password, byte[] salt) throws PostcardNonRetriableException {
		
		if(ArrayUtils.isEmpty(password) || ArrayUtils.isEmpty(salt)) {
			throw new EmptyCipherInputsException("Either password or salt are invalid to generate DK");
		}
		
		byte[] key = null;
		PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
		gen.init(password, salt, PostcardConstants.DK_ITERATION_COUNT);
		// converting bytes to bits
		key = ((KeyParameter) gen.generateDerivedParameters((PostcardConstants.DERIVED_KEY_SIZE * Byte.SIZE))).getKey();
		LOG.debug("DK generated: {} of length: {} bytes", PostcardUtility.returnFormattedText(key), key.length);
		return key;
	}

}
