package com.racs.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.racs.core.entities.OwnershipEntity;
import com.racs.core.services.OwnerService;
import com.racs.core.services.OwnershipService;

/**
 * Product controller.
 */
@RestController
@RequestMapping(value = "/propiedad")
public class OwnershipRestController {

    private OwnershipService service;
    private OwnerService ownerService;

    
    @Autowired
	public void setService(OwnershipService service) {
		this.service = service;
	}



    @Autowired
	public void setOwnerService(OwnerService ownerService) {
		this.ownerService = ownerService;
	}




	@GetMapping("/listado")
    public ResponseEntity<?> findAll() {
 
        Iterable<OwnershipEntity> result = service.listAllOwner();
        return new ResponseEntity<>(result,  HttpStatus.OK);
        
    }
	
    @GetMapping("/{id}")
    public ResponseEntity<?> findByOwnerShip(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.getOwnerById(id),HttpStatus.OK);
    }
  

}
