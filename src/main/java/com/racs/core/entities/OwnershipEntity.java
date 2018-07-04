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

@Entity
@Table(name = "OWNERSHIP")
public class OwnershipEntity implements Serializable{
	
	private static final long serialVersionUID = -5703889625848068724L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	@Column(name = "OSH_ID")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="OWN_ID")
    private OwnerEntity ownerEntity;
	
	
	@Column(name = "OSH_NUMBER\n", length=5)
	private String ownershipNumber;
	
	@Column(name = "OSH_TYPE", length=20)
	private String typeOwnership;
	
	
	@Column(name = "OSH_INHABITED")
	private Boolean inhabited;
	
	@Column(name = "OSH_AREA")
	private Double area;

	public OwnershipEntity() {

	}

	public OwnershipEntity(Integer id, OwnerEntity ownerEntity, String ownershipNumber, String typeOwnership,
			Boolean inhabited, Double area) {
		super();
		this.id = id;
		this.ownerEntity = ownerEntity;
		this.ownershipNumber = ownershipNumber;
		this.typeOwnership = typeOwnership;
		this.inhabited = inhabited;
		this.area = area;
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

	public String getOwnershipNumber() {
		return ownershipNumber;
	}

	public void setOwnershipNumber(String ownershipNumber) {
		this.ownershipNumber = ownershipNumber;
	}

	public String getTypeOwnership() {
		return typeOwnership;
	}

	public void setTypeOwnership(String typeOwnership) {
		this.typeOwnership = typeOwnership;
	}

	public Boolean getInhabited() {
		return inhabited;
	}

	public void setInhabited(Boolean inhabited) {
		this.inhabited = inhabited;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}
	
}
