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
public class AddProductsBeans implements Serializable {
	private static final long serialVersionUID = 1L;
	private int fSku;
	private String fName;
	private String fDescription;
	private double fPrice;
	private int Quantity;
	private String picture;
	
	public AddProductsBeans() {}

	public int getSku() {
		return fSku;
	}

	public void setSku(int sku) {
		this.fSku = sku;
	}

	public String getName() {
		return fName;
	}

	public void setName(String name) {
		this.fName = name;
	}

	public String getDescription() {
		return fDescription;
	}

	public void setDescription(String description) {
		this.fDescription = description;
	}

	public double getPrice() {
		return fPrice;
	}

	public void setPrice(double price) {
		this.fPrice = price;
	}
	
	public int getQuantity() {
		return Quantity;
	}

	public void setQuantity(int Quantity) {
		this.Quantity = Quantity;
	}
	
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void addProductToDB( ) {
		Connection conn = null;
		
		try {
			
			String createSQL = "INSERT INTO products (sku, name, description, price, qty, picture) VALUES (?, ?, ?, ?, ?, ?)";
			DBConnection inst = DBConnection.getInstance();
			conn = inst.getConnection();

			PreparedStatement pst = conn.prepareStatement(createSQL);	
			pst.setInt(1, fSku);
			pst.setString(2, fName);
			pst.setString(3, fDescription);
			pst.setDouble(4, fPrice);
			pst.setInt(5, Quantity);
			pst.setString(6, picture);

			int rs = pst.executeUpdate();
			
			if (rs > 0) {
				ShowMessages.showSuccessMessage("The product was added successfully");
			} else {
				 ShowMessages.showErrorMessage("Error, the product was not added, please try again"); 
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
