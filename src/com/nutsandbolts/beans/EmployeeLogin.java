package com.nutsandbolts.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
	public static Employee sessionCustomer = null;
	public int attempt = 0;
	public long start;
	public boolean valid;
	
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

	public static Employee getSessionCustomer() {
		return sessionCustomer;
	}

	public static void setSessionCustomer(Employee sessionCustomer) {
		EmployeeLogin.sessionCustomer = sessionCustomer;
	}

	public String validateUsernamePassword() {
		
		if (attempt < 2) {
		valid = validatePass(email, password);
		} else {
			valid = false;
		}
			
		if (attempt >= 3) {
			
			if (System.currentTimeMillis() - start > 900000) {
				// Only allow the timer to reset once 15 minutes have passed.
				attempt = 0;
				return "login";
			}
			ShowMessages.showErrorMessage("Too many failed login attempts.  Wait 15 minutes.");
			return "login";
		
		} else if (valid && attempt < 2) {

			HttpSession session = SessionManagement.getSession();
			session.setAttribute("admin", email);
			setLoggedin(true);
			attempt = 0;
			return "index?faces-redirect=true";
			
		} else {
			
			ShowMessages.showErrorMessage("Incorrect username or password!");
			attempt++;
			if (attempt >= 3) {
				// Get the start time for the counter.
				start = System.currentTimeMillis();				
			}
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
				employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
				sessionCustomer = employee;
				count++;
				System.out.println(employee.getUserName());
				
				if(employee.getEmail().equals(email) && !employee.getPassword().equals(rs.getString(3)) ) {
				attempt++;
				System.out.println(attempt);
				}
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
