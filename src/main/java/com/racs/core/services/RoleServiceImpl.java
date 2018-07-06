package com.racs.core.services;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.Roles;
import com.racs.core.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository rolRepository;
    private Roles roleAppSupport;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Autowired
    public void setRolRepository(RoleRepository rolRepository) {
		this.rolRepository = rolRepository;
	}
    
    @Override
    public List<Roles> listRoles() {
        return rolRepository.findAll();
    }

	@Override
    public Roles getRolById(Long id) {
        return rolRepository.findOne(id);
    }

    @Override
    public Roles saveRol(Roles rol) throws SisDaVyPException {
    	roleAppSupport = new Roles();
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

}
