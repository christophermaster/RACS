package com.racs.core.entities;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "VEHICLE", uniqueConstraints = @UniqueConstraint(columnNames ={"VEH_LICENCEPLATE"} ))
public class VehicleEntity implements Serializable{
	
	private static final long serialVersionUID = -5703889625848068724L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	@Column(name= "VEH_ID")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="OWN_ID")
	@JsonIgnore
    private OwnerEntity ownerEntity;
	
	
	@Column(name = "VEH_MAKE", length=30)
	private String makeVehicle;
	
	@Column(name = "VEH_MODEL", length=30)
	private String modelVehicle;
	
	
	@Column(name = "VEH_COLOUR", length=20)
	private String colourVehicle;
	
	@Column(name = "VEH_YEAR")
	private Long LongVehicle;
	
	@Column(name = "VEH_LICENCEPLATE", length=20, nullable = false)
	private String lecenseplateVehicle;

	public VehicleEntity() {

	}

	public VehicleEntity(Integer id, OwnerEntity ownerEntity, String makeVehicle, String modelVehicle,
			String colourVehicle, Long longVehicle, String lecenseplateVehicle) {
		super();
		this.id = id;
		this.ownerEntity = ownerEntity;
		this.makeVehicle = makeVehicle;
		this.modelVehicle = modelVehicle;
		this.colourVehicle = colourVehicle;
		LongVehicle = longVehicle;
		this.lecenseplateVehicle = lecenseplateVehicle;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OwnerEntity getOwnerEntity() {
		return ownerEntity;
	}

	public void setOwnerEntity(OwnerEntity ownerEntity) {
		this.ownerEntity = ownerEntity;
	}

	public String getMakeVehicle() {
		return makeVehicle;
	}

	public void setMakeVehicle(String makeVehicle) {
		this.makeVehicle = makeVehicle;
	}

	public String getModelVehicle() {
		return modelVehicle;
	}

	public void setModelVehicle(String modelVehicle) {
		this.modelVehicle = modelVehicle;
	}

	public String getColourVehicle() {
		return colourVehicle;
	}

	public void setColourVehicle(String colourVehicle) {
		this.colourVehicle = colourVehicle;
	}

	public Long getLongVehicle() {
		return LongVehicle;
	}

	public void setLongVehicle(Long longVehicle) {
		LongVehicle = longVehicle;
	}

	public String getLecenseplateVehicle() {
		return lecenseplateVehicle;
	}

	public void setLecenseplateVehicle(String lecenseplateVehicle) {
		this.lecenseplateVehicle = lecenseplateVehicle;
	}
	
}
