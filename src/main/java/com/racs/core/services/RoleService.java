package com.racs.core.services;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.Roles;

import java.util.List;

public interface RoleService {

    List<Roles> listRoles();

    Roles getRolById(Long id);

    Roles saveRol(Roles rol) throws SisDaVyPException;

    void deleteRol(Long id);

}
