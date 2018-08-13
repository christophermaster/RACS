package com.racs.core.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.commons.bean.Notification;
import com.racs.core.entities.ComunityEntity;
import com.racs.core.services.ComunityService;

/**
 * Comunity controller.
 */
@Controller
public class ComunityController {

	/*Services*/
    private ComunityService comunityService;
    
    /*Entity*/
    private ComunityEntity comunity;
    
    /*Notification*/
    private Notification notification;
    
    @Autowired
    public void setComunityService(ComunityService comunityService) {
        this.comunityService = comunityService;
    }

    /**
     * List all Comunity.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/sso/comunidades", method = RequestMethod.GET)
    public String list(Model model) {
    	
        model.addAttribute("comunidades", comunityService.listAllComunyty());
        return "comunity/comunidades";
        
    }

    /**
     * View a specific Comunity by its id.
     *
     * @param id
     * @param model
     * @return
     */
    
    @RequestMapping("/sso/comunidad/{id}")
    public String showComunity(@PathVariable Integer id, Model model) {
    	
        model.addAttribute("comunidad", comunityService.getComunityById(id));
        return "comunity/comunidadshow";
        
    }

   
    @RequestMapping("/sso/comunidad/editar/{id}")
    public String editComunity(@PathVariable Integer id, Model model) {
    	
        model.addAttribute("comunidad", comunityService.getComunityById(id));
        return "comunity/comunidadform";
    }

    /**
     * New Comunity.
     *
     * @param model
     * @return
     */
    @RequestMapping("/sso/comunidad/nuevo")
    public String newComunity(Model model) {
        model.addAttribute("comunidad", new ComunityEntity());
        return "comunity/comunidadform";
    }

    /**
     * Save Comunity to database.
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/sso/comunidad", method = RequestMethod.POST)
    public String saveComunity(ComunityEntity comunityEntity,  Model model) {
    	
    	comunity = new ComunityEntity();
		notification = new Notification();
		
		//verificamos si es una comunidad nueva o no , para mostrar el msj adecuado
		if(comunityEntity.getId() != null) {
			
			comunity = comunityService.saveComunity(comunityEntity);
			
			notification.alert("1", "SUCCESS",
					"Comunidad: ".concat(comunity.getNameComunity()).concat(" Actualizado de forma EXITOSA"));
			
		}else {
			
			comunity = comunityService.saveComunity(comunityEntity);

			notification.alert("1", "SUCCESS",
					"Comunidad: ".concat(comunity.getNameComunity()).concat(" Guardado de forma EXITOSA"));
			
		}
		
		model.addAttribute("comunidad", comunity);
		model.addAttribute("notification", notification);
		
        return "comunity/comunidadshow";
    }

    /**
     * Delete Comunity by its id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/sso/comunidad/eliminar/{id}")
    public String deleteComunity(@PathVariable Integer id,Model model) {
    	
    	comunity = new ComunityEntity();
    	notification = new Notification();
    	
    	//Se obtiene la comunidad a eliminar 
    	comunity = comunityService.getComunityById(id);
    	
    	//Se procede a eliminar la comunidad 
    	comunityService.deleteComunity(id);
    	
    	
    	//Se muestra la notificacion  
    	notification.alert("1", "SUCCESS",
				"La Comunidad " + comunity.getNameComunity() + " se ha eliminado correctamente.");
    	
    	model.addAttribute("notification", notification);
    	model.addAttribute("comunidades", comunityService.listAllComunyty());
    	
    	return "comunity/comunidades";
    }

}
