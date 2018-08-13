package com.racs.core.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "accesshistory")
public class AccessHistoryEntity implements Serializable {

	private static final long serialVersionUID = -5703889625848068724L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Transient
	private Integer idOwner;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWN_ID")
	@JsonIgnore
	private OwnerEntity ownerEntity;

	@Column(name = "date")
	private String date;

	@Column(name = "hour")
	private String hour;

	@Column(name = "typeaccess", length = 30)
	private String typeaccess;

	@Column(name = "typesecurity", length = 30)
	private String typesecurity;

	@Lob
	@Column(name = "photho")
	private byte[] photho;

	private String ruta;

	public AccessHistoryEntity() {

	}

	public AccessHistoryEntity(Integer id, OwnerEntity ownerEntity, String date, String hour, String typeaccess,
			String typesecurity) {
		super();
		this.id = id;
		this.ownerEntity = ownerEntity;
		this.date = date;
		this.hour = hour;
		this.typeaccess = typeaccess;
		this.typesecurity = typesecurity;

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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getTypeaccess() {
		return typeaccess;
	}

	public void setTypeaccess(String typeaccess) {
		this.typeaccess = typeaccess;
	}

	public String getTypesecurity() {
		return typesecurity;
	}

	public void setTypesecurity(String typesecurity) {
		this.typesecurity = typesecurity;
	}

	public byte[] getPhotho() {
		return photho;
	}

	public void setPhotho(byte[] photho) {
		this.photho = photho;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public Integer getIdOwner() {
		return idOwner;
	}

	public void setIdOwner(Integer idOwner) {
		this.idOwner = idOwner;
	}

}
