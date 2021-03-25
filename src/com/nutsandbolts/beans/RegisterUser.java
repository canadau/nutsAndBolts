package com.nutsandbolts.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.nutsandbolts.tools.DBConnection;
import com.nutsandbolts.tools.EmailValidator;
import com.nutsandbolts.tools.PasswordValidator;
import com.nutsandbolts.tools.ShowMessages;

@ManagedBean (name = "registerUser")
@RequestScoped
public class RegisterUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	public RegisterUser() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	/*********** Methods start here ***********/
	
	public boolean isExistUser() {
		boolean stat = false;
		Connection conn = null;
		PreparedStatement pst = null;
		int count = 0;
		
		try {
			
			String createSQL = "SELECT email FROM userLogin WHERE email = ?;";
			conn = DBConnection.getConnection();

			pst = conn.prepareStatement(createSQL);	
			pst.setString(1, email);

			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				count++;				
			}			
			
			if (count > 0) {				
				stat = true; 				
			} 
				
			pst.close();
		} catch (Exception e) {
			e.getCause();
			System.out.println(e.getCause());
		} finally {
			DBConnection.close(conn);
		}
		return stat;
	}
	
	public void registerUser() {
		
		Connection conn = null;
		PreparedStatement pst = null;
		
		if(!PasswordValidator.isValid(password)) {
			
			ShowMessages.showErrorMessage("Password must be at least one lowercase letter, "
					+ "one uppercase letter, one number, a special character, and 8 characters long.");
			return;
			
		} else if (!EmailValidator.isLowercaseEmail(email)) {
			
			ShowMessages.showErrorMessage("The email must be lower case, and you need to use your employee email to complete this process, 'example@nab.com'");
			return;
			
		} else if (!isExistUser()) {
			try {
				
				String createSQL = "INSERT INTO userLogin (firstName, lastName, email, password) "
						+ "VALUES (?,?,?,?);";
				conn = DBConnection.getConnection();
				firstName = firstName.trim().substring(0,1).toUpperCase() + firstName.substring(1);
				lastName = lastName.trim().substring(0,1).toUpperCase() + lastName.substring(1);
				email = email.trim().toLowerCase();
				pst = conn.prepareStatement(createSQL);	
				pst.setString(1, firstName);
				pst.setString(2, lastName);
				pst.setString(3, email);
				pst.setString(4, password);

				int rs = pst.executeUpdate();
				
				if (rs > 0) {
					ShowMessages.showSuccessMessage("The account was created successfully");				
				} else {
					ShowMessages.showErrorMessage("Error, please try again!");
				}
					
				pst.close();
			} catch (Exception e) {
				e.getCause();
				System.out.println(e.getCause());
			} finally {
				DBConnection.close(conn);
			}
		} else {
			ShowMessages.showErrorMessage("You already have an account please call customer support for help!");
		}
				
	}
	

}
