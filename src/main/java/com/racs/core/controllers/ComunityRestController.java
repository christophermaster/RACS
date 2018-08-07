package com.racs.core.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import com.racs.commons.exception.ResponseErrorEnum;
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
    @GetMapping("/{id}")
    public ResponseEntity<?> findByComunity(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.getComunityById(id),HttpStatus.OK);
    }
    
    @PostMapping("/guardar")
    public ResponseEntity<?> save(@RequestBody ComunityEntity entity) {
        ResponseErrorEnum errorState ;

        if (isNull(entity)) {
            errorState = ResponseErrorEnum.NULL_ENTITY;
        } else if (nonNull(entity.getId())) {
            errorState = ResponseErrorEnum.NON_NEW_ENTITY;
        } else {

            return new ResponseEntity<>(service.saveComunity(entity),HttpStatus.CREATED);
        }

        return errorState.asErrorResponse();
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> update(@RequestBody ComunityEntity entity) {
        ResponseErrorEnum errorState;

        if (isNull(entity)) {
            errorState = ResponseErrorEnum.NULL_ENTITY;
        } else if (isNull(entity.getId())) {
            errorState = ResponseErrorEnum.NULL_ID;
        } else {

            return new ResponseEntity<>(service.saveComunity(entity), HttpStatus.ACCEPTED);
        }

        return errorState.asErrorResponse();
    }
 

}
