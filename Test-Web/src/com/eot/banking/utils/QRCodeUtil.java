package com.eot.banking.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;


public class QRCodeUtil {
	
	private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    
	public static BufferedImage createQRCode(String qrCodeData,
			String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
			throws WriterException, IOException {
		BitMatrix matrix = new MultiFormatWriter().encode(
				new String(qrCodeData.getBytes(charset), charset),
				BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
		//QRCode in png format
		  MatrixToImageWriter.writeToStream(matrix, "jpg", new ByteArrayOutputStream());
		
		int width = matrix.getWidth();
        int height = matrix.getHeight();
        
        // Converting BitMatrix to Buffered Image 
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        
		return image;
	}

	public static String readQRCode(String filePath, String charset, Map hintMap)
			throws FileNotFoundException, IOException, NotFoundException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
				new BufferedImageLuminanceSource(
						ImageIO.read(new FileInputStream(filePath)))));
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
				hintMap);
		return qrCodeResult.getText();
	}
	
	public static byte[] exportQRCodeInPDFFormat(BufferedImage image, String agentCode){
		byte[] imageInByte =null;
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", byteStream);
			imageInByte = byteStream.toByteArray();
			//byteStream.flush();
			Image qrimage = Image.getInstance(imageInByte);
			Document document = new Document(PageSize.A4);
			PdfWriter.getInstance(document, new FileOutputStream(agentCode+"_QRCode.pdf"));
			document.open();
			document.add(qrimage);
			document.close();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
		return imageInByte;
	}
}
