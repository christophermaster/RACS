package com.racs.core.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.commons.bean.Notification;
import com.racs.core.entities.OwnerEntity;
import com.racs.core.services.ComunityService;
import com.racs.core.services.OwnerService;

/**
 * Product controller.
 */
@Controller
public class OwnerController {

    private OwnerService ownerService;
    private ComunityService comunityService;
    private OwnerEntity owner;
    private Notification notification;
    @Autowired
    public void setOwnerService(OwnerService ownerService) {
        this.ownerService = ownerService;
        
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
    @RequestMapping(value = "/sso/propietarios", method = RequestMethod.GET)
    public String list(Model model) {
    	
        model.addAttribute("propietarios", ownerService.listAllOwner());
        System.out.println("Returning rpoducts:");
        return "owner/propietarios";
    }

    /**
     * View a specific product by its id.
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/sso/propietario/{id}")
    public String showOwner(@PathVariable Integer id, Model model) {
        model.addAttribute("propietario", ownerService.getOwnerById(id));
        return "owner/propietarioshow";
    }

    // Afficher le formulaire de modification du Product
    @RequestMapping("/sso/propietario/editar/{id}")
    public String editOwner(@PathVariable Integer id, Model model) {
    	model.addAttribute("comunidades", comunityService.listAllComunyty());
        model.addAttribute("propietario", ownerService.getOwnerById(id));
        return "owner/propietarioform";
    }

    /**
     * New product.
     *
     * @param model
     * @return
     */
    @RequestMapping("/sso/propietario/nuevo")
    public String newOwner(Model model) {
    	model.addAttribute("comunidades", comunityService.listAllComunyty());
        model.addAttribute("propietario", new OwnerEntity());
        return "owner/propietarioform";
    }

    /**
     * Save product to database.
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/sso/propietario", method = RequestMethod.POST)
    public String saveOwner(OwnerEntity ownerEntity, Model model) {
    	
    	owner = new OwnerEntity();
    	notification = new Notification();
    	if(ownerEntity.getId() != null) {
			
    		ownerService.saveOwner(ownerEntity);
			owner = ownerService.getOwnerById(ownerEntity.getId());
			
			
			notification.alert("1", "SUCCESS",
					"Propietario: ".concat(owner.getNameOwner()).concat(" Actualizado de forma EXITOSA"));
			
		}else {
			
			ownerService.saveOwner(ownerEntity);
			owner = ownerService.getOwnerById(ownerEntity.getId());
			
			
			notification.alert("1", "SUCCESS",
					"Propietario: ".concat(owner.getNameOwner()).concat(" Guardado de forma EXITOSA"));
			
		}
		
		model.addAttribute("propietario", owner);
		model.addAttribute("notification", notification);
    	return "owner/propietarioshow";
    }

    /**
     * Delete product by its id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/sso/propietario/eliminar/{id}")
    public String deleteOwner(@PathVariable Integer id, Model model) {
    	
    	notification = new Notification();
    	
    	owner = ownerService.getOwnerById(id);
    	ownerService.deleteOwner(id);
    	
		
    	
    	notification.alert("1", "SUCCESS",
				"El propietario" + owner.getNameOwner() + " se ha eliminado correctamente.");
    	
    	model.addAttribute("notification", notification);
    	model.addAttribute("propietarios", ownerService.listAllOwner());
    	
    	return "owner/propietarios";
    }

}
