package com.racs.core.controllers;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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

/**
 * Product controller.
 */
@RestController
@RequestMapping(value = "/acceso")
public class AccessHistoryRestController {

	private AccessHistoryService service;

	@Autowired
	public void setAccessHistoryService(AccessHistoryService service) {
		this.service = service;
	}

	@GetMapping("/listado")
	public ResponseEntity<?> findAll() {

		Iterable<AccessHistoryEntity> result = service.listAllAccessHistory();
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findByComunity(@PathVariable("id") Integer id) {
		return new ResponseEntity<>(service.getAccessHistoryById(id), HttpStatus.OK);
	}

	@PostMapping("/guardar")
	public ResponseEntity<?> save(@RequestBody AccessHistoryEntity entity) {
		ResponseErrorEnum errorState;
		AccessHistoryEntity accessHistoryEntity;

		if (isNull(entity)) {
			errorState = ResponseErrorEnum.NULL_ENTITY;
		} else if (nonNull(entity.getId())) {
			errorState = ResponseErrorEnum.NON_NEW_ENTITY;
		} else {
			accessHistoryEntity = service.saveAccessHistory(entity);
			return new ResponseEntity<>(accessHistoryEntity, HttpStatus.CREATED);
		}
		return errorState.asErrorResponse();
	}

}
