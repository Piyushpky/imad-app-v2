package com.hp.wpp.avatar.registration.service.repository;

import com.hp.wpp.avatar.registration.service.entities.EntityService;
import com.hp.wpp.avatar.registration.service.entities.HashedEntityIdentifier;

public interface EntityServiceDao {

	public void persistEntityServices(EntityService service, HashedEntityIdentifier entityIdentifier);
}
