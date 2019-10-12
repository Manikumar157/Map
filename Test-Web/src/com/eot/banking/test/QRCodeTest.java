package com.eot.banking.test;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;

public class QRCodeTest {
	
	    public static void main (String[] args) throws Exception
	    {
	         //Step - 1 :Create Document object that will hold the code
	         Document qr_code_Example = new Document(new Rectangle(360, 852));
	         // Step-2: Create PdfWriter object for the document
	         PdfWriter writer = PdfWriter.getInstance(qr_code_Example, new FileOutputStream("QR_PDF_Output.pdf"));
	         // Step -3: Open document for editing
	         qr_code_Example.open();            
	         //Step-4: Create New paragraph for QR Summary
	         qr_code_Example.add(new Paragraph("Wow, we created a QR Code in PDF document using iText Java"));
	         //Step-5: Create QR Code by using BarcodeQRCode Class
	         BarcodeQRCode my_code = new BarcodeQRCode("Example QR Code Creation in Itext", 10, 10, null);
	         //Step-6: Get Image corresponding to the input string
	         Image qr_image = my_code.getImage();
	         //Step-7: Stamp the QR image into the PDF document
	         qr_code_Example.add(qr_image);
	         //Step-8: Close the PDF document
	         qr_code_Example.close();
	    }
	  }

