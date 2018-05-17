package com.hp.wpp.avatar.registration.device.repository;

import org.springframework.dao.DataIntegrityViolationException;

import com.hp.wpp.avatar.registration.device.entities.EntityDevice;
import com.hp.wpp.avatar.registration.device.entities.HashedEntityIdentifier;

public interface EntityDeviceDao {

	public void persistEntityDevices(EntityDevice device);

	public void persistHashedIdentifier(HashedEntityIdentifier entityIdentifier);
}
