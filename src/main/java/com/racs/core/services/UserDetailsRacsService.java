package com.racs.core.services;

import java.util.logging.Logger;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.racs.RacsWebApplication;
import com.racs.config.CustomUserDetails;

public class UserDetailsRacsService implements UserDetailsService {
	
	final Logger log = Logger.getLogger(RacsWebApplication.class.getName());
	
	private UserService userSsoService;
	
	public UserDetailsRacsService(UserService userSsoService) {
		this.userSsoService = userSsoService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("\n User: "+username);
		return new CustomUserDetails(userSsoService.findUserSsoByUsername(username));
	}

}
