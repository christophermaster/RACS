package com.racs.core.services;

import com.racs.commons.exception.SisDaVyPException;
import com.racs.core.entities.Schedule;

import java.util.List;


public interface ScheduleService {

    List<Schedule> listSchedules();
    
    List<Schedule> listEnabledSchedule() throws SisDaVyPException;

    Schedule saveSchedule(Schedule schedule) throws SisDaVyPException ;

    void deleteSchedule(Long id);

    Schedule getScheduleById(Long id);
    
    Schedule getScheduleByTime(Integer hour, String min);

    Boolean getScheduleByExpression(String expression) throws SisDaVyPException ;

}
