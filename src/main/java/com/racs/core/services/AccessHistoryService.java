package com.racs.core.services;

import com.racs.core.entities.AccessHistoryEntity;

public interface AccessHistoryService {
	
	Iterable<AccessHistoryEntity> listAllAccessHistory();
	
	AccessHistoryEntity  getAccessHistoryById(Integer id);
	
	AccessHistoryEntity saveAccessHistory(AccessHistoryEntity accessHistory);
	
	void deleteAccessHistory(Integer id);

	

}
