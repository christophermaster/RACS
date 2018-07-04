package com.racs.core.services;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.RoleUser;
import com.racs.core.entities.User;
import com.racs.core.repositories.UserSsoRepository;

@Service
public class UserSsoServiceImpl implements UserSsoService {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UserSsoServiceImpl.class);

	private UserSsoRepository userSsoRepository;
	private User userSsoSupport;

	@Value("${spring.datasource.url}")
	private String urlDB;

	@Value("${spring.datasource.username}")
	private String userDB;

	@Value("${spring.datasource.password}")
	private String passwordDB;

	@Value("${spring.datasource.driver-class-name}")
	private String driverClassDB;

	/*@Bean public PasswordEncoder getPasswordEncoder(){
		return new BCryptPasswordEncoder(); }*/

	@PersistenceContext
	private EntityManager em;

	@Autowired
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Autowired
	public void setUserSsoRepository(UserSsoRepository userSsoRepository) {
		this.userSsoRepository = userSsoRepository;
	}

	@Override
	public List<User> listUsersSso() {
		return userSsoRepository.findAll();
	}

	@Override
	public User getUserSsoById(Long id) {
		return userSsoRepository.findOne(id);
	}

	@Override
	public User saveUserSso(User userSso) throws SisDaVyPException {
		userSsoSupport = new User();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		try {
			if (userSso.getId() == null) {
				userSso.setCreationDate(new Date(System.currentTimeMillis()));
				if(userSso.getPassword()==null){
					logger.warn("[Carga Masiva] Usuario " + userSso.getUsername() +": registrado con password null.");
				}else{
					userSso.setPassword(encoder.encode(userSso.getPassword()));
				}
			}
			userSsoSupport = userSsoRepository.saveAndFlush(userSso);
		} catch (Exception e) {
			if (e instanceof DataIntegrityViolationException) {
				throw new SisDaVyPException(e.getMessage(), e.getCause(), "DUPKEY");
			} else {
				throw new SisDaVyPException(e.getMessage(), e.getCause(), "NOSE");
			}
		}
		return userSsoSupport;
	}

	@Override
	public void deleteUserSso(Long id) {
		userSsoRepository.delete(id);
	}

	@Override
	public User findUserSsoByUsername(String username) {
		userSsoSupport = new User();
		try {
			TypedQuery<User> query = em.createQuery("SELECT us FROM UserSso us WHERE us.username =  :username  and us.enabled = true",
					User.class);
			userSsoSupport = query.setParameter("username", username.trim()).getSingleResult();
		} catch (Exception e) {
			System.err.println("Usuario no encontrado en FindUserByUserName: " + username);
		}
		return userSsoSupport;
	}

	@Override
	public Boolean isAdminSsoUser(String username) {
		userSsoSupport = null;
		Boolean result = Boolean.FALSE;
		Set<RoleUser> listaRoles = null;
		try {
			//TODO mejorar consulta
			TypedQuery<User> query = em.createQuery("SELECT us FROM UserSso us WHERE us.username =  :username  and us.enabled = true",
					User.class);
			userSsoSupport = query.setParameter("username", username.trim()).getSingleResult();
			listaRoles = userSsoSupport.getRoles();
			for (RoleUser role : listaRoles ) {
				//TODO CLASE CONSTANTE
				if(role.getName().equals("ADMIN_SSO")){
					result = Boolean.TRUE;
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("FALLA: metodo isAdminSsoUser, con usuario: " + username);
		}
		return result;
	}

	@Override
	public User findUserSsoByToken(String token) {
		userSsoSupport = new User();
		try {
			TypedQuery<User> query = em.createQuery("SELECT us FROM UserSso us WHERE us.token =  :token",
					User.class);
			userSsoSupport = query.setParameter("token", token.trim()).getSingleResult();
		} catch (Exception e) {
			System.err.println("Token no encontrado en findUserSsoByToken: " + token);
		}
		return userSsoSupport;
	}

	@Override
	public User findUserSsoByCredentials(String username, String password) {
		userSsoSupport = new User();
		try {
			TypedQuery<User> query = em.createQuery("SELECT us FROM UserSso us WHERE us.username =  :username and us.password = :password and us.enabled = true",
					User.class);
			userSsoSupport = query.setParameter("username", username.trim()).setParameter("password", password).getSingleResult();
		} catch (Exception e) {
			System.err.println("Credenciales invalidas: " + username);
		}
		return userSsoSupport;
	}

}
