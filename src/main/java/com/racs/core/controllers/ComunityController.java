package com.racs.core.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.core.entities.ComunityEntity;
import com.racs.core.services.ComunityService;

/**
 * Product controller.
 */
@Controller
public class ComunityController {

    private ComunityService comunityService;

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
    @RequestMapping(value = "/sso/comunidades", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("comunidades", comunityService.listAllComunyty());
        System.out.println("Returning products:" + model);
        return "comunity/comunidades";
    }

    /**
     * View a specific product by its id.
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

    // Afficher le formulaire de modification du Product
    @RequestMapping("/sso/comunidad/editar/{id}")
    public String editComunity(@PathVariable Integer id, Model model) {
        model.addAttribute("comunidad", comunityService.getComunityById(id));
        return "comunity/comunidadform";
    }

    /**
     * New product.
     *
     * @param model
     * @return
     */
    @RequestMapping("/sso/comunidad/nuevo")
    public String newComunity(Model model) {
        model.addAttribute("comunidad", new ComunityEntity());
        return "comunity/comunidadnuevo";
    }

    /**
     * Save product to database.
     *
     * @param product
     * @return
     */
    @RequestMapping(value = "/sso/comunidad", method = RequestMethod.POST)
    public String saveComunity(ComunityEntity comunityEntity) {
    	comunityService.saveComunity(comunityEntity);
        return "redirect:/sso/comunidad/" + comunityEntity.getId();
    }

    /**
     * Delete product by its id.
     *
     * @param id
     * @return
     */
    @RequestMapping("/sso/comunidad/eliminar/{id}")
    public String deleteComunity(@PathVariable Integer id) {
    	comunityService.deleteComunity(id);
        return "redirect:/sso/comunidades/";
    }

}
