package com.racs.core.services;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.RoleUser;
import com.racs.core.repositories.RoleAppRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Service
public class RoleUserServiceImpl implements RoleUserService {

    private RoleAppRepository rolRepository;
    private RoleUser roleAppSupport;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Autowired
    public void setRolRepository(RoleAppRepository rolRepository) {
		this.rolRepository = rolRepository;
	}
    
    @Override
    public List<RoleUser> listRoles() {
        return rolRepository.findAll();
    }

	@Override
    public RoleUser getRolById(Long id) {
        return rolRepository.findOne(id);
    }

    @Override
    public RoleUser saveRol(RoleUser rol) throws SisDaVyPException {
    	roleAppSupport = new RoleUser();
    	try {
			if (rol.getId()==null) {
				rol.setCreationDate(new Date(System.currentTimeMillis()));
			}
			roleAppSupport = rolRepository.saveAndFlush(rol);
		} catch (Exception e) {
			//TODO validar exception
			throw new SisDaVyPException(e.getMessage(), e.getCause(), "DUPKEY");
		}
    	return roleAppSupport;
    }

    @Override
    public void deleteRol(Long id) {
    	/*try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("busquedaRoles");
			EntityManager em = emf.createEntityManager();
			TypedQuery<RoleApp> query = em.createQuery("DELETE  FROM RoleApp ra WHERE ra.id = :rolid", RoleApp.class);
			query.setParameter("rolid", id).getSingleResult();
		} catch (Exception e) {
			//TODO
			e.printStackTrace();
		}*/
    	rolRepository.delete(id);
    	//return;
    }

    @Override
    public RoleUser findRolByNameApp(String name, Long appId) {
    	roleAppSupport = new RoleUser();
    	try {
            TypedQuery<RoleUser> query = em.createQuery("SELECT ra FROM RoleApp ra WHERE ra.name = :rolname AND ra.ApplicationClient = :appid", RoleUser.class);
            roleAppSupport = query.setParameter("rolname", name).setParameter("appid", appId).getSingleResult();

		} catch (Exception e) {
			//TODO
			e.printStackTrace();
		}
    	return roleAppSupport;
    }
    
    @Override
    public List<RoleUser> listRolesByApplicationId(Long idapp){
    	return rolRepository.findAll();
    }

}
