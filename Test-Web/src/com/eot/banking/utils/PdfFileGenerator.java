package com.eot.banking.utils;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;





@Service
public class PdfFileGenerator {
	
	 private static Font font = new Font(Font.HELVETICA, 18, Font.BOLD);
	 private static Font smallBold = new Font(Font.HELVETICA, 12, Font.BOLD);
	 //private static Font fontHeading = FontFactory.getFont(Font.HELVETICA, 10, Font.BOLD, BaBaseColor.BLACK);
	 //private static Font fontItem = FontFactory.getFont(Font.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
	 
	public byte[] genaratePdfFile(){
		
		Document doc = new Document(PageSize.A3, 50, 50, 10, 50);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		
		// Creation of paragraph object
					
			
		try { 
			PdfWriter.getInstance(doc, byteStream);
			doc.open();
			Chunk c = new Chunk("Outlet Report",
					FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLDITALIC, Color.BLACK));
			c.setBackground(Color.GRAY, 255.1f, 8.2f, 255.1f, 8.1f);
			Paragraph title = new Paragraph(c);
			title.setAlignment(Element.ALIGN_CENTER);
			doc.add(title);
			
			addMetaData(doc, "mGurush");
			//addTitlePage(doc, "User1");
			createTable(doc);
			doc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteStream.toByteArray();

	}
	
	private static void addMetaData(Document document, String titleName) {
        document.addTitle(titleName);
        document.addSubject("PDF Report");
    }
	
	 private static void addTitlePage(Document document, String printedBy)
	            throws DocumentException {
	        Paragraph preface = new Paragraph();
	        addEmptyLine(preface, 1);
	        preface.add(new Paragraph("mGurush", font));
	        addEmptyLine(preface, 1);
	        preface.add(new Paragraph(
	                "Logged in: " + printedBy + "        " +"Date:"+ new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	                smallBold));
	        addEmptyLine(preface, 1);
	        document.add(preface);
	    }
	 
	 private static void addEmptyLine(Paragraph paragraph, int number) {
	        for (int i = 0; i < number; i++) {
	            paragraph.add(new Paragraph(" "));
	        }
	    }
	 
	 private static void createTable(Document document)
	            throws DocumentException {
	        PdfPTable table = new PdfPTable(3);

	        // t.setBorderColor(BaseColor.GRAY);
	        // t.setPadding(4);
	        // t.setSpacing(4);
	        // t.setBorderWidth(1);

	        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);

	        c1 = new PdfPCell(new Phrase("Table Header 2"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);

	        c1 = new PdfPCell(new Phrase("Table Header 3"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        table.setHeaderRows(1);

	        table.addCell("1.0");
	        table.addCell("1.1");
	        table.addCell("1.2");
	        table.addCell("2.1");
	        table.addCell("2.2");
	        table.addCell("2.3");

	        document.add(table);
	    }
}
