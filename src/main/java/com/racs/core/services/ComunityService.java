package com.racs.core.services;

import java.util.List;

import com.racs.core.entities.ComunityEntity;


public interface ComunityService {
	
	Iterable<ComunityEntity> listAllComunyty();
	
	List<ComunityEntity> listAllComunity();
	
	ComunityEntity  getComunityById(Integer id);
	
	ComunityEntity saveComunity(ComunityEntity comunyty);
	
	void deleteComunity(Integer id);

	

}
