package com.racs.commons.helper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

public class Base64Encoder {
	
	
	
	
	public static String imageToBase64(byte[] data) {
		
		String path = "C:\\Users\\ESTACION1\\Desktop\\proyec\\RACSystem\\src\\main\\resources\\static\\images\\output.jpg";
		
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
	        BufferedImage bImage2;
			bImage2 = ImageIO.read(bis);
			ImageIO.write(bImage2, "jpg", new File(path));
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return path;
	}

	
   public static Optional<byte[]> base64ToImage(String path) {
        int len = path.split("\\.").length;
        String ext = path.split("\\.")[len - 1];
        try {
            BufferedImage img = ImageIO.read(new File(path));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, ext, baos);
            return Optional.of(baos.toByteArray());
        } catch(IOException e) {
            return Optional.empty();
        }
    }
   
   public static void remove() {
		String path = "C:\\Users\\ESTACION1\\Desktop\\proyec\\RACSystem\\src\\main\\resources\\static\\images\\output.jpg";
		File file = new File(path);
        
        if(file.delete())
        {
            System.out.println("File deleted successfully");
        }
        else
        {
            System.out.println("Failed to delete the file");
        }
	   
   }


}
