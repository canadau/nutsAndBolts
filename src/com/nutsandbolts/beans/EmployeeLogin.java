package com.nutsandbolts.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

import com.nutsandbolts.Employee;
import com.nutsandbolts.tools.DBConnection;
import com.nutsandbolts.tools.SessionManagement;
import com.nutsandbolts.tools.ShowMessages;

@ManagedBean (name="employeelogin")
@SessionScoped
public class EmployeeLogin implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String userName;
	private String email;
	private String password;
	public Employee employee;
	public static boolean isLoggedin = false;
	public Employee tempEmployee = null;
	public int attempts = 0;
	private long start = 0;
	private long finish = 0;
	private long timeElapsed;
	private long timeElapsedAfterSusp;
	
	
	public EmployeeLogin() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public static boolean isLoggedin() {
		return isLoggedin;
	}

	public static void setLoggedin(boolean isLoggedin) {
		EmployeeLogin.isLoggedin = isLoggedin;
	}

	
	
	// --------- Methods start here -------------------

	public String validateUsernamePassword() {
		timeElapsedAfterSusp = System.currentTimeMillis() - finish;
		System.out.println(timeElapsedAfterSusp);
		boolean valid = validatePass(email, password);

		if (valid &&(attempts < 3 ||timeElapsedAfterSusp > 900000 ) && (timeElapsed < 900000 || timeElapsedAfterSusp > 900000)) {
			employee = tempEmployee;
			tempEmployee = null;
			HttpSession session = SessionManagement.getSession();
			session.setAttribute("admin", email);
			setLoggedin(true);

			return "index?faces-redirect=true";
		
		} else if (isExistEmail(email)) {
			
			if (attempts < 2) {
				attempts++;
				System.out.println("Attempt "+attempts);
				start = System.currentTimeMillis();
				ShowMessages.showErrorMessage("Incorrect username or password!");
	
			} else if (isExistEmail(email) && attempts <= 2 ) {
				attempts++;
				if(attempts == 3) finish = System.currentTimeMillis();
				ShowMessages.showErrorMessage("Your account was suspended, try after 15 minutes");
				System.out.println("Attempt #1 "+attempts);
				timeElapsed = start - finish;
				//System.out.println(timeElapsed);			
			} else {
				ShowMessages.showErrorMessage("Your account was suspended, try after 15 minutes");
				System.out.println("Attempt #2 "+attempts);
				//System.out.println(timeElapsed);
			}
			
			return "login";
			
		} else {					
				ShowMessages.showErrorMessage("Incorrect username or password!");
			return "login";
		}
	}	
	
	public String logout() {
		HttpSession session = SessionManagement.getSession();
		session.invalidate();
		isLoggedin = false;		
		employee.setEmail(""); 
		employee.setPassword("");
		employee = null;
		return "login.xhtml?faces-redirect=true";
	}
	
	
	public boolean validatePass(String email, String password) {
		Connection conn = null;
		PreparedStatement pst = null;
		int count = 0;
		
		try {
			
			String createSQL = "SELECT id, userName, email, password FROM employeeLogin WHERE email = ? AND password = ?;";
			conn = DBConnection.getConnection();

			pst = conn.prepareStatement(createSQL);	
			pst.setString(1, email);
			pst.setString(2, password);

			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				tempEmployee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));				
				count++;
				System.out.println(tempEmployee.getUserName());
				
			}			
			
			if (count > 0) {				
				return true; 				
			} 
				
			pst.close();
		} catch (Exception e) {
			e.getCause();
			System.out.println(e.getCause());
		} finally {
			DBConnection.close(conn);
		}
		
		return false;
			
	}	
	
	public boolean isExistEmail(String email) {
		Connection conn = null;
		PreparedStatement pst = null;
		int count = 0;
		
		try {
			
			String createSQL = "SELECT email FROM employeeLogin WHERE email = ?;";
			conn = DBConnection.getConnection();

			pst = conn.prepareStatement(createSQL);	
			pst.setString(1, email);

			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				
				count++;
				System.out.println(rs.getString(1));				
				
			}			
			
			if (count > 0) {				
				return true; 				
			} 
				
			pst.close();
		} catch (Exception e) {
			e.getCause();
			System.out.println(e.getCause());
		} finally {
			DBConnection.close(conn);
		}
		
		return false;
	}
		

}
