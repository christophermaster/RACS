package com.racs.core.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.commons.bean.Notification;
import com.racs.core.entities.OwnerEntity;
import com.racs.core.entities.VehicleEntity;
import com.racs.core.services.OwnerService;
import com.racs.core.services.VehicleService;

/**
 * Vehicle controller.
 */
@Controller
public class VehicleController {

	/*Services*/
    private VehicleService vehicleService;
    private OwnerService ownerService;
    
    /*Entity*/
    private VehicleEntity vehicle;
    private OwnerEntity owner;
    /*Notification*/
    private Notification notification;

    @Autowired
    public void setVehicleService(VehicleService vehicleService) {
        
    	this.vehicleService = vehicleService;
        
    }
    
    @Autowired
    public void setOwnerService(OwnerService ownerService) {
        
        this.ownerService = ownerService;
    }

    /**
     * List all Vehicle.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/sso/vehiculos", method = RequestMethod.GET)
    public String list(Model model) {
    	
        model.addAttribute("vehiculos", vehicleService.listAllVehicle());
        System.out.println("Returning vehiculos:");
        return "vehicle/vehiculos";
        
    }

    /**
     * View a specific Vehicle by its id.
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/sso/vehiculo/{id}")
    public String showVehicle(@PathVariable Integer id, Model model) {
        model.addAttribute("vehiculo", vehicleService.getVehicleById(id));
        return "vehicle/vehiculoshow";
    }

    @RequestMapping("/sso/vehiculo/editar/{id}")
    public String editVehicle(@PathVariable Integer id, Model model) {
    	
    	/*Se obtine la lista de los propietarios*/
    	model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("vehiculo", vehicleService.getVehicleById(id));
        return "vehicle/vehiculoform";
    }

    /**
     * New Vehicle.
     *
     * @param model
     * @return
     */
    @RequestMapping("/sso/vehiculo/nuevo")
    public String newVehicle(Model model) {
    	/*Se obtine la lista de los propietarios*/
    	model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("vehiculo", new VehicleEntity());
        return "vehicle/vehiculoform";
    }

    /**
     * Save Vehicle to database.
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/sso/vehiculo", method = RequestMethod.POST)
    public String saveVehicle(VehicleEntity vehicleEntity,  Model model) {
    	
    	vehicle = new VehicleEntity();
    	owner = new OwnerEntity();
    	
    	notification = new Notification();
    	
    	if(vehicleEntity.getId() != null) {
			    		
    		vehicle = vehicleService.saveVehicle(vehicleEntity);
						
			notification.alert("1", "SUCCESS",
					"Vehiculo: ".concat(vehicle.getLecenseplateVehicle()).concat(" Actualizado de forma EXITOSA"));
			
		}else {
			
			owner = ownerService.getOwnerById(vehicleEntity.getOwnerEntity().getId());
			vehicleEntity.setOwnerEntity(owner);
		
    		vehicle = vehicleService.saveVehicle(vehicleEntity);
			
    		notification.alert("1", "SUCCESS",
					"Vehiculo: ".concat(vehicle.getLecenseplateVehicle()).concat(" Actualizado de forma EXITOSA"));
			
		}
    	model.addAttribute("vehiculo", vehicle);
		model.addAttribute("notification", notification);
    	return "vehicle/vehiculoshow";
    }

    /**
     * Delete Vehicle by its id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/sso/vehiculo/eliminar/{id}")
    public String deleteVehicle(@PathVariable Integer id,Model model) {
    	
    	notification = new Notification();
    	
    	//Se obtiene el vehiculo a eliminar
    	vehicle = vehicleService.getVehicleById(id);
    	
    	//Se procede a eliminar el vehiculo 
    	vehicleService.deleteVehicle(id);
    	
    	notification.alert("1", "SUCCESS",
				"El vehiculo" + vehicle.getLecenseplateVehicle() + " se ha eliminado correctamente.");
    	
    	model.addAttribute("notification", notification);
    	model.addAttribute("vehiculos", vehicleService.listAllVehicle());
    	
    	return "vehicle/vehiculos";
    }

}
