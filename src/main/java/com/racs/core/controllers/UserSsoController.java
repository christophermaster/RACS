package com.racs.core.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.racs.commons.RolType;
import com.racs.commons.bean.HashGenerator;
import com.racs.commons.bean.Notification;
import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.RoleUser;
import com.racs.core.entities.User;
import com.racs.core.services.MailSenderService;
import com.racs.core.services.RoleUserService;
import com.racs.core.services.UserSsoService;

/**
 * @author team disca
 * 
 * Clase controladora del funcionamiento y administración de los
 * usuarios y sus aplicaciones. 18/03/2018
 */
@Controller(value="UserSsoController")
public class UserSsoController {

    
    private User userSsoRef;
	private HashGenerator hashGenerator = new HashGenerator();
	private RoleUser roleAppRef;
	private Notification notification;
	
	private List<User> listUserSsoRef;
	private Set<RoleUser> listRoleAppRef;
	
	//Services
	private UserSsoService userSsoService;
	private RoleUserService roleAppService;
	private MailSenderService mailSenderService;

	@Autowired
	public void setUserSsoService(UserSsoService userSsoService) {
		this.userSsoService = userSsoService;
	}

	@Autowired
	public void setRoleAppService(RoleUserService roleAppService) {
		this.roleAppService = roleAppService;
	}

	@Autowired
	public void setMailSenderService(MailSenderService mailSenderService) {
		this.mailSenderService = mailSenderService;
	}

