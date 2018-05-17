/**
 * 
 */
package com.hp.wpp.postcard.compression;

import java.io.IOException;

/**
 * @author mahammad
 *
 */
public interface Compresssor {
	
	public byte[] compressByteArray(byte[] bytes) throws IOException;
	
	public byte[] uncompressByteArray(byte[] bytes) throws IOException;

}
