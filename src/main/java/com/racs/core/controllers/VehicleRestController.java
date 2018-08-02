package com.racs.core.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.racs.core.entities.VehicleEntity;
import com.racs.core.services.OwnerService;
import com.racs.core.services.VehicleService;

/**
 * Product controller.
 */
@RestController
@RequestMapping(value = "/vehiculo")
public class VehicleRestController {

    private VehicleService service;
    private OwnerService ownerService;


    @Autowired
    public void setVehicleService(VehicleService service) {
        this.service = service;
        
    }
    
    @Autowired
	public void setOwnerService(OwnerService ownerService) {
		this.ownerService = ownerService;
	}


	@GetMapping("/listado")
    public ResponseEntity<?> findAll() {
 
        Iterable<VehicleEntity> result = service.listAllVehicle();
        return new ResponseEntity<>(result,  HttpStatus.OK);
        
    }


}
