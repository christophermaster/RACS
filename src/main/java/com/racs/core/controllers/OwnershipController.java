package com.racs.core.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.commons.bean.Notification;
import com.racs.core.entities.OwnerEntity;
import com.racs.core.entities.OwnershipEntity;
import com.racs.core.services.OwnerService;
import com.racs.core.services.OwnershipService;

/**
 * Ownership controller.
 */
@Controller
public class OwnershipController {

    private OwnershipService ownershipService;
    private OwnerService ownerService;
    private OwnershipEntity ownership;
    private Notification notification;
    
    @Autowired
    public void setOwnershipService(OwnershipService ownershipService) {
        this.ownershipService = ownershipService; 
    }
    
    @Autowired
    public void setOwnerService(OwnerService ownerService) {
    	this.ownerService = ownerService;
        
    }
    
	/**
     * List all Ownership.
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
     * View a specific Ownership by its id.
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

  
    @RequestMapping("/sso/propiedad/editar/{id}")
    public String editOwner(@PathVariable Integer id, Model model) {
    	/*Se obtine la lista de los propietarios*/
        model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("propiedad", ownershipService.getOwnerById(id));
        return "ownership/propiedadform";
        
    }

    /**
     * New Ownership.
     *
     * @param model
     * @return
     */
    @RequestMapping("/sso/propiedad/nuevo")
    public String newOwner(Model model) {
    	
    	/*Se obtine la lista de los propietarios*/
        model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("propiedad", new OwnershipEntity());
        return "ownership/propiedadform";
        
    }

    /**
     * Save Ownership to database.
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/sso/propiedad", method = RequestMethod.POST)
    public String saveOwner(OwnershipEntity ownershipEntity ,Model model) {
    	
    	ownership = new OwnershipEntity();
    	notification = new Notification();
    	OwnerEntity owner = new OwnerEntity();
    	    	
    	/*verificamos si es una propiedad nueva o no , para mostrar el msj adecuado*/
    	if(ownershipEntity.getId() != null) {
			    		
    		ownership = ownershipService.saveOwner(ownershipEntity);
			
			notification.alert("1", "SUCCESS",
					"Propiedad: ".concat(ownership.getOwnershipNumber()).concat(" Actualizado de forma EXITOSA"));
			
		}else {
			
			/*Se optiene la entidad de usuario amtes de guardarla */
			owner = ownerService.getOwnerById(ownershipEntity.getOwnerEntity().getId());
			ownershipEntity.setOwnerEntity(owner);
			
			ownership = ownershipService.saveOwner(ownershipEntity);
			
			notification.alert("1", "SUCCESS",
					"Propiedad: ".concat(ownership.getOwnershipNumber()).concat(" Guardado de forma EXITOSA"));
			
		}
    	
    	
    	model.addAttribute("propiedad", ownership);
		model.addAttribute("notification", notification);
		return "ownership/propiedadshow";
    }

    /**
     * Delete Ownership by its id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/sso/propiedad/eliminar/{id}")
    public String deleteOwner(@PathVariable Integer id, Model model) {
    	
    	ownership = new OwnershipEntity();
    	notification = new Notification();
    	
    	//Se obtiene la propiedad a eliminar 
    	ownership = ownershipService.getOwnerById(id);
    	
    	//Se procede a eliminar la propiedad 
    	ownershipService.deleteOwner(id);
    	
    	notification.alert("1", "SUCCESS",
				"La Propiedad" + ownership.getOwnershipNumber() + " se ha eliminado correctamente.");
    	
    	model.addAttribute("notification", notification);
    	model.addAttribute("propiedades", ownershipService.listAllOwner());
    	
    	return "ownership/propiedades";
    }

}
