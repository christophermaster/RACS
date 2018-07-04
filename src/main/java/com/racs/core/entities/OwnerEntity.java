package com.racs.core.entities;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.context.annotation.Bean;



@Entity
@Table(name = "OWNER")
public class OwnerEntity implements Serializable {
	
	private static final long serialVersionUID = -6703889625848068724L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	@Column(name = "OWN_id")
	private Integer id;
	
		
	@Column(name = "OWN_NAME", length=50)
	private String nameOwner;
	
	@Column(name = "OWN_LASNAME", length=50)
	private String lastNameOwner;
	
	@Column(name = "OWN_IDENTIFICATION_NUMBER")
	private Integer identificationNumberOwner;
	
	@Column(name ="OWN_TYPE_IDENTIFICATION" , length=2)
	private String typeIdentificationNumberOwner;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="COM_ID")
	private ComunityEntity comunityEntity;

	@OneToMany(mappedBy = "ownerEntity", cascade = CascadeType.ALL)
	private List<VehicleEntity> listVehicle = new ArrayList<>();
	
	@OneToMany(mappedBy = "ownerEntity", cascade = CascadeType.ALL)
	private List<AccessHistoryEntity> accessHistoryEntity = new ArrayList<>();
	
	@OneToMany(mappedBy = "ownerEntity", cascade = CascadeType.ALL)
	private List<OwnershipEntity> listOwnership = new ArrayList<>();

	



	public OwnerEntity() {

	}

	public OwnerEntity(Integer id, List<ComunityEntity> comunityEntity, String nameOwner, String lastNameOwner,
			Integer identificationNumberOwner, String typeIdentificationNumberOwner, String typeOwner) {
		super();
		this.id = id;
		this.nameOwner = nameOwner;
		this.lastNameOwner = lastNameOwner;
		this.identificationNumberOwner = identificationNumberOwner;
		this.typeIdentificationNumberOwner = typeIdentificationNumberOwner;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNameOwner() {
		return nameOwner;
	}

	public void setNameOwner(String nameOwner) {
		this.nameOwner = nameOwner;
	}

	public String getLastNameOwner() {
		return lastNameOwner;
	}

	public void setLastNameOwner(String lastNameOwner) {
		this.lastNameOwner = lastNameOwner;
	}

	public Integer getIdentificationNumberOwner() {
		return identificationNumberOwner;
	}

	public void setIdentificationNumberOwner(Integer identificationNumberOwner) {
		this.identificationNumberOwner = identificationNumberOwner;
	}

	public String getTypeIdentificationNumberOwner() {
		return typeIdentificationNumberOwner;
	}

	public void setTypeIdentificationNumberOwner(String typeIdentificationNumberOwner) {
		this.typeIdentificationNumberOwner = typeIdentificationNumberOwner;
	}

	public ComunityEntity getComunityEntity() {
		return comunityEntity;
	}

	public void setComunityEntity(ComunityEntity comunityEntity) {
		this.comunityEntity = comunityEntity;
	}

	public List<VehicleEntity> getListVehicle() {
		return listVehicle;
	}

	public void setListVehicle(List<VehicleEntity> listVehicle) {
		this.listVehicle = listVehicle;
	}

	public List<OwnershipEntity> getListOwnership() {
		return listOwnership;
	}

	public void setListOwnership(List<OwnershipEntity> listOwnership) {
		this.listOwnership = listOwnership;
	}
 

	

}
