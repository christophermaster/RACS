package com.racs.core.services;

import com.racs.core.entities.OwnershipEntity;

public interface OwnershipService {
	
	Iterable<OwnershipEntity> listAllOwner();
	
	OwnershipEntity  getOwnerById(Integer id);
	
	OwnershipEntity saveOwner(OwnershipEntity owner);
	
	void deleteOwner(Integer id);
	

}
