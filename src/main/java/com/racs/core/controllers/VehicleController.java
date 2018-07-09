package com.racs.core.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.commons.bean.Notification;
import com.racs.core.entities.VehicleEntity;
import com.racs.core.services.OwnerService;
import com.racs.core.services.VehicleService;

/**
 * Product controller.
 */
@Controller
public class VehicleController {

    private VehicleService vehicleService;
    private OwnerService ownerService;
    private VehicleEntity vehicle;
    private Notification notification;

    @Autowired
    public void setVehicleService(VehicleService vehicleService,OwnerService ownerService) {
        this.vehicleService = vehicleService;
        this.ownerService = ownerService;
    }

    /**
     * List all products.
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
     * View a specific product by its id.
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

    // Afficher le formulaire de modification du Product
    @RequestMapping("/sso/vehiculo/editar/{id}")
    public String editVehicle(@PathVariable Integer id, Model model) {
    	model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("vehiculo", vehicleService.getVehicleById(id));
        return "vehicle/vehiculoform";
    }

    /**
     * New product.
     *
     * @param model
     * @return
     */
    @RequestMapping("/sso/vehiculo/nuevo")
    public String newVehicle(Model model) {
    	model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("vehiculo", new VehicleEntity());
        return "vehicle/vehiculoform";
    }

    /**
     * Save product to database.
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/sso/vehiculo", method = RequestMethod.POST)
    public String saveVehicle(VehicleEntity vehicleEntity,  Model model) {
    	
    	vehicle = new VehicleEntity();
    	notification = new Notification();
    	if(vehicleEntity.getId() != null) {
			
    		vehicleService.saveVehicle(vehicleEntity);
    		vehicle = vehicleService.getVehicleById(vehicleEntity.getId());
			
			
			notification.alert("1", "SUCCESS",
					"Vehiculo: ".concat(vehicle.getLecenseplateVehicle()).concat(" Actualizado de forma EXITOSA"));
			
		}else {
			
			vehicleService.saveVehicle(vehicleEntity);
    		vehicle = vehicleService.getVehicleById(vehicleEntity.getId());
			
			
    		notification.alert("1", "SUCCESS",
					"Vehiculo: ".concat(vehicle.getLecenseplateVehicle()).concat(" Actualizado de forma EXITOSA"));
			
		}
    	model.addAttribute("vehiculo", vehicle);
		model.addAttribute("notification", notification);
    	return "vehicle/vehiculoshow";
    }

    /**
     * Delete product by its id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/sso/vehiculo/eliminar/{id}")
    public String deleteVehicle(@PathVariable Integer id,Model model) {
    	
    	notification = new Notification();
    	vehicle = vehicleService.getVehicleById(id);
    	vehicleService.deleteVehicle(id);
    	
    	notification.alert("1", "SUCCESS",
				"El vehiculo" + vehicle.getLecenseplateVehicle() + " se ha eliminado correctamente.");
    	
    	model.addAttribute("notification", notification);
    	model.addAttribute("vehiculos", vehicleService.listAllVehicle());
    	
    	return "vehicle/vehiculos";
    }

}
