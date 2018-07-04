package com.racs.core.controllers;

import java.io.IOException;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.racs.commons.bean.BulkLoadCsv;
import com.racs.commons.bean.Notification;
import com.racs.commons.exception.SisDaVyPException;
import com.racs.commons.helper.CargaMasivaHelper;

@Controller
@MultipartConfig(fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5*5, maxRequestSize=1024*1024*5*5)
public class BulkLoadCsvController {

    private Notification notification;
    private CargaMasivaHelper cargaMasivaHelper;
    
	/**
	 * @return the cargaMasivaHelper
	 */
	public CargaMasivaHelper getCargaMasivaHelper() {
		return cargaMasivaHelper;
	}

	/**
	 * @param cargaMasivaHelper the cargaMasivaHelper to set
	 */
	@Autowired
	public void setCargaMasivaHelper(CargaMasivaHelper cargaMasivaHelper) {
		this.cargaMasivaHelper = cargaMasivaHelper;
	}

	/* REQUEST MAPPING */
	@RequestMapping(value = "/sso/cargar/usuarios", method = RequestMethod.GET)
	public String loadFile(Model model) {
		model.addAttribute("file", null);
		model.addAttribute("notification", null);
		return "cargarusuarios";
	}

	@RequestMapping(value = "/sso/procesar/archivo", method = RequestMethod.GET)
	public String refreshFile() {
		return "redirect:/sso/cargar/usuarios";
	}

	@RequestMapping(value = "/sso/procesar/archivo", method = RequestMethod.POST)
	public String importFile(@RequestParam("file") MultipartFile archivo, Model model)
			throws IOException, SisDaVyPException {
		BulkLoadCsv file;
		notification = new Notification();
		
		file = new BulkLoadCsv();
		file.setNameFile(archivo.getOriginalFilename());

		if (file.getNameFile().contains(".csv")) {
			
			//Invocar a clase helper que se encrga de procesar el archivo de carga masiva
			file = cargaMasivaHelper.procesarCargaMasiva(archivo,file);

			if (file.getUsersLoad() != null) {
				if (file.getTotal() == file.getProcessed()) {
					notification.alert("1", "SUCCESS", "Se procesaron los registros correctamente.");
				} else if (file.getTotal() == file.getNoProcessed()) {
					notification.alert("1", "ERROR", "No se proceso ningun registro del archivo.");
				} else {
					notification.alert("1", "WARNING", "Algunos registros no se procesaron.");
				}
			}
		} else {
			notification.alert("1", "ERROR", "Solo se permiten archivos .csv.");
		}
		model.addAttribute("file", file);
		model.addAttribute("notification", notification);

		return "cargarusuarios";
	}
}