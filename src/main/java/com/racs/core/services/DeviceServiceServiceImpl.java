package com.racs.core.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.racs.core.entities.DeviceEntity;
import com.racs.core.repositories.DeviceRepository;



@Service
public class DeviceServiceServiceImpl  implements DeviceService{

	private DeviceRepository deviceRepository;
	
	@Autowired
	public void setComunityRepository(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}



	@Override
	public Iterable<DeviceEntity> listAllDevice() {
		// TODO Auto-generated method stub
		return deviceRepository.findAll();
	}

	@Override
	public DeviceEntity getDeviceById(Integer id) {
		// TODO Auto-generated method stub
		return deviceRepository.findOne(id);
	}

	@Override
	public DeviceEntity saveDevice(DeviceEntity device) {
		// TODO Auto-generated method stub
		return deviceRepository.save(device);
	}

	@Override
	public void deleteDevice(Integer id) {
		// TODO Auto-generated method stub
		deviceRepository.delete(id);
	}
	
	

}
