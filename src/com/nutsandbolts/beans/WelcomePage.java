package com.nutsandbolts.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nutsandbolts.HomePage;


@ManagedBean(name="page1")
@SessionScoped
public class WelcomePage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public WelcomePage() {}		
	
	/*
	 * public String dateFromDatabase() { HomePage homePage = new HomePage(); return
	 * "The login date was "+ homePage.dateFromDB() ; }
	 */	
	
	/*
	 * public Date getCurrentUtcTime() throws ParseException { SimpleDateFormat
	 * simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss");
	 * simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-5")); SimpleDateFormat
	 * localDateFormat = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss"); return
	 * localDateFormat.parse( simpleDateFormat.format(new Date()) ); }
	 */
	
	public static void jScript()  {
		 System.out.println("Hello");
		 HomePage getUtilHomePage = new HomePage();
		 getUtilHomePage.dateFromDB();
	}
	
	public String timeFromSQL()  {
		String str;
		 HomePage getUtilHomePage = new HomePage();
		 str = getUtilHomePage.dateFromDB();
		 System.out.println(str);
		 return str;
	}

}