package com.racs.core.controllers;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.commons.bean.Notification;
import com.racs.commons.helper.ConnectionFTP;
import com.racs.commons.helper.ConnectionSQlite;
import com.racs.core.entities.AccessHistoryEntity;
import com.racs.core.entities.ComunityEntity;
import com.racs.core.entities.DeviceEntity;
import com.racs.core.entities.OwnerEntity;
import com.racs.core.services.AccessHistoryService;
import com.racs.core.services.ComunityService;
import com.racs.core.services.DeviceService;
import com.racs.core.services.OwnerService;

/**
 * Product controller.
 */
@Controller
public class SynchronizationController {

    private DeviceService deviceService;
    private ComunityService comunityService;
    private AccessHistoryService accessHistoryService;
    private OwnerService ownerService;
    
    private Notification notification;
    
    private DeviceEntity device;
    private AccessHistoryEntity access;
    
    private ConnectionFTP connectionFTP;
    private ConnectionSQlite connectionSQlite;
    
    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Autowired
	public void setComunityService(ComunityService comunityService) {
		this.comunityService = comunityService;
	}
    
    @Autowired
    public void setConnectionFTP(ConnectionFTP connectionFTP) {
		this.connectionFTP = connectionFTP;
	}

	@Autowired
	public void setConnectionSQlite(ConnectionSQlite connectionSQlite) {
		this.connectionSQlite = connectionSQlite;
	}
	
	@Autowired
    public void setAccessHistoryService(AccessHistoryService accessHistoryService) {
		this.accessHistoryService = accessHistoryService;
	}

	@Autowired
	public void setOwnerService(OwnerService ownerService) {
		this.ownerService = ownerService;
	}

    
    /**
     * List all products.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/sso/sincronizaciones", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("dispositivos", deviceService.listAllDevice());
        System.out.println("Returning products:" + model);
        return "synchronization/sincronizaciones";
    }

    /**
     * View a specific product by its id.
     *
     * @param id
     * @param model
     * @return
     * @throws Exception 
     */
    @RequestMapping("/sso/sincronizacion/{id}")
    public String sincronizacion(@PathVariable Integer id, Model model) throws Exception {
    	
    	device = new DeviceEntity();
    	
    	device = deviceService.getDeviceById(id);
    	
    	String server = device.getIpFTP();
    	Integer port = Integer.valueOf(device.getPortFTP());
    	String user = device.getUserFTP();
    	String pass = device.getPassFTP();
    	
    	connectionFTP.connectionDownloadFTP(server, port, user, pass);;
    	notification = new Notification();
    	
    	if (selectAll() != false){
    	    notification.alert("1", "SUCCESS","Sincronizacón Exitosa");
    	}else {
    		notification.alert("1", "ERROR","Sincronizacón Fallida");
    	}
    	
    	model.addAttribute("notification", notification);
    	model.addAttribute("dispositivos", deviceService.listAllDevice());
        return "synchronization/sincronizaciones";
    }
    
    public Boolean selectAll() throws Exception{
    	
        access = new AccessHistoryEntity() ;
        OwnerEntity owner = new OwnerEntity();
  
        String sql = "SELECT * FROM accesshistory"; 

        try {
        	
	    	 Connection conn = connectionSQlite.connectSqlite();
	    	 
	    	 if(conn != null) {
	    		 
	    		 Statement stmt  = conn.createStatement();
	    	     
		         ResultSet rs    = stmt.executeQuery(sql);
		       
		        // loop through the result set
		        while (rs.next()) {
		        
		        	access.setDate(rs.getString("HIS_DATE"));
		        	access.setHour(rs.getString("HIS_HOUR"));
		        	access.setPhotho(rs.getBytes("HIS_PHOTO"));
		        	access.setTypeaccess(rs.getString("HIS_TYPEACCESS"));
		        	access.setTypesecurity(rs.getString("HIS_TYPESECURITY"));
		        	owner = ownerService.getOwnerById(rs.getInt("OWN_ID"));
		        	access.setOwnerEntity(owner);
		        
		            accessHistoryService.saveAccessHistory(access);
		            access = new AccessHistoryEntity() ;
		            owner = new OwnerEntity();
		        }
		        
		        return true;
 
	    	 }else{
	    		
	    	    	
	    		 return false;
	    	 }
	        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    
    
   


}
