package com.racs.commons.helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.racs.core.entities.AccessHistoryEntity;


public class DataTransfers {
		
	/*Entity*/
	private AccessHistoryEntity access;

	/*Connection to DB*/
    private ConnectionSQlite connectionSQlite;


	
	public List<AccessHistoryEntity> dataTransfersFromSQLiteToMySQL(Integer id) {
		
		
	    access = new AccessHistoryEntity() ;

	    
	    List<AccessHistoryEntity> list = new ArrayList<AccessHistoryEntity>();
        connectionSQlite = new ConnectionSQlite();
        
        /*Seleccionar el historico de una comunidad*/
        String sql = "SELECT * FROM accesshistory WHERE COM_ID = " + id; 

        try {
        		/*hago la conexion a la Base de datos*/
	    	 Connection conn = connectionSQlite.connectSqlite();
	    	 
	    	 /*se verifica si la conexion con exito*/
	    	 if(conn != null) {
	    		 
	    		 Statement stmt  = conn.createStatement();
	    	     /*Se ejecuta la consulta sql*/
		         ResultSet rs    = stmt.executeQuery(sql);
		       
		        /* loop through the result set*/
		        while (rs.next()) {
		        
		        	access.setDate(rs.getString("HIS_DATE"));
		        	access.setHour(rs.getString("HIS_HOUR"));
		        	access.setPhotho(rs.getBytes("HIS_PHOTO"));
		        	access.setTypeaccess(rs.getString("HIS_TYPEACCESS"));
		        	access.setTypesecurity(rs.getString("HIS_TYPESECURITY"));
		        	access.setIdOwner(rs.getInt("OWN_ID"));
		        
		        	list.add(access);
		        	
		        	access = new AccessHistoryEntity(); 
		            

		        }
		        
	    	 }
	        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
        }
        
		return list;
		
	}
	

}
