package com.nutsandbolts.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.nutsandbolts.tools.DBConnection;
import com.nutsandbolts.tools.ShowMessages;

@ManagedBean(name="addProductB")
@RequestScoped
public class RegisterUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	public RegisterUser() {}

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
	
	public void addProductToDB( ) {
		Connection conn = null;
		
		try {
			
			String createSQL = "INSERT INTO userLogin (firstName, lastName, email, password) VALUES (?, ?, ?, ?)";
			conn = DBConnection.getConnection();

			PreparedStatement pst = conn.prepareStatement(createSQL);	
			pst.setString(1, firstName);
			pst.setString(2, lastName);
			pst.setString(3, email);
			pst.setString(4, password);

			int rs = pst.executeUpdate();
			
			if (rs > 0) {
				ShowMessages.showSuccessMessage("The user was added successfully");
			} else {
				 ShowMessages.showErrorMessage("Error, the user was not added, please try again"); 
			}			
				
			pst.close();
		} catch (Exception e) {
			e.getCause();
			System.out.println(e.getCause());
		} finally {
			DBConnection.close(conn);
		}		
		
	}	


}



