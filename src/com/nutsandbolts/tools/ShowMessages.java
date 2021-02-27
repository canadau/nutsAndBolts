package com.nutsandbolts.tools;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ShowMessages {
	
	public static void showSuccessMessage(String msg) {
		
		  FacesContext facesContext = FacesContext.getCurrentInstance(); 
		  FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg); 
		  facesContext.addMessage(null, facesMessage);
		 
		
	}
	
	public static void showErrorMessage(String msg) {
		
		  FacesContext facesContext = FacesContext.getCurrentInstance(); 
		  FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg); 
		  facesContext.addMessage(null, facesMessage);
		 
		
	}

}
