package com.hp.wpp.ssnclaim.service.ssn.service;

import com.hp.wpp.ssnclaim.entities.PrinterDataEntity;
import com.hp.wpp.ssnclaim.restmodel.json.schema.ClaimInfo;
import com.hp.wpp.ssnclaim.restmodel.json.schema.RegisterPrinter;
import com.hp.wpp.ssnclaim.restmodel.json.schema.ResponseUUID;
import com.hp.wpp.ssnclaim.service.printercode.data.PrinterCodeData;
import com.hp.wpp.ssnclaim.service.ssn.data.SSNFields;


/**
 *
 * Helps to validate the SSN provided by printer
 * <p>
 *	1. Validate registration payload attribute serial-number against SSN's serial-number.
 *<p>
 *	2. Basic signature header validation using printer registration domain.
 *<p>
 *	3. Validate input issuance counter value against cloud DB stored counter.
 *<p>
 *
 *
 * @author vran
 *
 */

public interface DeviceLookUpService {
	void deletePrinterCodeData(PrinterCodeData printerCodeData);

	public PrinterCodeData validatePrinterCode(String code) ;

	public SSNFields validateSSNCode(String code) ;

	public PrinterDataEntity processPrinterCodeData(PrinterCodeData ssnFields);

	public void processPrinter(RegisterPrinter regDeviceClaim) ;


	public PrinterDataEntity snKeyValidation(String snKey) ;

	PrinterDataEntity getPrinterCodeData(String printerId, String claimCode);

	public  PrinterDataEntity getPrinterCodeData (PrinterCodeData printerCodeFields);

	public ClaimInfo createClaimResponse(String printerId) ;

	public ResponseUUID getPrinterInfo(String uuid);

}