    //USER
	/**
	 * Metodo para listar todos los usuarios en BD o en Active Directory.
	 *
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/sso/usuarios", method = RequestMethod.GET)
	public String list(Model model) {
		listUserSsoRef = new ArrayList<User>();
		listUserSsoRef = userSsoService.listUsersSso();
		model.addAttribute("usersSso", listUserSsoRef);
		return "usuarios";
	}

	/**
	 * Consultar un usuario especifico por ID *
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/sso/usuario/{id}")
	public String showUserSso(@PathVariable Long id, Model model) {
		userSsoRef = new User();
		userSsoRef = userSsoService.getUserSsoById(id);
		model.addAttribute("userSso", userSsoRef);
		return "usuarioshow";
	}

	/**
	 * Ediatr un usuario especifico por ID
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/sso/usuario/edit/{id}")
	public String editUserSso(@PathVariable Long id, Model model) {
		userSsoRef = new User();
		userSsoRef = userSsoService.getUserSsoById(id);

		model.addAttribute("userSso", userSsoRef);
		return "usuarioform";
	}

	/**
	 * Crear un nuevo Usuario.
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/sso/usuario/new")
	public String newUserSso(Model model) {
		model.addAttribute("userSso", new User());
		return "usuarioform";
	}

	/**
	 * Logout Usuario.
	 *
	 * @param
	 *
	 * @return
	 */
	@RequestMapping("/sso/logout-sso")
	public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("userSso", new User());
		invalidateSession(request, response);
		return "redirect:/login";
	}

	/**
	 * Guardar un usuario en la Fuente de Datos
	 *
	 * @param userSso
	 * @return
	 */
	@RequestMapping(value = "/sso/usuario", method = RequestMethod.POST)
	public String saveUserSso(User userSso, Model model) {
		userSsoRef = new User();
		notification = new Notification();
		try {
			//Si el usuario tiene Id quiere decir que se trata de una edicion
			if(userSso.getId() != null){
				userSsoRef = userSsoService.findUserSsoByUsername(userSso.getUsername());
				userSso.setRoles(userSsoRef.getRoles());
				if(userSso.isAdmin()){
					userSso.setEnabled(Boolean.TRUE);
				}
				userSsoRef = userSsoService.saveUserSso(userSso);
			}else{
				String passwordTemp = hashGenerator.getPasswordTemp();
				userSso.setPassword(passwordTemp);
				if(sendEmailNewUser(userSso, passwordTemp)){
					userSsoRef = userSsoService.saveUserSso(userSso);
				}else{
					notification.alert("1", "ERROR", "Ocurrieron inconvenientes al momento de enviar el correo. Verifíque que la dirección indicada sea válida.");
					model.addAttribute("userSso", userSso);
					model.addAttribute("notification", notification);
					return "usuarioform";
				}
			}

		} catch (SisDaVyPException e) {
			if (e.getCodigoErr().equals("DUPKEY")) {
				notification.alert("1", "ERROR", "Nombre de Usuario Ya EXISTE: " + userSso.getUsername());
			} else {
				notification.alert("1", "ERROR", e.getMensaje());
			}
			model.addAttribute("userSso", userSso);
			model.addAttribute("notification", notification);
			return "usuarioform";
		}
		notification.alert("1", "SUCCESS",
				"Usuario: ".concat(userSsoRef.getUsername()).concat(" Actualizado de forma EXITOSA"));
		model.addAttribute("userSso", userSso);
		model.addAttribute("notification", notification);
		return "usuarioshow";
	}

	/**
	 * Eliminar un usuario por el ID
	 * 
	 * @param id
	 * @return String
	 */
	@RequestMapping("/sso/usuario/delete/{id}")
	public String deleteUserSso(@PathVariable Long id, Model model) {
		userSsoRef = userSsoService.getUserSsoById(id);

		notification = new Notification();
		if(userSsoRef.isAdmin()){
			notification.alert("1", "ERROR",
					"No se puede eliminar un usuario Administrador.");
		}else{
			userSsoService.deleteUserSso(id);

			notification.alert("1", "SUCCESS",
					"El Usuario " + userSsoRef.getUsername() + " se ha eliminado correctamente.");
		}

		listUserSsoRef = new ArrayList<User>();
		listUserSsoRef = userSsoService.listUsersSso();
		model.addAttribute("notification", notification);
		model.addAttribute("usersSso", listUserSsoRef);
		return "usuarios";
	}

	// USUARIO-APLICACION-ROLE
	/**
	 * Metodo que permite consultar los roles de una aplicacion para vincular al
	 * usuario.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/sso/usuario/{id}/aplicacion/{appId}/ver")
	public String verRolesAplicacionesUsuario(@PathVariable Long id, @PathVariable Long appId, Model model) {
		// Usuario
		userSsoRef = new User();
		listRoleAppRef = new HashSet<RoleUser>();
		roleAppRef = new RoleUser();
		Set<RoleUser> listVinculadas = new HashSet<>();

		userSsoRef = userSsoService.getUserSsoById(id);
		Iterable<RoleUser> iter = userSsoRef.getRoles();
		
		for (RoleUser roleApp : iter) {
			//roleApp.setAplicationClient(applicationClientRef);
			listVinculadas.add(roleApp);
		}

		model.addAttribute("userSso", userSsoRef);// usuarioshow
		model.addAttribute("roles", listRoleAppRef);// vincularrolesusuario
		model.addAttribute("rolesvinc", listVinculadas);// vincularrolesusuario
		model.addAttribute("rol", roleAppRef);// vincularrolesusuario

		return "usuarioaplicacionroles";
	}
	
	/**
	 * Vincular una aplicacion a un usuario
	 * 
	 * @param rol
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sso/usuario/aplicacion/rol/vincular", method = RequestMethod.POST)
	public String saveRolUsuario(RoleUser rol, Model model) {
		userSsoRef = new User();
		listRoleAppRef = new HashSet<>();
		Set<RoleUser> listVinculadas = new HashSet<>();
		roleAppRef = new RoleUser();
		notification = null;
		Boolean existeRol = Boolean.FALSE;

		//userSsoRef = userSsoService.getUserSsoById(rol.getAplicationClient().getUserSso().getId());
		
		Iterable<RoleUser> iter1 = userSsoRef.getRoles();
		for (RoleUser roleApp : iter1) {
			if (roleApp.getId().equals(rol.getId())) {
				existeRol = Boolean.TRUE;
				notification = new Notification();
				notification.alert("1", "ERROR", "Rol " + roleApp.getName()+", ya esta Vinculado al Usuario");
			}
		}
		
		if (!existeRol) {
			roleAppRef = roleAppService.getRolById(rol.getId());
			userSsoRef.addRoleApp(roleAppRef);
			roleAppRef.addUserSso(userSsoRef);
			try {
				userSsoRef = userSsoService.saveUserSso(userSsoRef);
			} catch (SisDaVyPException e) {
				notification = new Notification();
				notification.alert("1", "ERROR", e.getMensaje());
			}		
		}
		
		Iterable<RoleUser> iter = userSsoRef.getRoles();
		for (RoleUser roleApp : iter) {
			//roleApp.setAplicationClient(applicationClientRef);
			listVinculadas.add(roleApp);
		}
		if (notification == null) {
			notification = new Notification();
			notification.alert("1", "SUCCESS","Rol " + roleAppRef.getName()+", Vinculado EXITOSAMENTE.");
		}
		model.addAttribute("userSso", userSsoRef);// usuarioshow
		model.addAttribute("roles", listRoleAppRef);// vincularrolesusuario
		model.addAttribute("rolesvinc", listVinculadas);// vincularrolesusuario
		model.addAttribute("rol", roleAppRef);// vincularrolesusuario
		model.addAttribute("notification", notification);
		notification = new Notification();

		return "usuarioaplicacionroles";
	}
	
	/**
	 * Desvincular un rol de un usuario
	 * 
	 * @param id
	 * @param idUser
	 * @param idApp
	 * @return Long
	 */
	@RequestMapping("/sso/usuario/{idUser}/aplicacion/{idApp}/rol/delete/{id}")
	public String deleteRolUsuario(@PathVariable Long idUser, @PathVariable Long idApp, @PathVariable Long id, Model model) {
		userSsoRef = new User();
		roleAppRef = new RoleUser();
		listRoleAppRef = new HashSet<>();
		Set<RoleUser> listVinculadas = new HashSet<>();
		
		try {
			userSsoRef = userSsoService.getUserSsoById(idUser);
			roleAppRef = roleAppService.getRolById(id);

			if(userSsoRef.isAdmin()){
				notification = new Notification();
				notification.alert("1", "ERROR", new String("No se puede desvincular el rol al usuario administrador"));
			}else{
				userSsoRef.removeRoleApp(roleAppRef);
				roleAppRef.removeUserSso(userSsoRef);

				userSsoService.saveUserSso(userSsoRef);
			}
			
		} catch (SisDaVyPException e) {
			if (e.getCodigoErr().equals("DUPKEY")) {
				notification = new Notification();
				notification.alert("1", "ERROR", new String("Falla Desvinculando Rol: " + roleAppRef.getName()));
			}
		}
		if (notification == null) {
			notification = new Notification();
			notification.alert("1", "SUCCESS","Rol " + roleAppRef.getName()+", se ha desvinculado EXITOSAMENTE.");
		}
		model.addAttribute("userSso", userSsoRef);// usuarioshow
		model.addAttribute("roles", listRoleAppRef);// vincularrolesusuario
		model.addAttribute("rolesvinc", listVinculadas);// vincularrolesusuario
		model.addAttribute("rol", roleAppRef);// vincularrolesusuario
		model.addAttribute("notification", notification);
		
		return "usuarioaplicacionroles";
	}
	
	/**
	 * Modificar un rol de un usuario
	 * @param rol
	 * @param model
	 *
	 * @return
	 */
	@RequestMapping(value = "/sso/usuario/aplicacion/rol/guardar", method = RequestMethod.POST)
	public String saveRolEditAplicacion(RoleUser rol, Model model) {
		userSsoRef = new User();
		listRoleAppRef = new HashSet<>();
		Set<RoleUser> listVinculadas = new HashSet<>();
		roleAppRef = new RoleUser();
		notification = null;
		Boolean existeRol = Boolean.FALSE;
		
		if (existeRol){
			roleAppRef = roleAppService.getRolById(rol.getId());
			model.addAttribute("tiporoles", this.obtenerTipoRoles());
			model.addAttribute("rol", roleAppRef);
			model.addAttribute("notification", notification);
			return "roledituser";
			
		}else{
			roleAppRef = roleAppService.getRolById(rol.getId());
			userSsoRef.addRoleApp(roleAppRef);
			roleAppRef.addUserSso(userSsoRef);
			try {
				userSsoRef = userSsoService.saveUserSso(userSsoRef);
			} catch (SisDaVyPException e) {
				notification = new Notification();
				notification.alert("1", "ERROR", e.getMensaje());
			}
			
			Iterable<RoleUser> iter = userSsoRef.getRoles();
			for (RoleUser roleApp : iter) {
				//roleApp.setAplicationClient(applicationClientRef);
				listVinculadas.add(roleApp);
			}
			
			if (notification == null) {
				notification = new Notification();
				notification.alert("1", "SUCCESS",
						"Rol: ".concat(roleAppRef.getName()).concat(" Actualizado de forma EXITOSA"));
			}
			model.addAttribute("userSso", userSsoRef);// usuarioshow
			model.addAttribute("roles", listRoleAppRef);// vincularrolesusuario
			model.addAttribute("rolesvinc", listVinculadas);// vincularrolesusuario
			model.addAttribute("rol", roleAppRef);// vincularrolesusuario
			model.addAttribute("notification", notification);
			return "aplicacionrolform";
		}
	}
	
	/**
	 * Editar un rol de un usuario por el ID
	 * 
	 * @param id
	 * @param userId
	 * @param appId
	 * @return Long
	 */
	@RequestMapping("/sso/usuario/{userId}/aplicacion/{appId}/rol/edit/{id}")
	public String editarRolAplicacionCliente(@PathVariable Long userId, @PathVariable Long appId, @PathVariable Long id, Model model) {

		roleAppRef = new RoleUser();
		userSsoRef = new User();

		roleAppRef = roleAppService.getRolById(id);
		userSsoRef = userSsoService.getUserSsoById(userId);
		
		model.addAttribute("tiporoles", this.obtenerTipoRoles());
		model.addAttribute("rol", roleAppRef);

		return "roledituser";
	}
	
	
	@RequestMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String errorLogin = (String) session.getAttribute("errorLogin");
		Boolean autenticado = (Boolean) session.getAttribute("authenticated");

		if(autenticado!= null && autenticado){
			return "redirect:/sso";
		}else{
			if(errorLogin != null){
				model.addAttribute("errorLogin", errorLogin);
			}
			model.addAttribute("userSso", new User());
			return "login";
		}

	}

	@RequestMapping(value = "/password/olvido", method = RequestMethod.GET)
	public String forgetPassword(Model model) {
		model.addAttribute("accion", "olvido");
		model.addAttribute("userSso", new User());
		return "password";
	}

	@RequestMapping(value = "/password/recuperar", method = RequestMethod.POST)
	public String resendPassword(HttpServletRequest request, User userSso, Model model) {
		notification = new Notification();
		userSsoRef = userSsoService.findUserSsoByUsername(userSso.getUsername());
		if(userSsoRef.getId() == null){
			notification.alert("1", "ERROR", "El correo electrónico ingresado no se encuentra registrado.");
			model.addAttribute("accion", "olvido");
		}
		else{
			//genero token
			String token = hashGenerator.getToken();
			try {
				//envio correo
				if(sendEmailToken(request, token, userSsoRef.getEmail())){
					//almaceno token
					//userSsoRef.setToken(token);
					//userSsoRef.setTokenDateValidate(new Date(System.currentTimeMillis() + 3600000));
					userSsoService.saveUserSso(userSsoRef);
					notification.alert("1", "SUCCES", "Se ha enviado un correo electrónico a la dirección especificada.");
					model.addAttribute("accion", "envioToken");
				}else{
					notification.alert("1", "ERROR", "Ocurrieron inconvenientes al momento de enviar el correo electrónico.");
					model.addAttribute("accion", "olvido");
				}
			} catch (SisDaVyPException e) {
				e.printStackTrace();
				notification.alert("1", "ERROR", "Ocurrieron inconvenientes al momento de asignar el codigo de validación al usuario.");
				model.addAttribute("accion", "olvido");
			}
		}

		model.addAttribute("notification", notification);
		model.addAttribute("userSso", userSsoRef);
		return "password";
	}

	//
	public String actualizarPassword(User userSso) {
		userSsoRef = new User();
		// genero token
		String token = hashGenerator.getToken();
		try {
			// envio correo
			if (sendEmailToken(token, userSso.getEmail())) {
				// almaceno token
				userSsoRef = userSsoService.findUserSsoByUsername(userSso.getUsername());
				//userSsoRef.setToken(token);
				//userSsoRef.setTokenDateValidate(new Date(System.currentTimeMillis() + 3600000));
				userSsoService.saveUserSso(userSsoRef);
			}
		} catch (SisDaVyPException e) {
			e.printStackTrace();
		}
		return "actualizarpass";
	}
	

	@RequestMapping("/password/recuperar/{token}")
	public String validateToken(HttpServletRequest request, @PathVariable String token, Model model) {
		notification = new Notification();
		userSsoRef = userSsoService.findUserSsoByToken(token);
		Date now = new Date(System.currentTimeMillis());
		if(userSsoRef.getId() == null){
			notification.alert("1", "ERROR", "El código de recuperación de contraseña no es valido.");
			model.addAttribute("notification", notification);
			model.addAttribute("userSso", new User());
			return "login";
		}else{
			if (Boolean.TRUE) {
				notification.alert("1", "ERROR", "El código de recuperación de contraseña ya expiro, adquiera uno nuevo.");
				model.addAttribute("accion", "olvido");
				model.addAttribute("notification", notification);
			}else{
				model.addAttribute("accion", "recuperar");
			}
			model.addAttribute("userSso", userSsoRef);
			return "password";
		}
	}

	@RequestMapping("/password/nueva")
	public String changePassword(User user, Model model) {
		notification = new Notification();
		User userUpdate = userSsoService.getUserSsoById(user.getId());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(userUpdate.getId() != null){
			userUpdate.setPassword(encoder.encode(user.getPassword()));
			try {
				userSsoService.saveUserSso(userUpdate);
				model.addAttribute("accion", "cambioPassword");
				notification.alert("1", "SUCCES", "Cambio de contraseña.");
			} catch (SisDaVyPException e) {
				e.printStackTrace();
				model.addAttribute("accion", "recuperar");
				notification.alert("1", "ERROR", "Ocurrieron inconvenientes al momento de actualizar la contraseña.");
			}
		}

		model.addAttribute("notification", notification);
		return "password";
	}

	// Metodos de Apoyo
	private List<String> obtenerTipoRoles() {
		List<String> tiporoles = new ArrayList<>();
		// Lista de tipos de roles
		tiporoles.add(RolType.ADMINISTRADOR_SEGURIDAD.toString());
		tiporoles.add(RolType.APLICACION.toString());

		return tiporoles;
	}

	public boolean sendEmailToken(HttpServletRequest request, String token, String email) {
		JavaMailSender emailSender = mailSenderService.getJavaMailSender();
		try {
			String uri = request.getRequestURL() + "/" + token;

			mailSenderService.sendEmailToken(email, uri, emailSender);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendEmailNewUser(User userSso, String passwordTemp) {
		JavaMailSender emailSender = mailSenderService.getJavaMailSender();
		try {
			mailSenderService.sendEmailNewUser(userSso, passwordTemp, emailSender);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendEmailToken(String token, String destino) {
		JavaMailSender emailSender = mailSenderService.getJavaMailSender();
		try {
			String uri = "http://localhost:8080/password/recuperar/" + token;
			mailSenderService.sendEmailToken(destino, uri, emailSender);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void invalidateSession(HttpServletRequest request, HttpServletResponse response) {
		// Se invalida la session
		HttpSession session = request.getSession();
		session.invalidate();

		response.setContentType("text/html");
		Cookie[] cookies = request.getCookies();

		// Se eliminan todas las cookies
		if (cookies != null) {

			for (int i = 0; i < cookies.length; i++) {

				Cookie cookie = cookies[i];
				cookies[i].setValue(null);
				cookies[i].setMaxAge(0);
				response.addCookie(cookie);
			}
		}
	}


}
