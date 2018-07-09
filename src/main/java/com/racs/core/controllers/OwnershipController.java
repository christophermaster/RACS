package com.racs.core.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.commons.bean.Notification;
import com.racs.core.entities.OwnershipEntity;
import com.racs.core.services.OwnerService;
import com.racs.core.services.OwnershipService;

/**
 * Product controller.
 */
@Controller
public class OwnershipController {

    private OwnershipService ownershipService;
    private OwnerService ownerService;
    private OwnershipEntity ownership;
    private Notification notification;
    
    @Autowired
    public void setOwnershipService(OwnershipService ownershipService,OwnerService ownerService) {
        this.ownershipService = ownershipService;
        this.ownerService = ownerService;
        
    }
    
	/**
     * List all products.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/sso/propiedades", method = RequestMethod.GET)
    public String list(Model model) {

        model.addAttribute("propiedades", ownershipService.listAllOwner());
        System.out.println("Returning propiedades:");
        return "ownership/propiedades";
    }

    /**
     * View a specific product by its id.
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/sso/propiedad/{id}")
    public String showOwner(@PathVariable Integer id, Model model) {
        model.addAttribute("propiedad", ownershipService.getOwnerById(id));
        return "ownership/propiedadshow";
    }

    // Afficher le formulaire de modification du Product
    @RequestMapping("/sso/propiedad/editar/{id}")
    public String editOwner(@PathVariable Integer id, Model model) {
        model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("propiedad", ownershipService.getOwnerById(id));
        return "ownership/propiedadform";
    }

    /**
     * New product.
     *
     * @param model
     * @return
     */
    @RequestMapping("/sso/propiedad/nuevo")
    public String newOwner(Model model) {
        model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("propiedad", new OwnershipEntity());
        return "ownership/propiedadform";
    }

    /**
     * Save product to database.
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/sso/propiedad", method = RequestMethod.POST)
    public String saveOwner(OwnershipEntity ownershipEntity ,Model model) {
    	
    	ownership = new OwnershipEntity();
    	notification = new Notification();
    	
    	if(ownershipEntity.getId() != null) {
			
    		ownershipService.saveOwner(ownershipEntity);
    		ownership = ownershipService.getOwnerById(ownershipEntity.getId());
			
			
			notification.alert("1", "SUCCESS",
					"Propiedad: ".concat(ownership.getOwnershipNumber()).concat(" Actualizado de forma EXITOSA"));
			
		}else {
			
			ownershipService.saveOwner(ownershipEntity);
			ownership = ownershipService.getOwnerById(ownershipEntity.getId());
			
			
			notification.alert("1", "SUCCESS",
					"Propiedad: ".concat(ownership.getOwnershipNumber()).concat(" Guardado de forma EXITOSA"));
			
		}
    	
    	
    	model.addAttribute("propiedad", ownership);
		model.addAttribute("notification", notification);
		return "ownership/propiedadshow";
    }

    /**
     * Delete product by its id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/sso/propiedad/eliminar/{id}")
    public String deleteOwner(@PathVariable Integer id, Model model) {
    	
    	notification = new Notification();
    	
    	ownership = ownershipService.getOwnerById(id);
    	ownershipService.deleteOwner(id);
    	
    	notification.alert("1", "SUCCESS",
				"La Propiedad" + ownership.getOwnershipNumber() + " se ha eliminado correctamente.");
    	
    	model.addAttribute("notification", notification);
    	model.addAttribute("propiedades", ownershipService.listAllOwner());
    	
    	return "ownership/propiedades";
    }

}
