/**
 * 
 */
package com.hp.wpp.postcard.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;

/**
 * @author mahammad
 *
 */
public class ZipCompressor implements Compresssor {
	
	private static final WPPLogger LOG = WPPLoggerFactory.getLogger(ZipCompressor.class);

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.compression.Compresssor#compressByteArray(byte[])
	 */
	@Override
	public byte[] compressByteArray(byte[] bytes) throws IOException {
		LOG.debug("Inside Zip compressor.");
		Deflater deflater = new Deflater();
		deflater.setInput(bytes);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);

		deflater.finish();
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();

		return output;
	}

	/* (non-Javadoc)
	 * @see com.hp.wpp.postcard.compression.Compresssor#uncompressByteArray(byte[])
	 */
	@Override
	public byte[] uncompressByteArray(byte[] bytes) throws IOException {
		LOG.debug("Inside Zip decompressor.");
		Inflater inflater = new Inflater();
		inflater.setInput(bytes);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count;
				count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
		} catch (DataFormatException e) {
			LOG.error("DataFormatException while decompress: ", e);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
		LOG.debug("Original: " + bytes.length);
		LOG.debug("Compressed: " + output.length);
		return output;
	}

}
