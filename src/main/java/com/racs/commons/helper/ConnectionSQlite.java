package com.racs.commons.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

@Component
public class ConnectionSQlite {
	
	public ConnectionSQlite() {}

	public Connection connectSqlite() {
		
        Connection conn = null;
        try {
        	Class.forName ("org.sqlite.JDBC");
            // db parameters
        	String url = "jdbc:sqlite:C:\\Users\\ESTACION1\\Desktop\\proyec\\ReconocimientoPlaca.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established." + conn  );
            
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage()+ conn);
        } 
        
		return conn;
    }

}
