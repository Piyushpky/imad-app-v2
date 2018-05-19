package com.hp.wpp.ssnclaim.exception;

public class SSNNonRetriableException extends RuntimeException{
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	public SSNNonRetriableException(){
		super();
	}

	public SSNNonRetriableException(Throwable e){
		super(e);
	}
	
	public SSNNonRetriableException(String messg){
		super(messg);
	}

}
