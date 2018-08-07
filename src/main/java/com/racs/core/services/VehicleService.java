package com.racs.core.services;

import java.util.List;

import com.racs.core.entities.VehicleEntity;

public interface VehicleService {
	
	Iterable<VehicleEntity> listAllVehicle();
	
	VehicleEntity  getVehicleById(Integer id);
	
	VehicleEntity saveVehicle(VehicleEntity vehicle);
	
	void deleteVehicle(Integer id);
	
	List<VehicleEntity> findAllVehicle();

}
