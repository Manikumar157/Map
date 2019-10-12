package com.eot.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.eot.banking.dao.WebUserDao;

public class ClearingPoolDaoTest {

	public static void main(String[] args) throws Exception {

		String[] path = new String[]{
				"WebContent/WEB-INF/config/hibernateContext.xml","WebContent/WEB-INF/config/daoContext.xml"
		} ;

		ApplicationContext context = new FileSystemXmlApplicationContext(path);
		

		WebUserDao dao = (WebUserDao) context.getBean("webUserDao");
		
		
		//System.out.println("list -> " + dao.getUsersCount() );
		

	}

}
