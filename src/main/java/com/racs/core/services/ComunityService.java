package com.racs.core.services;

import com.racs.core.entities.ComunityEntity;

public interface ComunityService {
	
	Iterable<ComunityEntity> listAllComunyty();
	
	ComunityEntity  getComunityById(Integer id);
	
	ComunityEntity saveComunity(ComunityEntity comunyty);
	
	void deleteComunity(Integer id);

	

}
