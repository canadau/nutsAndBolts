package com.nutsandbolts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.nutsandbolts.tools.DBConnection;


public class Products {
	private int sku;
	private String name;
	private String description;
	private double price;
	
	public Products(int sku, String name, String description, double price) {
		super();
		this.sku = sku;
		this.name = name;
		this.description = description;
		this.price = price;
	}

	public int getSku() {
		return sku;
	}

	public void setSku(int sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}		
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void addProduct(int sku, String name, String description, double price ) {
		
		try {
			System.out.println("It is going to push the product to DB");

			String createSQL = "INSERT INTO products (sku, name, description, price) VALUES (?, ?, ?, ?)";
			Connection conn = DBConnection.getConnection();

			PreparedStatement pst = conn.prepareStatement(createSQL);	
			pst.setInt(1, sku);
			pst.setString(2, name);
			pst.setString(3, description);
			pst.setDouble(4, price);

			ResultSet rSet = pst.executeQuery();


		} catch (Exception e) {
			e.getMessage();
		}
		
		 
	}
	
}
	
	
	
