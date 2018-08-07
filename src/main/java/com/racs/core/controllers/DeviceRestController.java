package com.racs.core.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.racs.commons.bean.Notification;
import com.racs.core.entities.AccessHistoryEntity;
import com.racs.core.entities.ComunityEntity;
import com.racs.core.entities.DeviceEntity;
import com.racs.core.services.ComunityService;
import com.racs.core.services.DeviceService;

/**
 * Product controller.
 */
@RestController
@RequestMapping(value = "/dispositivo")
public class DeviceRestController {

    private DeviceService service;

    
    @Autowired
    public void setDeviceService(DeviceService service) {
        this.service = service;
    }


    @GetMapping("/listado")
    public ResponseEntity<?> findAll() {
 
        Iterable<DeviceEntity> result = service.listAllDevice();
        return new ResponseEntity<>(result,  HttpStatus.OK);
        
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findByComunity(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.getDeviceById(id),HttpStatus.OK);
    }

}
