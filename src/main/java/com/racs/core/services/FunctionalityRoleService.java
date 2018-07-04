package com.racs.core.services;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.FunctionalityRole;

public interface FunctionalityRoleService {

    Iterable<FunctionalityRole> listFuncionalidades();

    FunctionalityRole getFuncionalidadById(Long id);

    FunctionalityRole getFuncionalidadByName(String name, long idRoleApp);

    FunctionalityRole saveFuncionalidad(FunctionalityRole funcionalidad) throws SisDaVyPException;

    void deleteFuncionalidad(Long id);

    FunctionalityRole getFunctionalityRoleByName(String name, long RoleAppId);

}
