package com.racs.core.entities;

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

@Entity
@Table(name = "DEVICE",  uniqueConstraints = @UniqueConstraint(columnNames ={"DEV_SERIAL"} ))
public class DeviceEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO  )
	@Column(name = "DEV_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="COM_ID")
	private ComunityEntity comunityEntity;
		
	@Column(name = "DEV_SERIAL", length=30)
	private String serialDevice;
	
	@Column(name = "DEV_IP_FTP", length=30)
	private String ipFTP;
	
	@Column(name = "DEV_PORT_FTP", length=5)
	private String portFTP;
	
	@Column(name = "DEV_USER_FTP",length=15)
	private String userFTP;
	
	@Column(name ="DEV_PASS_FTP" , length=15)
	private String passFTP;
	
	@Column(name ="DEV_STATE")
	private Boolean stateDevice;
	
	public DeviceEntity() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ComunityEntity getComunityEntity() {
		return comunityEntity;
	}

	public void setComunityEntity(ComunityEntity comunityEntity) {
		this.comunityEntity = comunityEntity;
	}

	public String getSerialDevice() {
		return serialDevice;
	}

	public void setSerialDevice(String serialDevice) {
		this.serialDevice = serialDevice;
	}

	public String getIpFTP() {
		return ipFTP;
	}

	public void setIpFTP(String ipFTP) {
		this.ipFTP = ipFTP;
	}

	public String getPortFTP() {
		return portFTP;
	}

	public void setPortFTP(String portFTP) {
		this.portFTP = portFTP;
	}

	public String getUserFTP() {
		return userFTP;
	}

	public void setUserFTP(String userFTP) {
		this.userFTP = userFTP;
	}

	public String getPassFTP() {
		return passFTP;
	}

	public void setPassFTP(String passFTP) {
		this.passFTP = passFTP;
	}

	public Boolean getStateDevice() {
		return stateDevice;
	}

	public void setStateDevice(Boolean stateDevice) {
		this.stateDevice = stateDevice;
	}
	
	
	

}
