package com.nutsandbolts.beans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.nutsandbolts.HomePage;

@ManagedBean(name="homepage")
@RequestScoped
public class WelcomePage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public WelcomePage() {}		
	
	public String dateFromDatabase() {
		HomePage homePage = new HomePage();		
		return "The login date was "+ homePage.dateFromDB() ;
	}	
	
	public Date getCurrentUtcTime() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss");
        return localDateFormat.parse( simpleDateFormat.format(new Date()) );
    }
	
}