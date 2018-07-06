package com.racs.commons.helper;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.User;
import com.racs.core.services.UserService;

/**
 * Clase que permite apoyar las acciones y opciones que se generan <br>
 * durante el proceso de autenticación por Base de Datos.
 * 
 * @author team disca
 *
 */
@Component
public class LoginHelper {

	private final Logger log = Logger.getLogger(LoginHelper.class.getName());

	@Value("${serverName}")
	private String serverName;

	@Value("${domainAD}")
	private String domainAD;

	@Autowired
	private UserService userSsoService1;
	
	public User findUserSsoOnDb(final UserService userSsoService, String usernameCredential, HttpSession session)
			throws UsernameNotFoundException {
		User result = new User();
		log.info("\n Usuario a Consultar: " + usernameCredential);
		result = userSsoService.findUserSsoByUsername(usernameCredential);
		try {
			if (result.getId() == null) {
				result = null;
			}
			// Se valida la fecha activo hasta del usuario
			if (result != null && result.getUserActiveUntil() != null) {
				Date hoy = new Date();
				if (hoy.after(result.getUserActiveUntil())) {
					result.setEnabled(false);
					result = userSsoService.saveUserSso(result);
					session.setAttribute("errorLogin", "El usuario se encuentra inactivo.");
					//int dias = (int) ((hoy.getTime() - result.getUserActiveUntil().getTime()) / 86400000);
					throw new UsernameNotFoundException("El usuario actualmente no tiene acceso al Sistema.");
				}
			}
		} catch (SisDaVyPException e) {
			throw new UsernameNotFoundException(e.getMensaje());
		} catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
		return result;
	}

	/**
	 * Conectarse y autenticar un usuario en Active Dierctory
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	public Boolean autenticarUserInActiveDirectory(String username, String password) {
		Boolean result = Boolean.FALSE;
		ActiveDirectoryHelper activeDirectoryHelper;

		log.info("Iniciando Autenticacion con Active Directory");

		String domain = domainAD;
		String servidor = serverName;
		// Creating instance of ActiveDirectory
		activeDirectoryHelper = new ActiveDirectoryHelper();
		result = activeDirectoryHelper.isUserValidInLDAP(username, password, domain, servidor);

		log.info("Usuario Autenticado contra el Active Directory");
		// Closing LDAP Connection
		activeDirectoryHelper.closeLdapConnection();
		return result;
	}

	//Getter & Setter
	public UserService getUserSsoService() {
		return userSsoService1;
	}

	public void setUserSsoService(UserService userSsoService) {
		this.userSsoService1 = userSsoService;
	}

	public HttpSession getSession(){
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession(true);
		session.setAttribute("errorLogin", "");

		return session;
	}
}
