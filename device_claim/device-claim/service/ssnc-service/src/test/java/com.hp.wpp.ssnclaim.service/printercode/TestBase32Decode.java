
package com.hp.wpp.ssnclaim.service.printercode ;

import java.util.Arrays;

import com.hp.wpp.ssnc.common.util.PrinterCodeBase32;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestBase32Decode {

	/* * header w/ signature and mask: f29b1fac
	 * Printer Code => 8MQV 9NIO 0123 4567*/



	
	@Test
	public void testBase32()
	{
		String input = "8MQV9N";
		byte[] actualOutput = PrinterCodeBase32.decode(input);
		byte[] expectedOutput = new byte[]{(byte) 0xf2,(byte) 0x9b,0x1f,(byte) 0xac};
		Assert.assertTrue(Arrays.equals(expectedOutput, actualOutput));
	}
	
	@Test
	public void testEcodeBase32()
	{
		byte[] input = new byte[]{(byte) 0xf2,(byte) 0x9b,0x1f,(byte) 0xac};
		String output = PrinterCodeBase32.encode(input);
		Assert.assertEquals(output, "8MQV9NA=");
	}
}

