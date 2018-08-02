package com.racs.commons.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Component;

@Component
public class ConnectionFTP  {
	
	/*private String server = "10.0.0.32";
	private int port = 2121;
	private String user = "admin";
	private String pass = "12345";*/
	private FTPClient ftpClient = new FTPClient();
	
	public ConnectionFTP(){} 
	
	
    public void connectionDownloadFTP(String server,Integer port, String user,String pass) {
    
	    try {
	    	System.out.println("Conexion->>>>>>>>"+server+port+user+pass);
	        ftpClient.connect(server, port);
	        ftpClient.login(user, pass);
	        ftpClient.enterLocalPassiveMode();
	        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	
	     
	        String remoteFile1 = "/data/data/com.disca.openalprsample/databases/ReconocimientoPlaca.db";
	        File downloadFile1 = new File("C:\\Users\\ESTACION1\\Desktop\\proyec\\ReconocimientoPlaca.db");
	        OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
	        boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
	        outputStream1.close();
	
	        if (success) {
	            System.out.println("File #1 has been downloaded successfully.");
	        }
	
	      
	        outputStream1.close();
	     
	
	    } catch (IOException ex) {
	        System.out.println("Error: " + ex.getMessage());
	        ex.printStackTrace();
	    } finally {
	        try {
	            if (ftpClient.isConnected()) {
	                ftpClient.logout();
	                ftpClient.disconnect();
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
    }
	

}


