package com.racs.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.racs.commons.bean.Notification;
import com.racs.commons.helper.ConnectionFTP;
import com.racs.commons.helper.DataTransfers;
import com.racs.core.entities.AccessHistoryEntity;
import com.racs.core.entities.DeviceEntity;
import com.racs.core.services.AccessHistoryService;
import com.racs.core.services.DeviceService;
import com.racs.core.services.OwnerService;


/**
 *  Synchronization controller.
 */
@Controller
public class SynchronizationController {

	/*Services*/
    private DeviceService deviceService;
    private AccessHistoryService accessHistoryService;
    private OwnerService oService;

    /*Entity*/
    private DeviceEntity device;
    private AccessHistoryEntity accessHistoryEntity;
    /*Notificaciones*/
    private Notification notification;
    
    private ConnectionFTP connectionFTP;
    private DataTransfers dataTransfers;
    
    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
    
    @Autowired
    public void setOwnerService(OwnerService oService) {
        this.oService = oService;
    }

    @Autowired
    public void setConnectionFTP(ConnectionFTP connectionFTP) {
		this.connectionFTP = connectionFTP;
	}
    
    @Autowired
    public void setAccessHistoryService(AccessHistoryService accessHistoryService) {
		this.accessHistoryService = accessHistoryService;
	}
    
    /**
     * List all  Synchronization.
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
     * View a specific  Synchronization by its id.
     *
     * @param id
     * @param model
     * @return
     * @throws Exception 
     */
    @RequestMapping("/sso/sincronizacion/{id}")
    public String sincronizacion(@PathVariable Integer id, Model model) throws Exception {
    	
    	/*Se inicaliza la entidad*/
    	device = new DeviceEntity();
    	
    	/*Transferencia de datos*/
    	
    	dataTransfers = new DataTransfers();
    	
    	/*Notificaion*/
    	notification = new Notification();
    	
    	/*verificar la conexion ftp*/
    	Boolean success = false;
    	
    	/*se optine los datos de conexion ftp para extraer la base de datos*/
    	device = deviceService.getDeviceById(id);
    	
    	String server = device.getIpFTP(); //ip del ftp
    	Integer port = Integer.valueOf(device.getPortFTP());//puerto de entra
    	String user = device.getUserFTP(); //usuario de conexion
    	String pass = device.getPassFTP(); //contrasela de conexion 
    	
    	/*Se hace la conexion */
    	success = connectionFTP.connectionDownloadFTP(server, port, user, pass);
    	
    	/*se verifica si la conexion si fue exitosa o no */
    	if(success) {
    		
    		notification.alert("1", "SUCCESS","Conexion Exitosa al dispositivo");
    		
    		/*Verificamos si se  extrajo y se guardo el historico*/
    		List<AccessHistoryEntity> list = dataTransfers.dataTransfersFromSQLiteToMySQL();
    		
    		if (list != null){
    			
    			for(int i = 0; i < list.size(); i++ ) {
    				
    				accessHistoryEntity = list.get(i);
    				accessHistoryEntity.setOwnerEntity(oService.getOwnerById(accessHistoryEntity.getIdOwner()));
    				accessHistoryService.saveAccessHistory(accessHistoryEntity);
    				accessHistoryEntity = new AccessHistoryEntity();
    			}
    			
        	    notification.alert("1", "SUCCESS","Sincronizacón Exitosa");
        	    
        	}else {
        		
        		notification.alert("1", "ERROR","Sincronizacón Fallida");
        		
        	}
    		
    	}else {
    		notification.alert("1", "ERROR","Conexion Fallida al dispositivo");
    	}
    	   	
    	model.addAttribute("notification", notification);
    	model.addAttribute("dispositivos", deviceService.listAllDevice());
    	
        return "synchronization/sincronizaciones";
    }
    
    
    
   


}
