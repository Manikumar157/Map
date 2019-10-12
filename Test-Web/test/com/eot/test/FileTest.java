package com.eot.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		String fileName = "TestMT1.xml";
		StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"latin1\" ?>");
		appendLineToBuffer(buffer, "<SWIFT_msg_fields>");
		appendLineToBuffer(buffer, "<msg_format>S</msg_format>");
		appendLineToBuffer(buffer, "<msg_sub_format>I</msg_sub_format>");
		appendLineToBuffer(buffer, "<msg_sender>CTMISND0AXXX</msg_sender>");
		appendLineToBuffer(buffer, "<msg_receiver>ZYAASND0XXXX</msg_receiver>");
		appendLineToBuffer(buffer, "<msg_type>971</msg_type>");
		appendLineToBuffer(buffer, "<msg_priority />");
		appendLineToBuffer(buffer, "<msg_del_notif_rq />");
		appendLineToBuffer(buffer, "<msg_user_priority>0030</msg_user_priority>");
		appendLineToBuffer(buffer, "<msg_user_reference>" + "123" + "</msg_user_reference>");
		appendLineToBuffer(buffer, "<msg_copy_srv_id />");
		appendLineToBuffer(buffer, "<msg_fin_validation />");
		appendLineToBuffer(buffer, "<msg_pde>N</msg_pde>");
		appendLineToBuffer(buffer, "<block4>:20:" + "123");
		buffer.append("</block4>");
		appendLineToBuffer(buffer, "</SWIFT_msg_fields>");
		
		File file = new File( "C:\\Users\\manoj\\Desktop\\" + fileName );
		boolean fileCreated = file.createNewFile();
		if( !fileCreated ) {

			System.out.println("Not Created");
		}
		FileOutputStream outputStream = new FileOutputStream( file );
		outputStream.write(buffer.toString().getBytes());
		outputStream.close();

	}

	public static void appendLineToBuffer( StringBuffer buffer, String line ) {

		System.setProperty( "line.separator", "" + "\n\n" );

		buffer.append("\n\n" + line);
	}


}
