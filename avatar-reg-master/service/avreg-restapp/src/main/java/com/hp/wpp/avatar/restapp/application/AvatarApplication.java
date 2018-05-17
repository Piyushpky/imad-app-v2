/**
 * 
 */
package com.hp.wpp.avatar.restapp.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.hp.wpp.avatar.restapp.resources.EntityResource;
import com.hp.wpp.avatar.restapp.resources.ResetCredentialResource;


public class AvatarApplication extends Application {
	
	private Set<Object> singletons = new HashSet<Object>();

	public AvatarApplication() {
		singletons.add(new EntityResource());
		singletons.add(new ResetCredentialResource());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
