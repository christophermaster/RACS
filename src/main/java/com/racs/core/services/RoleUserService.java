package com.racs.core.services;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.RoleUser;

import java.util.List;

public interface RoleUserService {

    List<RoleUser> listRoles();
    
    List<RoleUser> listRolesByApplicationId(Long idApp);

    RoleUser getRolById(Long id);

    RoleUser findRolByNameApp(String name, Long idApp);

    RoleUser saveRol(RoleUser rol) throws SisDaVyPException;

    void deleteRol(Long id);

}
