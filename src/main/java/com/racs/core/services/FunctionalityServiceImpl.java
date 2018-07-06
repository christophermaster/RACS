package com.racs.core.services;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.Functionality;
import com.racs.core.repositories.FunctionalityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Service
public class FunctionalityServiceImpl implements FunctionalityService {

    private FunctionalityRepository funcionalidadRepository;
    private Functionality functionalityRoleSupport;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public void setEm(EntityManager em) {
        this.em = em;
    }
    
    @Autowired
    public void setFuncionalidadRepository(FunctionalityRepository funcionalidadRepository) {
		this.funcionalidadRepository = funcionalidadRepository;
	}

	@Override
    public Iterable<Functionality> listFuncionalidades() {
        return funcionalidadRepository.findAll();
    }

	@Override
    public Functionality getFuncionalidadById(Long id) {
        return funcionalidadRepository.findOne(id);
    }

    @Override
    public Functionality saveFuncionalidad(Functionality funcionalidad) throws SisDaVyPException{
    	try {
			if (funcionalidad.getId()==null) {
				funcionalidad.setCreated(new Date(System.currentTimeMillis()));
			}
			functionalityRoleSupport = funcionalidadRepository.save(funcionalidad);
		} catch (Exception e) {
			throw new SisDaVyPException(e.getMessage(),e.getCause(),"DUPKEY");
		}     
        return functionalityRoleSupport;
    }

    @Override
    public void deleteFuncionalidad(Long id) {
        funcionalidadRepository.delete(id);
    }

    @Override
    public Functionality getFuncionalidadByName(String name, long roleAppId) {
        try {
            TypedQuery<Functionality> query = em.createQuery("SELECT fr FROM Functionality fr WHERE fr.name = :functionalityname AND fr.roleApp = :roleappid", Functionality.class);
            return query.setParameter("functionalityname", name).setParameter("roleappid", roleAppId).getSingleResult();

        } catch (Exception e) {
            //TODO
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Functionality getFunctionalityRoleByName(String name, long roleAppId) {
        try {
            Functionality objFunctionalityRole = new Functionality();
            TypedQuery<Functionality> query = em.createQuery("SELECT fr FROM Functionality fr WHERE fr.name = :functionalityname AND fr.roleApp = :roleappid", Functionality.class);

            List<Functionality> list = query.setParameter("functionalityname", name).setParameter("roleappid", roleAppId).getResultList();
            for (Functionality functionalityRole : list) {
                objFunctionalityRole = functionalityRole;
                break;
            }
            return objFunctionalityRole;

        } catch (Exception e) {
            //TODO
            e.printStackTrace();
            return new Functionality();
        }

    }

}
