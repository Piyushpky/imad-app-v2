package com.hp.wpp.ssnc.common.util;

public class PrinterCodeBase32 {
	private PrinterCodeBase32(){};
	  private static final byte[] ENCODE_TABLE = {
	            'A', 'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
	            'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z',
	            '0','1','2', '3', '4', '5', '6', '7','8','9'
	    };
	  private static final byte[] DECODE_TABLE = new byte[128];
	  private static final byte PAD = '=';
	  
	  static {
		  for(int i = 0; i < ENCODE_TABLE.length; i++){
			  DECODE_TABLE[ENCODE_TABLE[i]] = (byte)i;
		  }
	  }
	  
	  public static String encode(byte[] input){
		  
		  // check if input length != 4
		  byte[] output = new byte[8];
		  output[0] = ENCODE_TABLE[(input[0] >> 3) & 0x1F];
		  output[1] = ENCODE_TABLE[((input[0] << 2) & 0x1F) | ((input[1] >> 6) & 0x03)];
		  output[2] = ENCODE_TABLE[(input[1] >> 1) & 0x1F];
		  output[3] = ENCODE_TABLE[((input[1] << 4) & 0x1F) | (input[2] >> 4)];
		  output[4] = ENCODE_TABLE[(((input[2] << 1) & 0x1F) | ((input[3] >> 7) & 0x01))];
		  output[5] = ENCODE_TABLE[(input[3] >> 2) & 0x1F];
		  output[6] = ENCODE_TABLE[((input[3] << 3) & 0x1F)];
		  output[7] = PAD;
		  return new String(output); 
	  }
	  
	  public static byte[] decode(String inputStr){
		  byte[] input = inputStr.getBytes();
		  byte[] output = new byte[4];
		  output[0] = (byte)((DECODE_TABLE[input[0]] << 3) | (DECODE_TABLE[input[1]] >> 2));
		  output[1] = (byte)((DECODE_TABLE[input[1]] << 6) | (DECODE_TABLE[input[2]] << 1) | (DECODE_TABLE[input[3]] >> 4));
		  output[2] = (byte)((DECODE_TABLE[input[3]] << 4) | (DECODE_TABLE[input[4]] >> 1));
		  output[3] = (byte)((DECODE_TABLE[input[4]] << 7) | (DECODE_TABLE[input[5]] << 2) | 0x00);
		  return output;
	  }
}
