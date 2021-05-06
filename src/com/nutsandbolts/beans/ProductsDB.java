package com.nutsandbolts.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.nutsandbolts.Products;
import com.nutsandbolts.tools.DBConnection;
import com.nutsandbolts.tools.ShowMessages;

@ManagedBean (name = "products")
@RequestScoped
public class ProductsDB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Products product;
	private List<Products> products = new ArrayList<>();
	
	public ProductsDB() {}
	public List<Products> getProductFromDB(){		
		
		try {


			String createSQL = "SELECT sku, name, description, price, qty, picture, greatDeal FROM products ";
			DBConnection inst = DBConnection.getInstance();
			Connection conn = inst.getConnection();

			PreparedStatement pst = conn.prepareStatement(createSQL);	
			
			ResultSet rSet = pst.executeQuery();
			
			while (rSet.next()) {
				product = new Products(rSet.getInt(1), rSet.getString(2), rSet.getString(3), rSet.getDouble(4), rSet.getInt(5), rSet.getString(6), rSet.getInt(7));
				products.add(product);
			}

		} catch (Exception e) {
			e.getMessage();
		}
		return products;
	}
	
	public List<Products> getProductFromDBIfGreatDeal() {
		try {


			String createSQL = "SELECT sku, name, description, price, qty, picture , greatDeal FROM products ";
			DBConnection inst = DBConnection.getInstance();
			Connection conn = inst.getConnection();

			PreparedStatement pst = conn.prepareStatement(createSQL);	
			
			ResultSet rSet = pst.executeQuery();
			
			while (rSet.next()) {
				product = new Products(rSet.getInt(1), rSet.getString(2), rSet.getString(3), rSet.getDouble(4), rSet.getInt(5), rSet.getString(6), rSet.getInt(7));
				if (rSet.getBoolean(7)) {
				products.add(product);
				}
			}

		} catch (Exception e) {
			e.getMessage();
		}
		return products;
	}
	
	public void markGreatDeal(int sku, int deal) {
		System.out.println(deal);
		int greatDeal;
		if (deal == 1) {
			greatDeal = 0;
		} else { greatDeal = 1;}
	
		
		try {
			
			String createSQL = "UPDATE products SET greatDeal = ? WHERE sku = ?";
			DBConnection inst = DBConnection.getInstance();
			Connection conn = inst.getConnection();
			PreparedStatement pst = conn.prepareStatement(createSQL);
			pst.setInt(1, greatDeal);
			pst.setInt(2, sku);
			int set = pst.executeUpdate();
			
			if (set > 0) {
				ShowMessages.showSuccessMessage("The product was marked as great deal");
			} else {
				 ShowMessages.showErrorMessage("Error, please try again"); 
			}
			
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
