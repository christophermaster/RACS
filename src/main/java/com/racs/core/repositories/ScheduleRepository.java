package com.racs.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.racs.core.entities.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
