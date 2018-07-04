package com.racs.core.services;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.Schedule;
import com.racs.core.repositories.ScheduleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	private ScheduleRepository scheduleRepository;
	private Schedule scheduleSupport;

	/*@Bean public PasswordEncoder getPasswordEncoder(){
		return new BCryptPasswordEncoder(); }*/

	@PersistenceContext
	private EntityManager em;

	@Autowired
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Autowired
	public void setScheduleRepository(ScheduleRepository scheduleRepository) {
		this.scheduleRepository = scheduleRepository;
	}

	@Override
	public List<Schedule> listSchedules() {
		return scheduleRepository.findAll();
	}

	@Override
	public Schedule saveSchedule(Schedule schedule) throws SisDaVyPException {
		scheduleSupport = new Schedule();
		try {
			if (schedule.getId() == null) {
				schedule.setCreated(new Date(System.currentTimeMillis()));
			}
			scheduleSupport = scheduleRepository.save(schedule);
		} catch (Exception e) {
			throw new SisDaVyPException(e.getMessage(), e.getCause(), "Error");
		}
		return scheduleSupport;
	}

	@Override
	public Schedule getScheduleById(Long id) {
		return scheduleRepository.findOne(id);
	}

	@Override
	public void deleteSchedule(Long id) {
		scheduleRepository.delete(id);
	}

	@Override
	public Schedule getScheduleByTime(Integer hour, String min) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Schedule> listEnabledSchedule() throws SisDaVyPException {
		List<Schedule> reSchedules = new ArrayList<>();
		try {
            TypedQuery<Schedule> query = em.createQuery("SELECT sh FROM Schedule sh WHERE sh.enabled = :enabled", Schedule.class);
            reSchedules = query.setParameter("enabled", Boolean.TRUE).getResultList();
		} catch (Exception e) {
			System.err.println(e.getCause());
			throw new SisDaVyPException();
		}
    	return reSchedules;
	}

	@Override
	public Boolean getScheduleByExpression(String expression) throws SisDaVyPException {
		List<Schedule> reSchedules = new ArrayList<>();
		try {
			TypedQuery<Schedule> query = em.createQuery("SELECT sh FROM Schedule sh WHERE sh.expression =  :expression",	Schedule.class);
			reSchedules = query.setParameter("expression", expression).getResultList();

			if(reSchedules.size() > 0){
				return true;
			}
		} catch (Exception e) {
			System.err.println(e.getCause());
			throw new SisDaVyPException();
		}
		return false;
	}

}
