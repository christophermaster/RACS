package com.racs.core.services;

import com.racs.core.entities.DeviceEntity;

public interface DeviceService {
	
	Iterable<DeviceEntity> listAllDevice();
	
	DeviceEntity  getDeviceById(Integer id);
	
	DeviceEntity saveDevice(DeviceEntity accessHistory);
	
	void deleteDevice(Integer id);

	

}
