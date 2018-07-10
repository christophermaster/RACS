package com.racs.core.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.commons.bean.Notification;
import com.racs.core.entities.AccessHistoryEntity;
import com.racs.core.services.AccessHistoryService;
import com.racs.core.services.OwnerService;

/**
 * Product controller.
 */
@Controller
public class AccessHistoryController {

    private AccessHistoryService accessHistoryService;
    private AccessHistoryEntity access;
    private OwnerService ownerService;
    private Notification notification;

    @Autowired
	public void setAccessHistoryService(AccessHistoryService accessHistoryService,
			OwnerService ownerService) {
		this.accessHistoryService = accessHistoryService;
		this.ownerService = ownerService;
	}
   

    /**
     * List all products.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/sso/historicos", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("historicos", accessHistoryService.listAllAccessHistory());
        System.out.println("Returning todos:");
        return "history/historicos";
    }



	/**
     * View a specific product by its id.
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/sso/historial/{id}")
    public String showAccessHistory(@PathVariable Integer id, Model model) {
    	AccessHistoryEntity accessHistoryEntity;
    	accessHistoryEntity = accessHistoryService.getAccessHistoryById(id);
    	byte[] data = accessHistoryEntity.getPhotho();
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
	        BufferedImage bImage2;
			bImage2 = ImageIO.read(bis);
			ImageIO.write(bImage2, "jpg", new File("C:\\Users\\ESTACION1\\Desktop\\proyec\\RACSystem\\src\\main\\resources\\static\\images\\output.jpg"));
		
			accessHistoryEntity.setRuta("C:\\Users\\ESTACION1\\Desktop\\proyec\\RACSystem\\src\\main\\resources\\static\\images\\output.jpg");
			System.out.println(accessHistoryEntity.getRuta());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
        model.addAttribute("historial",accessHistoryEntity);
        
        return "history/historialshow";
    }

    // Afficher le formulaire de modification du Product
    @RequestMapping("/sso/historial/editar/{id}")
    public String editAccessHistory(@PathVariable Integer id, Model model) {
     	model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("historial", accessHistoryService.getAccessHistoryById(id));
        return "history/historialform";
    }

    /**
     * New product.
     *
     * @param model
     * @return
     */
    @RequestMapping("/sso/historial/nuevo")
    public String newAccessHistory(Model model) {
    	
    	System.out.println("AQUIIIIIIII 1" );
     	model.addAttribute("propietarios", ownerService.listAllOwner());
        model.addAttribute("historial", new AccessHistoryEntity());
        return "history/historialform";
    }

    /**
     * Save product to database.
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/sso/historial", method = RequestMethod.POST)
    public String saveAccessHistory(AccessHistoryEntity accessHistoryEntity,Model model) {
    	
    	notification = new Notification();
    	access = new AccessHistoryEntity();

    	Optional<byte[]> binary = toBinary("C:\\Users\\ESTACION1\\Desktop\\imagenes\\8.jpg");
    	// la imagen se procesó sin problemas y hay datos
    	if(binary.isPresent()) {
    	    byte[] image = binary.get();
    		accessHistoryEntity.setPhotho(image);
    	}
    	
    	if(accessHistoryEntity.getId() != null) {
			
    		accessHistoryService.saveAccessHistory(accessHistoryEntity);
    		access = accessHistoryService.getAccessHistoryById(accessHistoryEntity.getId());
			
			
			notification.alert("1", "SUCCESS",
					"El Acceso de: ".concat(access.getOwnerEntity().getNameOwner()).concat(" Actualizado de forma EXITOSA"));
			
		}else {
			
			accessHistoryService.saveAccessHistory(accessHistoryEntity);
			access = accessHistoryService.getAccessHistoryById(accessHistoryEntity.getId());
			
			System.out.println("entidad" + access);
			notification.alert("1", "SUCCESS",
					"El Acceso de: ".concat(access.getTypeaccess()).concat(" Guardado de forma EXITOSA"));
			
		}
    	
    
    	model.addAttribute("historial", access);
		model.addAttribute("notification", notification);
    	 return "history/historialshow";
    }

    /**
     * Delete product by its id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/sso/historial/eliminar/{id}")
    public String deleteAccessHistory(@PathVariable Integer id, Model model) {
    	
    	access = accessHistoryService.getAccessHistoryById(id);
    	accessHistoryService.deleteAccessHistory(id);
    	
    	notification = new Notification();
    	
    	notification.alert("1", "SUCCESS",
				"El Acceso de " + access.getOwnerEntity().getNameOwner() + " se ha eliminado correctamente.");
    	model.addAttribute("notification", notification);
    	model.addAttribute("historicos", accessHistoryService.listAllAccessHistory());
    	
    	
    	 return "history/historicos";
    }
    
    
    public static Optional<byte[]> toBinary(String path) {
        int len = path.split("\\.").length;
        String ext = path.split("\\.")[len - 1];
        try {
            BufferedImage img = ImageIO.read(new File(path));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, ext, baos);
            return Optional.of(baos.toByteArray());
        } catch(IOException e) {
            return Optional.empty();
        }
    }

    
    
    

}