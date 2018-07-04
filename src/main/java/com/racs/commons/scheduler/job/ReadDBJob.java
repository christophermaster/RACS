
package com.racs.commons.scheduler.job;

import com.racs.commons.bean.HashGenerator;
import com.racs.commons.scheduler.SchedulerConfiguration;
import com.racs.commons.scheduler.UpdateStatusUserADScheduler;
import com.racs.core.entities.Schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Calendar;

/**
 * <p>
 * Tarea de consulta a Bd de la frecuiencia de ejecución de la consulta de
 * estados de usuarios en Active dirtectory
 * </p>
 * 
 * @author team disca
 */
@Component
public class ReadDBJob implements Job {

	SchedulerConfiguration schedulerConfiguration = new SchedulerConfiguration();

	private static Logger _log = LoggerFactory.getLogger(ReadDBJob.class);

	public ReadDBJob() {
	}

	/**
	 * <p>
	 * Metodoq ue es invocado en función de la planificación efectuada por
	 * <code>{@link org.quartz.Scheduler}</code> cuando un
	 * <code>{@link org.quartz.Trigger}</code> es disparado y esta asociado con la
	 * tarea <code> ReadDBJobPlanning Job</code>.
	 * </p>
	 * 
	 * @throws JobExecutionException
	 * 
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		UpdateStatusUserADScheduler updateStatusUserADScheduler = null;
		Schedule sch = null;
		Connection conexion = null;
		Calendar cal = null;
		int hora =0, minutos = 0;
		HashGenerator hash = new HashGenerator();
		
		try {
			Class.forName(schedulerConfiguration.getDriverClassDB());
			conexion = DriverManager.getConnection(schedulerConfiguration.getUrlDB(), schedulerConfiguration.getUserDB(), schedulerConfiguration.getPasswordDB());
			cal = Calendar.getInstance();
			hora = cal.get(Calendar.HOUR_OF_DAY);
			minutos = cal.get(Calendar.MINUTE);
			
			_log.info("JOB ReadDBJob: Buscando en BD: "+String.valueOf(hora)+":"+String.valueOf(minutos));
			
			Statement s = conexion.createStatement();
			ResultSet rs = s.executeQuery("SELECT id, hours, minutes, enabled, name " + "FROM schedule where hours = "
					+ hora + " and minutes between " + minutos + " and " + (minutos + 29) + " and enabled = 'true'");

			while (rs.next()) {
				sch = new Schedule();
				sch.setId(rs.getLong(1));
				sch.setHours(rs.getInt(2));
				sch.setMinutes(rs.getInt(3));
				sch.setEnabled(rs.getBoolean(4));
				sch.setName(rs.getString(5));
				
				_log.info("JOB ReadDBJob: La tarea"+ sch.getName() +" está planificada entre las "+ 
						String.valueOf(hora)+":"+String.valueOf(minutos)+" y las "+String.valueOf(hora)+":"+String.valueOf(minutos+29));
				/**
				 * Iniciar el cronTrigger de la tarea programa de actualizar estado de usuarios.
				 */
				updateStatusUserADScheduler = new UpdateStatusUserADScheduler();
				updateStatusUserADScheduler.setHashTrigger(hash.getRandom());
				updateStatusUserADScheduler.run();
			}
			
		} catch (Exception e) {
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
		// This job simply prints out its job name and the
		// date and time that it is running
		JobKey jobKey = context.getJobDetail().getKey();
		_log.info("--->  Tarea ReadDBJob --> " + jobKey + " se ejecuto: " + String.valueOf(hora)+":"+String.valueOf(minutos)+"");
	}
}
