package com.racs.commons.scheduler;

import com.racs.commons.scheduler.job.UpdateStatusUserJob;
import com.racs.core.entities.Schedule;

import org.hibernate.validator.constraints.ModCheck;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Clase que permite ejecutar la consulta de los estados de los usuarios 
 * en Active Directory, para luego actualiza los mismo en BD.
 * 
 *  @author team disca
 */
@Component
public class UpdateStatusUserADScheduler {

	SchedulerConfiguration schedulerConfiguration = new SchedulerConfiguration();

	private String hashTrigger;

	public String getHashTrigger() {
		return hashTrigger;
	}

	public void setHashTrigger(String hashTrigger) {
		this.hashTrigger = hashTrigger;
	}

	public UpdateStatusUserADScheduler(){
	}

	public void run() throws Exception {
		Logger log = LoggerFactory.getLogger(UpdateStatusUserADScheduler.class);
		
		//Recupera de BD el Job Planificado
		Schedule schPlanificadoBD = this.recuperarScheduleToExecute();

		log.info("--- Inicializando Job Recuerrente de consulta en BD de Tarea Programada ---");

		// creamos referencia a scheduler (planificador)
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		JobDetail jobUpdateStatusUser = newJob(UpdateStatusUserJob.class).withIdentity(schPlanificadoBD.getName() + getHashTrigger(), "group2")
				.build();

		log.info(schPlanificadoBD.getExpression());
		
		// Tarea planificada a ser ejecutada cada dos (2) minutos.
		CronTrigger trigger = newTrigger().withIdentity("triggerSchedule_" + getHashTrigger(), "group2")
				.withSchedule(cronSchedule(schPlanificadoBD.getExpression())).build();
		Date ft = sched.scheduleJob(jobUpdateStatusUser, trigger);

		// Iniciar el planificador
		sched.start();
		log.info("------- Planificador Consulta BD Iniciado --".concat(jobUpdateStatusUser.getKey().toString())
					.concat("- fecha: ").concat(ft.toString())
					.concat(" Planificado: ").concat(trigger.getCronExpression()));

		java.util.Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		//Reinicio de jobs y trigger a las 12
//		if(sdf.format(cal.getTime()).equals("12:00")){
//			log.info("---Terminando Proceso de Actualización de Estatus de Usuarios ---");
//			sched.shutdown(Boolean.FALSE);
//			log.info("------- Fin de Proceso -----------------");
//			SchedulerMetaData metaData = sched.getMetaData();
//			log.info("Ejecución en " + metaData.getNumberOfJobsExecuted() + " tareas.");
//
//			ReadDBScheduler readDBScheduler = new ReadDBScheduler();
//			try {
//				readDBScheduler.run();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	private Schedule recuperarScheduleToExecute() {
		Schedule sch = null;
		Connection conexion = null;
		Calendar cal = null;
		int hora, minutos;

		try {
			Class.forName(schedulerConfiguration.getDriverClassDB());
			conexion = DriverManager.getConnection(schedulerConfiguration.getUrlDB(), schedulerConfiguration.getUserDB(), schedulerConfiguration.getPasswordDB());
			Statement s = conexion.createStatement();
			cal = Calendar.getInstance();
			hora = cal.get(Calendar.HOUR_OF_DAY);
			minutos = cal.get(Calendar.MINUTE);

			ResultSet rs = s.executeQuery("SELECT id, name, hours, minutes, enabled, expression " + "FROM schedule where hours = "
					+ hora + " and minutes between " + minutos + " and " + (minutos + 29) + " and enabled = 'true'");

			while (rs.next()) {
				sch = new Schedule();
				sch.setId(rs.getLong(1));
				sch.setName(rs.getString(2).trim());
				sch.setHours(rs.getInt(3));
				sch.setMinutes(rs.getInt(4));
				sch.setEnabled(rs.getBoolean(5));
				sch.setExpression(rs.getString(6));
			}
		}catch (Exception e) {
			//TODO validar mensaje o propagacion de excepcion
			e.printStackTrace();
		}finally {
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
					//TODO validar mensaje o propagacion de excepcion
					e.printStackTrace();
				}
			}
		}
		return sch;
	}
}
