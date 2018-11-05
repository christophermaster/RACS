package com.racs.core.controllers;

import java.util.Base64;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.racs.commons.bean.Notification;
import com.racs.commons.helper.Base64Encoder;
import com.racs.core.entities.AccessHistoryEntity;
import com.racs.core.services.AccessHistoryService;
import com.racs.core.services.OwnerService;

/**
 * Access controller.
 */
@Controller
public class AccessHistoryController {

	/*Services*/
    private AccessHistoryService accessHistoryService;
    private OwnerService ownerService;
    
    /*Entity*/
    private AccessHistoryEntity access;
    
	/*Notification*/
	private Notification notification;  
    

	
	@Autowired
    public void setAccessHistoryService(AccessHistoryService accessHistoryService) {
		this.accessHistoryService = accessHistoryService;
	}

	@Autowired
	public void setOwnerService(OwnerService ownerService) {
		this.ownerService = ownerService;
	}


	/**
     * List all Access.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/sso/historicos", method = RequestMethod.GET)
    public String list(Model model) {
    
        model.addAttribute("historicos", accessHistoryService.listAllAccessHistory());
        return "history/historicos";
    }



	/**
     * View a specific Access by its id.
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/sso/historial/{id}")
    public String showAccessHistory(@PathVariable Integer id, Model model) {

    	access = new AccessHistoryEntity();
    	
    	/*obtenemos el acceso por le id */
    	access = accessHistoryService.getAccessHistoryById(id);
    	
    	/*se codifica la imagen para que pueda ser visible */
//    	access.setRuta(Base64Encoder.imageToBase64(access.getPhotho()));
    	access.setRuta(new String(Base64.getEncoder().encode(access.getPhotho())));
        model.addAttribute("historial",access);
        
        return "history/historialshow";
    }


    @RequestMapping("/sso/historial/editar/{id}")
    public String editAccessHistory(@PathVariable Integer id, Model model) {
     	model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("historial", accessHistoryService.getAccessHistoryById(id));
        return "history/historialform";
    }

    /**
     * New Access.
     *
     * @param model
     * @return
     */
    @RequestMapping("/sso/historial/nuevo")
    public String newAccessHistory(Model model) {
    	
  
     	model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("historial", new AccessHistoryEntity());
        return "history/historialform";
    }

    /**
     * Save Access to database.
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/sso/historial", method = RequestMethod.POST)
    public String saveAccessHistory(AccessHistoryEntity accessHistoryEntity,Model model) {
    	
    	notification = new Notification();
    	access = new AccessHistoryEntity();

    	Optional<byte[]> binary = Base64Encoder.base64ToImage("C:\\Users\\ESTACION1\\Desktop\\imagenes\\8.jpg");
    	
    	// Verificamos si la imagen se proceso sin problema 
    	
    	if(binary.isPresent()) {
    	    byte[] image = binary.get();
    		accessHistoryEntity.setPhotho(image);
    	}
    	
    	if(accessHistoryEntity.getId() != null) {
					
    		access = accessHistoryService.saveAccessHistory(accessHistoryEntity);
			
			notification.alert("1", "SUCCESS",
					"El Acceso de: ".concat(access.getOwnerEntity().getNameOwner()).concat(" Actualizado de forma EXITOSA"));
			
		}else {
						
			access = accessHistoryService.saveAccessHistory(accessHistoryEntity);
			
			System.out.println("entidad" + access);
			notification.alert("1", "SUCCESS",
					"El Acceso de: ".concat(access.getTypeaccess()).concat(" Guardado de forma EXITOSA"));
			
		}
    	
    
    	model.addAttribute("historial", access);
		model.addAttribute("notification", notification);
    	 return "history/historialshow";
    }

    /**
     * Delete Access by its id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/sso/historial/eliminar/{id}")
    public String deleteAccessHistory(@PathVariable Integer id, Model model) {
    	
    	notification = new Notification();

    	//Se obtiene el Acceso a eliminar
    	access = accessHistoryService.getAccessHistoryById(id);
    	
    	//Se procede a eliminar el Acceso 
    	accessHistoryService.deleteAccessHistory(id);
    	
    	
    	notification.alert("1", "SUCCESS",
				"El Acceso de " + access.getOwnerEntity().getNameOwner() + " se ha eliminado correctamente.");
    	
    	model.addAttribute("notification", notification);
    	model.addAttribute("historicos", accessHistoryService.listAllAccessHistory());
    	
    	
    	 return "history/historicos";
    }
    
   
}
