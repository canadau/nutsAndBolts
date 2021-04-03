package com.nutsandbolts.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

import com.nutsandbolts.Customer;
import com.nutsandbolts.Employee;
import com.nutsandbolts.tools.DBConnection;
import com.nutsandbolts.tools.EmailValidator;
import com.nutsandbolts.tools.SessionManagement;
import com.nutsandbolts.tools.ShowMessages;

@ManagedBean (name="employeelogin")
@SessionScoped
public class LoginManagement implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String userName;
	private String email;
	private String password;
	public Employee employee;
	public Employee tempEmployee = null;
	public Customer customer;
	public Customer tempCustomer = null;
	public int attempts = 0;
	private long start = 0;
	private long finish = 0;
	private long timeElapsed;
	private long timeElapsedAfterSusp;
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getTempCustomer() {
		return tempCustomer;
	}

	public void setTempCustomer(Customer tempCustomer) {
		this.tempCustomer = tempCustomer;
	}

	public long getTimeElapsedAfterSusp() {
		return timeElapsedAfterSusp;
	}

	public void setTimeElapsedAfterSusp(long timeElapsedAfterSusp) {
		this.timeElapsedAfterSusp = timeElapsedAfterSusp;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	private HttpSession session;
	
	
	public LoginManagement() {}

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
	
	
	// --------- Methods start here -------------------

	public String validateUsernamePassword() {
		timeElapsedAfterSusp = System.currentTimeMillis() - finish;
		System.out.println(timeElapsedAfterSusp);
		boolean valid = validatePass(email, password);

		if (valid &&(attempts < 3 ||timeElapsedAfterSusp > 900000 ) && (timeElapsed < 900000 || timeElapsedAfterSusp > 900000)) {
			if (!EmailValidator.isEmpEmail(email)) {
				
				customer = tempCustomer;
				tempCustomer = null;
				session = SessionManagement.getSession();
				session.setAttribute("customer", customer.getFirstName());
				session.setAttribute("name", customer.getFirstName());
			} else {
				
				employee = tempEmployee;
				tempEmployee = null;
				session = SessionManagement.getSession();
				session.setAttribute("admin", employee.getUserName());
				session.setAttribute("name", employee.getUserName());
			}

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
		employee = null;
		customer = null;
		/*
		 * employee.setEmail(""); employee.setPassword(""); 
		 */
		return "login.xhtml?faces-redirect=true";
	}
	
	
	public boolean validatePass(String email, String password) {
		Connection conn = null;
		PreparedStatement pst = null;
		int count = 0;
		
		if (!EmailValidator.isEmpEmail(email)) {
			
			try {
				
				String createSQL = "SELECT id, firstName, lastName, email, password FROM userLogin WHERE email = ? AND password = ?;";
				conn = DBConnection.getConnection();
				
				password = encryptThisString(password);

				pst = conn.prepareStatement(createSQL);	
				pst.setString(1, email);
				pst.setString(2, password);

				ResultSet rs = pst.executeQuery();
				
				if (rs.next()) {
					tempCustomer = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));				
					count++;
					System.out.println(tempCustomer.getFirstName());
					
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
			
		} else {
			
			try {
				
				String createSQL = "SELECT id, userName, email, password FROM employeeLogin WHERE email = ? AND password = ?;";
				conn = DBConnection.getConnection();
				
				//Hash the password with sha-512
				password = encryptThisString(password);
				
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
		}
		
		return false;
			
	}	
	
	public boolean isExistEmail(String email) {
		Connection conn = null;
		PreparedStatement pst = null;
		int count = 0;
		
		if (!EmailValidator.isEmpEmail(email)) {
			
			try {
				
				String createSQL = "SELECT email FROM userLogin WHERE email = ?;";
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
			
		} else {
			
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
		}
		
		return false;
	}
	
		//Method for hashing with sha-512
		public static String encryptThisString(String input)
	    {
	        try {
	            // getInstance() method is called with algorithm SHA-512
	            MessageDigest md = MessageDigest.getInstance("SHA-512");
	  
	            // digest() method is called
	            // to calculate message digest of the input string
	            // returned as array of byte
	            byte[] messageDigest = md.digest(input.getBytes());
	  
	            // Convert byte array into signum representation
	            BigInteger no = new BigInteger(1, messageDigest);
	  
	            // Convert message digest into hex value
	            String hashtext = no.toString(16);
	  
	            // Add preceding 0s to make it 32 bit
	            while (hashtext.length() < 32) {
	                hashtext = "0" + hashtext;
	            }
	  
	            // return the HashText
	            return hashtext;
	        }
	  
	        // For specifying wrong message digest algorithms
	        catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException(e);
	        }
	    }
		

}
