package com.racs;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.WebApplicationInitializer;

import com.racs.commons.helper.LoginHelper;
import com.racs.commons.scheduler.ReadDBScheduler;
import com.racs.commons.scheduler.SchedulerConfiguration;
import com.racs.config.CustomAuthenticationProvider;
import com.racs.config.CustomUserDetails;
import com.racs.core.entities.User;
import com.racs.core.services.UserSsoService;

/**
 * Spring Boot Application que representa la clase de inicio del modulo.
 * @author team disca
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan(value = "com.racs")
public class RacsWebApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

	@Autowired
	private LoginHelper loginHelper;

	@Autowired
	private CustomAuthenticationProvider authenticationProvider;

	@Bean
	public SchedulerConfiguration schedulerConfiguration() {
		return new SchedulerConfiguration();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(RacsWebApplication.class);
	}

	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder, UserSsoService userSsoService) throws Exception {
		// estados de usuarios en active directory
		ReadDBScheduler readDBScheduler = new ReadDBScheduler();
		try {
			readDBScheduler.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

		builder.authenticationProvider(authenticationProvider)
				.userDetailsService(userDetailsService(authenticationProvider, userSsoService))
				.passwordEncoder(passwordEncoder());
	}

	public static void main(String[] args) {
		SpringApplication.run(RacsWebApplication.class, args);
		/**
		 * Se inicia el job recurrente (proceso planificado)
		 * que sincroniza los estados del usuario respecto del
		 * Active Directory.
		 */
		// estados de usuarios en active directory
//		ReadDBScheduler readDBScheduler = new ReadDBScheduler();
//		try {
//			readDBScheduler.run();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * Gestionar la etapa de autenticación del usuario que ofrece sus credenciales
	 * al modulo sso, además de instanciar los detalles del usuario ya autenticado
	 *
	 * @param customAuthentication
	 * @param userSsoService
	 * @return
	 */
	private UserDetailsService userDetailsService(final CustomAuthenticationProvider customAuthentication,
			final UserSsoService userSsoService) {

		System.out.println("[TEST] UserDetailsService: " + userSsoService.toString());

		return new UserDetailsService() {
			/**
			 * Metodo que permite ubicar el usuario en BD o en un Active Directory.
			 */
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				User userSso = null;
				CustomUserDetails customer = null;

				HttpSession session = loginHelper.getSession();
				/**
				 * Como el metodo authenticate de la clase CustomAuthenticationProvider es
				 * invocado para validar si el usuarios es administrador del modulo sso o
				 * si por el contrario es una usuario que necesita estar autenticado primero
				 * contra el Active Directory.
				 */
				if (customAuthentication.isUserAdminSSO) {
					userSso = loginHelper.findUserSsoOnDb(userSsoService, username, session);
					if (passwordEncoder().matches(customAuthentication.passwordDecode, userSso.getPassword())) {
						customer = new CustomUserDetails(userSso);
						session.setAttribute("username", username);
						session.setAttribute("authenticated", Boolean.TRUE);
					}else {
						session.setAttribute("errorLogin", "Contraseña Invalida.");
						throw new BadCredentialsException("Contraseña Invalida.");
					}
				} else {
					if (customAuthentication.isAuthInLDAP) {
						userSso = loginHelper.findUserSsoOnDb(userSsoService, username, session);
						if (userSso != null) {
							if (passwordEncoder().matches(customAuthentication.passwordDecode, userSso.getPassword())) {
								customer = new CustomUserDetails(userSso);
								session.setAttribute("username", username);
								session.setAttribute("authenticated", Boolean.TRUE);
							}else {
								session.setAttribute("errorLogin", "Contraseña Invalida.");
								throw new BadCredentialsException("Contraseñas Invalidas");
							}
						} else {
							/**
							 * Usuario esta autenticado contra el Active Directory,
							 * sin embargo, no existe en la BD del modulo
							 */
							session.setAttribute("errorLogin", "Usuario '" + username + "' "
									+ "no existe en el Modulo SSO, comuníquese con el administrador de seguridad");
//							throw new UsernameNotFoundException("Usuario '" + username + "' "
//									+ "no existe en el Modulo SSO, comuníquese con el administrador de seguridad");
						}
					} else {
						/**
						 * Usuario NO esta autenticado contra el Active Directory
						 * Se buscan los datos igualmente en la BD
						 */
						userSso = loginHelper.findUserSsoOnDb(userSsoService, username, session);
						if (userSso != null) {
							if (passwordEncoder().matches(customAuthentication.passwordDecode, userSso.getPassword())) {
								customer = new CustomUserDetails(userSso);
								session.setAttribute("username", username);
								session.setAttribute("authenticated", Boolean.TRUE);
							}else {
								session.setAttribute("errorLogin", "Contraseña Invalida.");
								throw new BadCredentialsException("Contraseñas Incorrecta.");
							}
						} else {
							/**
							 * Usuario no se encuentra en el Active directory y tampoco en la BD
							 */
							session.setAttribute("errorLogin", "Usuario '" + username + "' no Autorizado.");
//							throw new UsernameNotFoundException("Usuario '" + username + "' no Autorizado.");
						}
					}
				}
				return customer;
			}
		};
	}

	// Getter & Setter
	public LoginHelper getLoginHelper() {
		return loginHelper;
	}

	public void setLoginHelper(LoginHelper loginHelper) {
		this.loginHelper = loginHelper;
	}

}
