package com.eot.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.eot.banking.service.BankService;

public class LocationDaoTest {

	public static void main(String[] args) throws Exception {

		String[] path = new String[]{
				"WebContent/WEB-INF/GIM-Web-servlet.xml","WebContent/WEB-INF/config/*.xml"
		} ;

		ApplicationContext context = new FileSystemXmlApplicationContext(path);

		/*LocationDao dao = (LocationDao) context.getBean("locationDao");

		for(int i = 1; i < 20 ; i++){
			
//			System.out.println(dao.getCountry(1));
			List<Country> countries = dao.getCountries();

			for(Country country : countries){
				System.out.println(country.getCountry());
			}
		}
		
		for(int i = 1; i < 20 ; i++){
			
//			System.out.println(dao.getCountry(1));
			List<TimeZone> timeZoneList = dao.getTimeZones();

			for(TimeZone timeZone : timeZoneList){
				System.out.println(timeZone.getTimeZoneDesc());
			}
		}*/

		BankService service = (BankService) context.getBean("testBean");
		
		System.out.println(service);
	}

}
