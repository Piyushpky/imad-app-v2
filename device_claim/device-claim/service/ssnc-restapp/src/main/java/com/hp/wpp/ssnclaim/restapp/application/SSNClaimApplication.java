package com.hp.wpp.ssnclaim.restapp.application;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.hp.wpp.ssnclaim.restapp.resources.PrinterCodeResource;
import com.hp.wpp.ssnclaim.restapp.resources.PrinterResource;
import com.hp.wpp.ssnclaim.restapp.resources.PrinterGenericInfoResource;
import com.hp.wpp.ssnclaim.restapp.resources.SNKeyLookUpResource;

public class SSNClaimApplication extends Application {
	
	private Set<Object> singletons = new HashSet();

	public SSNClaimApplication() {
		singletons.add(new PrinterCodeResource());
		singletons.add(new PrinterResource());
		singletons.add(new SNKeyLookUpResource());
		singletons.add(new PrinterGenericInfoResource());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
