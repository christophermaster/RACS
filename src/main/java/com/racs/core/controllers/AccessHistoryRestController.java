package com.racs.core.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.racs.commons.exception.ResponseErrorEnum;
import com.racs.core.entities.AccessHistoryEntity;
import com.racs.core.services.AccessHistoryService;
import com.racs.core.services.ComunityService;
import com.racs.core.services.OwnerService;

/**
 * Product controller.
 */
@RestController
@RequestMapping(value = "/acceso")
public class AccessHistoryRestController {

	private AccessHistoryService service;
	private OwnerService ownerService ;
	private ComunityService comunityService;

	@Autowired
	public void setAccessHistoryService(AccessHistoryService service) {
		this.service = service;
	}
	@Autowired
	public void setOwnerService(OwnerService ownerService) {
		this.ownerService = ownerService;
	}
	@Autowired
	public void setComunityService(ComunityService comunityService) {
		this.comunityService = comunityService;
	}

	@GetMapping("/listado")
	public ResponseEntity<?> findAll() {

		Iterable<AccessHistoryEntity> result = service.listAllAccessHistory();
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findByAccess(@PathVariable("id") Integer id) {
		return new ResponseEntity<>(service.getAccessHistoryById(id), HttpStatus.OK);
	}
	
	@GetMapping("/comunidad/{id}")
	public ResponseEntity<?> findByAccesCode(@PathVariable("id") Integer id) {
		return new ResponseEntity<>(service.findByCode(id), HttpStatus.OK);
	}

	@PostMapping("/guardar")
	public ResponseEntity<?> save(@RequestBody AccessHistoryEntity entity) {
		ResponseErrorEnum errorState;
		AccessHistoryEntity accessHistoryEntity;

		if (entity == null) {
			
			errorState = ResponseErrorEnum.NULL_ENTITY;
			
		}else {
			
			entity.setOwnerEntity(ownerService.getOwnerById(entity.getIdOwner())); 
			entity.setComunityEntity(comunityService.getComunityById(entity.getCom_id()));
			accessHistoryEntity = service.saveAccessHistory(entity);
			return new ResponseEntity<>(accessHistoryEntity, HttpStatus.CREATED);
			
		}
		return errorState.asErrorResponse();
	}

}
