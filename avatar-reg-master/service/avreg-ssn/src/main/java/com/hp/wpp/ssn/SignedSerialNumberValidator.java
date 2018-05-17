package com.hp.wpp.ssn;

import com.hp.wpp.ssn.exception.InvalidSSNException;


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
 * @author srikanth potana
 *
 */

public interface SignedSerialNumberValidator {


	/**
	 *
	 * Validates the signature header using printer registration domain key and also validates issuance counter against DB recorded value, if available in DB.
	 *
	 * @param ssn  provided in registration payload (printer identification).
	 * @return true if validation is success. otherwise it throws an exceptions.
	 *
	 * <p><b>Throws the following run time exceptions</b>
	 * <p><i>  InvalidSSNSignatureException</i>
	 * <p><i>  InvalidSSNCounterException</i>
	 * <p><i>  InvalidSSNException</i>
	 * 
	 *
	 */

	public void validateAndPersistSSN(String ssn) throws InvalidSSNException;
	
	
	/**
	 * Validates the signature header using printer registration domain key and also validates issuance counter against DB recorded value, if available in DB.
	 * And also it performs validation on  SSN against registration payload's serial number attribute.
	 *
	 * @param ssn provided in registration payload (printer identification)
	 * @param serialNumber provided in registration payload (printer identification)
	 *
	 * <p><b>Throws the following run time exceptions</b>
	 * <p><i>  InvalidSSNSignatureException</i>
	 * <p><i>  InvalidSSNCounterException</i>
	 * <p><i>  InvalidSSNException</i>
	 * @throws InvalidSSNException 
	 *
	 */
	public void validateAndPersistSSN(String ssn,String serialNumber) throws InvalidSSNException;
	
	/**
	 * validate the signature header using printer registration domain key.
	 * @param ssn
	 * <p><b>Throws the following run time exceptions</b>
	 * <p><i>  InvalidSSNSignatureException</i>
	 * <p><i>  InvalidSSNException</i>
	 * @throws InvalidSSNException 
	 * 
	 */
	public void validateSSN(String ssn) throws InvalidSSNException;
	

}
