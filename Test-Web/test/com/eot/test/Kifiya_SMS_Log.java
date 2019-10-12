package com.eot.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class Kifiya_SMS_Log {

	public static void main(String args[])throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn=DriverManager.getConnection("jdbc:mysql://213.55.97.58:3306/gatewaydb?noAccessToProcedureBodies=true","mmoney","mMoney@123");
		CallableStatement calstat=conn.prepareCall("{call sp_MobMoney(?,?)}");
		calstat.setString(1,"0911424357");
		calstat.setString(2,"Its Test MSG from MM");
		ResultSet rs = calstat.executeQuery();
		conn.close();
		calstat.close();
		System.out.println("Your data has been inserted into table.");
	}
	
}
