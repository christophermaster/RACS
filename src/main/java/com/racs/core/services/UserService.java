package com.racs.core.services;

import java.util.List;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.User;

public interface UserService {

    List<User> listUsersSso();

    User getUserSsoById(Long id);

    User saveUserSso(User userSso) throws SisDaVyPException ;

    void deleteUserSso(Long id);

    User findUserSsoByUsername(String username);

    User findUserSsoByToken(String token);

    Boolean isAdminSsoUser(String username);

    User findUserSsoByCredentials(String username, String password);

}
