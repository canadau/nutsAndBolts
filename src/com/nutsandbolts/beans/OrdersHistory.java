package com.nutsandbolts.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.nutsandbolts.Products;
import com.nutsandbolts.tools.DBConnection;
import com.nutsandbolts.tools.SessionManagement;
/*
 * This class to manage the order history page
 */

@ManagedBean (name = "ordersHistory")
@SessionScoped
public class OrdersHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public List<String> orderNumberList = new ArrayList<>();
	public List<Products> productsList = new ArrayList<>();
	public Products products;

	
	// Method to get all the order numbers associate with the user name
	public List<String> ordersNumbers() {

		orderNumberList.clear();
		PreparedStatement pst = null;
		Connection conn = null;
		ResultSet rSet = null;
		
		String sqlQuery = "SELECT DISTINCT orderNumber FROM orders WHERE associateUser = ? ";
		
		try {
			
			conn = DBConnection.getInstance().getConnection();
			pst = conn.prepareStatement(sqlQuery);
			pst.setString(1, SessionManagement.getUserId());
			rSet = pst.executeQuery();
			
			while (rSet.next()) {
				orderNumberList.add(rSet.getString(1));
				
			}
			//System.out.println(orderNumberList);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return orderNumberList;
	}
	
	// Method to get all the products that associate with the order number
	public List<Products> productsList(String orderNumber) {

		productsList.clear();
		PreparedStatement pst = null;
		Connection conn = null;
		ResultSet rSet = null;
		
		String sqlQuery = "SELECT sku, name, price, qty, dateTime FROM orders WHERE orderNumber = ?";
		
		try {
			
			conn = DBConnection.getInstance().getConnection();
			pst = conn.prepareStatement(sqlQuery);
			pst.setString(1, orderNumber);
			rSet = pst.executeQuery();
			
			while (rSet.next()) {
				products = new Products(rSet.getInt(1), rSet.getString(2), rSet.getDouble(3), rSet.getInt(4), rSet.getString(5));
				productsList.add(products);
			}
			//System.out.println(productsList);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return productsList;
	}
	
	// Calculate total items for a single order number
	public int quantity(List<Products> list) {
		int qty = 0;
		
		for(Products pro : list) {
			qty = pro.qty + qty;
		}		
		return qty;
	}
	
	// Calculate the sub-total for a single order number
	public double subTotal(List<Products> list) {
		double subtotal = 0;
		DecimalFormat df = new DecimalFormat("#.00");
		
		for(Products pro : list) {
			subtotal = pro.getPrice() * pro.getQty() + subtotal;
		}		
		return Double.valueOf(df.format(subtotal));
	}
	
	// Ohio tax rate 7.5 -- this method calculates the tax
	public double tax(List<Products> list) {
		DecimalFormat df = new DecimalFormat("#.00");
		return Double.valueOf(df.format(subTotal(list) * .075));
	}
	
	// Calculate the total for a single order number
	public double totalCart(List<Products> list) {
		DecimalFormat df = new DecimalFormat("#.00");
		
		return Double.valueOf(df.format(subTotal(list) + tax(list)));
	}
	
	// Getter and setter start here
	public List<String> getOrderNumberList() {
		return orderNumberList;
	}

	public void setOrderNumberList(List<String> orderNumberList) {
		this.orderNumberList = orderNumberList;
	}

	public List<Products> getProductsList() {
		return productsList;
	}

	public void setProductsList(List<Products> productsList) {
		this.productsList = productsList;
	}

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}
	// Getter and setter ends here
	
	
}
