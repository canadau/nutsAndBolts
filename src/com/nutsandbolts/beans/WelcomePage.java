package com.nutsandbolts.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.nutsandbolts.HomePage;

@ManagedBean(name="homepage")
@RequestScoped
public class WelcomePage {

	public WelcomePage() {}		
	
	public String dateFromDatabase() {
		HomePage homePage = new HomePage();		
		return "The login date was "+ homePage.dateFromDB() ;
	}		
	
}