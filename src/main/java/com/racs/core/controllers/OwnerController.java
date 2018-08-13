package com.racs.core.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.commons.bean.Notification;
import com.racs.core.entities.ComunityEntity;
import com.racs.core.entities.OwnerEntity;
import com.racs.core.services.ComunityService;
import com.racs.core.services.OwnerService;

/**
 * Owner controller.
 */
@Controller
public class OwnerController {

	/*Services*/
    private OwnerService ownerService;
    private ComunityService comunityService;
    
    /*Entity*/
    private OwnerEntity owner;
    private ComunityEntity comunity;
    
    /*Notification*/
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
     * List all Owner.
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
     * View a specific Owner by its id.
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

    //Ver el formulario de cambio de producto
    @RequestMapping("/sso/propietario/editar/{id}")
    public String editOwner(@PathVariable Integer id, Model model) {
    	
    	/*Se lista todas la communidades*/
    	model.addAttribute("comunidades", comunityService.listAllComunyty());
        model.addAttribute("propietario", ownerService.getOwnerById(id));
        return "owner/propietarioform";
        
    }

    /**
     * New Owner.
     *
     * @param model
     * @return
     */
    @RequestMapping("/sso/propietario/nuevo")
    public String newOwner(Model model) {
    	
    	/*Se lista todas la communidades*/
    	model.addAttribute("comunidades", comunityService.listAllComunyty());
        model.addAttribute("propietario", new OwnerEntity());
        
        return "owner/propietarioform";
    }

    /**
     * Save Owner to database.
     *
     * @param Owner
     * @return
     */
    @RequestMapping(value = "/sso/propietario", method = RequestMethod.POST)
    public String saveOwner(OwnerEntity ownerEntity, Model model) {
    	
    	owner = new OwnerEntity();
    	comunity = new ComunityEntity();	
    	
    	notification = new Notification();
    	
    	
    	if(ownerEntity.getId() != null) {
			
    		owner = ownerService.saveOwner(ownerEntity);

			notification.alert("1", "SUCCESS",
					"Propietario: ".concat(owner.getNameOwner()).concat(" Actualizado de forma EXITOSA"));
			
		}else {
			
			/*Antes de guardar se obtine la comunidad con el id asociado */
			comunity = comunityService.getComunityById(ownerEntity.getComunityEntity().getId());
			
			/*se setea la comunidad en la entidad de propietario*/
	    	ownerEntity.setComunityEntity(comunity);
	    	
	    	/*se procede el guardado*/
	    	owner = ownerService.saveOwner(ownerEntity);
			
			notification.alert("1", "SUCCESS",
					"Propietario: ".concat(owner.getNameOwner()).concat(" Guardado de forma EXITOSA"));
			
		}
		
		model.addAttribute("propietario", owner);
		model.addAttribute("notification", notification);
    	return "owner/propietarioshow";
    }

    /**
     * Delete Owner by its id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/sso/propietario/eliminar/{id}")
    public String deleteOwner(@PathVariable Integer id, Model model) {
    	
    	owner = new OwnerEntity();
    	notification = new Notification();
    	
    	//Se obtiene el propietario a eliminar 
    	owner = ownerService.getOwnerById(id);
    	
    	//Se procede a eliminar la comunidad 
    	ownerService.deleteOwner(id);
    	
		
    	//Se muestra la notificacion  
    	notification.alert("1", "SUCCESS",
				"El propietario" + owner.getNameOwner() + " se ha eliminado correctamente.");
    	
    	model.addAttribute("notification", notification);
    	model.addAttribute("propietarios", ownerService.listAllOwner());
    	
    	return "owner/propietarios";
    }

}
