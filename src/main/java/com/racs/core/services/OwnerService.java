package com.racs.core.services;

import com.racs.core.entities.OwnerEntity;

public interface OwnerService {
	
	Iterable<OwnerEntity> listAllOwner();
	
	OwnerEntity  getOwnerById(Integer id);
	
	OwnerEntity saveOwner(OwnerEntity owner);
	
	void deleteOwner(Integer id);
	

}
