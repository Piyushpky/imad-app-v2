package com.hp.wpp.ssnclaim.service.printercode;

import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;

import java.util.Map;

public interface PrinterCode {
	/**
	 *
	 * Validates the signature header using printer registration domain key and
	 * also validates issuance counter against DB recorded value, if available
	 * in DB.
	 *
	 *
	 *            provided as part of claim.
	 * @return true if validation is success. otherwise it throws an exceptions.
	 *
	 *         <p>
	 *         <b>Throws the following run time exceptions</b>
	 *         <p>
	 *         <i> InvalidPrinterCodeSignatureException</i>
	 *         <p>
	 *         <i> InvalidPrinterCodeCounterException</i>
	 *         <p>
	 *         <i> InvalidPrinterCodeException</i>
	 * 
	 *
	 */

	public PrinterCodeData validatePrinterCode(String printerCode,
											   Map<Integer, String> domainMap) ;

}
