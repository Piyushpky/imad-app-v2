package com.hp.wpp.ssnclaim.exception;


/**
 * Root exception place holder for SSN Validation.
 * This exception is mapped with HTTP error status code:400 @ REST-API Resource layer. 
 *
 * @author srikanth potana
 *
 */

public class DeviceClaimRetriableException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DeviceClaimRetriableException(){
		super();
	}

	public DeviceClaimRetriableException(Throwable e){
		super(e);
	}
	
	public DeviceClaimRetriableException(String messg){
		super(messg);
	}
}
