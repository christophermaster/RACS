package com.racs.commons.scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.racs.commons.scheduler.job.ReadDBJob;

/**
 * Clase que permite planificar minuto a minuto el job de consulta a BD para
 * conocer si existe algun proceso programado pendiente de ejecució
 * 
 *  @author team disca
 */
@Component
public class ReadDBScheduler {

	SchedulerConfiguration schedulerConfiguration = new SchedulerConfiguration();

	public void run() throws Exception {
		Logger log = LoggerFactory.getLogger(ReadDBScheduler.class);

		log.info("JOB: Inicializando Job Consulta BD Tarea de Planificada del Modulo");

		// creamos referencia a scheduler (planificador)
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		JobDetail jobReadDbJobPlan = newJob(ReadDBJob.class).withIdentity("jobReadDbJobPlan", "group1")
				.build();
		
		// Tarea planificada a ser ejecutada cada treinta (30) minutos.
//		CronTrigger trigger = newTrigger().withIdentity("trigger1", "group1")
//				.withSchedule(cronSchedule(processUpdateStatusUser)).build();

		CronTrigger trigger = newTrigger().withIdentity("trigger1", "group1")
				.withSchedule(cronSchedule(schedulerConfiguration.getProcessUpdateStatusUser())).build();
		Date ft = sched.scheduleJob(jobReadDbJobPlan, trigger);

		// Iniciar el planificador
		sched.start();
		log.info("JOB: ".concat(jobReadDbJobPlan.getKey().toString())
					.concat(" Expresion de Planificacion: ").concat(trigger.getCronExpression())
					.concat(" Iniciado el: ").concat(ft.toString()));

	}
}
