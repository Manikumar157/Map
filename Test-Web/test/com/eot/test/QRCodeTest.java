package com.eot.test;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.eot.banking.dto.QRCodeDTO;
import com.eot.banking.utils.JSONAdaptor;
import com.eot.banking.utils.QRCodeUtil;
import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import com.itextpdf.text.pdf.qrcode.ErrorCorrectionLevel;

public class QRCodeTest {
	
	
	public static void main(String[] args) {
		qrCode();
		//samplePrint();
    }
	
	private static void qrCode() {
		String filePath = "D://opt/QRCode/"+"12333"+"_QRCode.jpg";
		QRCodeDTO qrCodeDTO=new QRCodeDTO();
		PDDocument document = new PDDocument();
		PDFont fontNormal = PDType1Font.COURIER;
        PDFont fontBold = PDType1Font.COURIER_BOLD;
        
		qrCodeDTO.setAgentCode("MNQ-006");
		qrCodeDTO.setName("Naqui");
		qrCodeDTO.setState("KAR");
		qrCodeDTO.setCity("Bangalore");
		qrCodeDTO.setMobileNumber("9008402788");
		qrCodeDTO.setAddress("HSR Layout");
		String qrCodeData = new JSONAdaptor().toJSON(qrCodeDTO);
		String charset = "UTF-8";
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		try {
			BufferedImage bitImage = QRCodeUtil.createQRCode(qrCodeData, charset, hintMap, 350, 350);
			byte[] byteArr = QRCodeUtil.exportQRCodeInPDFFormat(bitImage, "MNQ-006");
			
			InputStream in = new FileInputStream(filePath);
			BufferedImage bimg = ImageIO.read(in);
			float width = bimg.getWidth();
			width=width+100f;
			float height = bimg.getHeight();
			height=height+100f;
			System.out.println("Width: "+width);
			System.out.println("Height: "+height);
			//PDPage page = new PDPage(new PDRectangle(width, height));
			PDPage page = new PDPage(new PDRectangle(width, height));
			document.addPage(page); 
			PDImageXObject img = JPEGFactory.createFromStream(document, new FileInputStream(filePath));
			
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.setFont(PDType1Font.COURIER, 12);
			
			contentStream.drawImage(img, 0, 100);
			contentStream.setLeading(15f);
			contentStream.beginText();
			
			//Setting Name
			contentStream.newLineAtOffset(37,110);
			contentStream.setFont(fontBold, 12);
			contentStream.showText("Name: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getName());
			
			//Setting Code
			contentStream.newLine();
			contentStream.setFont(fontBold, 12);
			contentStream.showText("Code: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getAgentCode());
			
			//Setting MobileNumber
			contentStream.newLine();
			contentStream.setFont(fontBold, 12);
			contentStream.showText("MobileNumber: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getMobileNumber());
			
			//Setting State
			contentStream.newLine();
			contentStream.setFont(fontBold, 12);
			contentStream.showText("State: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getState());
			
			//Setting City
			contentStream.newLine();
			contentStream.setFont(fontBold, 12);
			contentStream.showText("City: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getCity());;
			
			//Setting City
			contentStream.newLine();
			contentStream.setFont(fontBold, 12);
			contentStream.showText("Address: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getAddress());;
			
			contentStream.endText();
			contentStream.close();

			document.save("D://opt/QRCode/test.pdf");
			System.out.println("Done");
			document.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	private static void samplePrint() {
		PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDFont fontNormal = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        try {
			PDPageContentStream contentStream =new PDPageContentStream(document, page);
			contentStream.beginText();
			contentStream.newLineAtOffset(100, 600);
			contentStream.setFont(fontBold, 15);
			contentStream.showText("Name: ");
			contentStream.setFont(fontNormal, 15);
			contentStream.showText ("Rajeev");
			contentStream.newLineAtOffset(200, 00);
			contentStream.setFont(fontBold, 15);
			contentStream.showText("Address: " );
			contentStream.setFont(fontNormal, 15);
			contentStream.showText ("BNG");
			contentStream.newLineAtOffset(-200, -20);
			contentStream.setFont(fontBold, 15);
			contentStream.showText("State: " );
			contentStream.setFont(fontNormal, 15);
			contentStream.showText ("KAR");
			contentStream.newLineAtOffset(200, 00);
			contentStream.setFont(fontBold, 15);
			contentStream.showText("Country: " );
			contentStream.setFont(fontNormal, 15);
			contentStream.showText ("INDIA");
			contentStream.endText();
			contentStream.close();
			document.save("D://opt/QRCode/sample.pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
