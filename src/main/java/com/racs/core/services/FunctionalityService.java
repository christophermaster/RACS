package com.racs.core.services;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.Functionality;

public interface FunctionalityService {

    Iterable<Functionality> listFuncionalidades();

    Functionality getFuncionalidadById(Long id);

    Functionality getFuncionalidadByName(String name, long idRoleApp);

    Functionality saveFuncionalidad(Functionality funcionalidad) throws SisDaVyPException;

    void deleteFuncionalidad(Long id);

    Functionality getFunctionalityRoleByName(String name, long RoleAppId);

}
