package com.racs.core.services;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.racs.core.entities.AccessHistoryEntity;
import com.racs.core.entities.User;
import com.racs.core.repositories.AccessHistoryRepository;



@Service
public class AccessHistoryServiceServiceImpl  implements AccessHistoryService{

	private AccessHistoryRepository accessHistoryRepository;
	private AccessHistoryEntity model;
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	public void setComunityRepository(AccessHistoryRepository accessHistoryRepository) {
		this.accessHistoryRepository = accessHistoryRepository;
	}

	@Override
	public Iterable<AccessHistoryEntity> listAllAccessHistory() {

		return accessHistoryRepository.findAll();
	}

	@Override
	public AccessHistoryEntity getAccessHistoryById(Integer id) {

		return accessHistoryRepository.findOne(id);
	}

	@Override
	public AccessHistoryEntity saveAccessHistory(AccessHistoryEntity accessHistory) {
	
		return accessHistoryRepository.save(accessHistory);
	}

	@Override
	public void deleteAccessHistory(Integer id) {
		accessHistoryRepository.delete(id);
		
	}
	
	@Override
	public AccessHistoryEntity findByCode(Integer id){
		
		model = new AccessHistoryEntity();
		
		try {
			TypedQuery<AccessHistoryEntity> query = em.createQuery("Select ac.code FROM AccessHistoryEntity  ac where ac.comunityEntity = :id",
					AccessHistoryEntity.class);
			model = query.setParameter("id", id).setMaxResults(1).getSingleResult();
		} catch (Exception e) {
			System.err.println("Credenciales invalidas: " + model);
		}
		return model;
	}
	

}
