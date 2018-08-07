package com.racs.core.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.racs.core.entities.User;
import com.racs.core.entities.VehicleEntity;
import com.racs.core.repositories.VehicleRepository;

@Service
public class VehicleServiceImpl implements VehicleService {

	private VehicleRepository vehicleRepository;
	private List<VehicleEntity> vehicle;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	public void setOwnershipRepository(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public Iterable<VehicleEntity> listAllVehicle() {
		// TODO Auto-generated method stub
		return vehicleRepository.findAll();
	}

	@Override
	public VehicleEntity getVehicleById(Integer id) {
		// TODO Auto-generated method stub
		return vehicleRepository.findOne(id);
	}

	@Override
	public VehicleEntity saveVehicle(VehicleEntity vehicle) {
		// TODO Auto-generated method stub
		return vehicleRepository.save(vehicle);
	}

	@Override
	public void deleteVehicle(Integer id) {
		vehicleRepository.delete(id);

	}

	@Override
	public List<VehicleEntity> findAllVehicle() {
		vehicle = new ArrayList<VehicleEntity>();
		try {
			TypedQuery<VehicleEntity> query = em.createQuery("SELECT v FROM VehicleEntity v", VehicleEntity.class);

			vehicle = query.getResultList();
		} catch (Exception e) {
			System.err.println("Credenciales invalidas: ");
		}
		return vehicle;
	}

}
