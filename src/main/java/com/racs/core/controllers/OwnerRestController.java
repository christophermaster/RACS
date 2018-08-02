package com.racs.core.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.racs.core.entities.OwnerEntity;
import com.racs.core.services.OwnerService;

/**
 * Product controller.
 */
@RestController
@RequestMapping(value = "/propietario")
public class OwnerRestController {

    private OwnerService service;
    
    @Autowired
    public void setOwnerService(OwnerService service) {
        this.service = service;
        
    }
    
    @GetMapping("/listado")
    public ResponseEntity<?> findAll() {
 
        Iterable<OwnerEntity> result = service.listAllOwner();
        return new ResponseEntity<>(result,  HttpStatus.OK);
        
    }

}
