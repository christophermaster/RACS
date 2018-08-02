package com.racs.core.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.racs.core.entities.ComunityEntity;
import com.racs.core.services.ComunityService;

/**
 * Product controller.
 */
@RestController
@RequestMapping(value = "/comunidad")
public class ComunityRestController {


	private ComunityService service;

    
    @Autowired
    public void setComunityService(ComunityService service) {
        this.service = service;
    }


    @GetMapping("/listado")
    public ResponseEntity<?> findAll() {
 
        Iterable<ComunityEntity> result = service.listAllComunity();
        return new ResponseEntity<>(result,  HttpStatus.OK);
        
    }
 

}
