package com.eot.banking.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.zip.Deflater;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.bind.DatatypeConverter;

import Decoder.BASE64Decoder;


public class ImageUtil {
	

public static BufferedImage base64ToImageBuffer(byte [] imageByte) throws IOException{
		
		BufferedImage image = null;
       
        try {
            BASE64Decoder decoder = new BASE64Decoder();
         
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
         

        File compressedImageFile = new File("C:\\Users\\IDS\\Desktop\\compressed_image.jpg");
        OutputStream os = new FileOutputStream(compressedImageFile);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = (ImageWriter) writers.next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.4f);  // Change the quality value you prefer
        writer.write(null, new IIOImage(image, null, null), param);
        os.close();
        ios.close();
        writer.dispose();
        
        return image;
		
		
	}

    public static byte[] compressByteArray(byte[] bytes){
    
    ByteArrayOutputStream baos = null;
    Deflater dfl = new Deflater();
    dfl.setLevel(Deflater.BEST_COMPRESSION);
    dfl.setInput(bytes);
    dfl.finish();
    baos = new ByteArrayOutputStream();
    byte[] tmp = new byte[4*1024];
    try{
        while(!dfl.finished()){
            int size = dfl.deflate(tmp);
            baos.write(tmp, 0, size);
        }
    } catch (Exception ex){
         
    } finally {
        try{
            if(baos != null) baos.close();
        } catch(Exception ex){}
    }
     
    return baos.toByteArray();
}
    
    public static byte[] stringtoBase64(String Images){
    	BASE64Decoder decoder = new BASE64Decoder();
		String base64Image = Images.split(",")[1];
		byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);
    	return imageBytes;
    }
	
}
