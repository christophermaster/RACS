package com.racs.core.controllers;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 
		List<VehicleEntity> result = service.findAllVehicle();
        return new ResponseEntity<>(result,  HttpStatus.OK);
        
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findByVehicle(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.getVehicleById(id),HttpStatus.OK);
    }


}
