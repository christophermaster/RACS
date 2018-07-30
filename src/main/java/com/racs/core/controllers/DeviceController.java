package com.racs.core.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.commons.bean.Notification;
import com.racs.core.entities.ComunityEntity;
import com.racs.core.entities.DeviceEntity;
import com.racs.core.services.ComunityService;
import com.racs.core.services.DeviceService;

/**
 * Product controller.
 */
@Controller
public class DeviceController {

    private DeviceService deviceService;
    private ComunityService comunityService;
    private Notification notification;
    private DeviceEntity device;
    
    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Autowired
	public void setComunityService(ComunityService comunityService) {
		this.comunityService = comunityService;
	}
    /**
     * List all products.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/sso/dispositivos", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("dispositivos", deviceService.listAllDevice());
        System.out.println("Returning products:" + model);
        return "device/dispositivos";
    }

    /**
     * View a specific product by its id.
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/sso/dispositivo/{id}")
    public String showDevice(@PathVariable Integer id, Model model) {
        model.addAttribute("dispositivo", deviceService.getDeviceById(id));
        return "device/dispositivoshow";
    }

    // Afficher le formulaire de modification du Product
    @RequestMapping("/sso/dispositivo/editar/{id}")
    public String editDevice(@PathVariable Integer id, Model model) {
    	model.addAttribute("comunidades", comunityService.listAllComunyty());
        model.addAttribute("dispositivo", deviceService.getDeviceById(id));
        return "device/dispositivoform";
    }

    /**
     * New product.
     *
     * @param model
     * @return
     */
    @RequestMapping("/sso/dispositivo/nuevo")
    public String newDevice(Model model) {
    	model.addAttribute("comunidades", comunityService.listAllComunyty());
        model.addAttribute("dispositivo", new DeviceEntity());
        return "device/dispositivoform";
    }

    /**
     * Save product to database.
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/sso/dispositivo", method = RequestMethod.POST)
    public String saveDevice(DeviceEntity deviceEntity,  Model model) {
    	
    	
    	System.out.println("1");
		device = new DeviceEntity();
		notification = new Notification();
		
		ComunityEntity comunity = new ComunityEntity();	

		if(deviceEntity.getId() != null) {
			
			deviceService.saveDevice(deviceEntity);
			device = deviceService.getDeviceById(deviceEntity.getId());
			
			
			notification.alert("1", "SUCCESS",
					"Dispositivo: ".concat(device.getSerialDevice()).concat(" Actualizado de forma EXITOSA"));
			
		}else {
			comunity = comunityService.getComunityById(deviceEntity.getComunityEntity().getId());
			deviceEntity.setComunityEntity(comunity);
			deviceService.saveDevice(deviceEntity);
			device = deviceService.getDeviceById(deviceEntity.getId());
			
			
			notification.alert("1", "SUCCESS",
					"Dispositivo: ".concat(device.getSerialDevice()).concat(" Guardado de forma EXITOSA"));
			
		}
		
		model.addAttribute("dispositivo", device);
		model.addAttribute("notification", notification);
		
        return "device/dispositivoshow";
    }

    /**
     * Delete product by its id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/sso/dispositivo/eliminar/{id}")
    public String deleteDevice(@PathVariable Integer id,Model model) {
    	
    	device = deviceService.getDeviceById(id);
    	deviceService.deleteDevice(id);
    	
    	notification = new Notification();
    	
    	notification.alert("1", "SUCCESS",
				"El Dispositivo " + device.getSerialDevice()+ " se ha eliminado correctamente.");
    	
    	model.addAttribute("notification", notification);
    	model.addAttribute("disposotivo", deviceService.listAllDevice());
    	
    	return "device/dispositivos";
    }

}
