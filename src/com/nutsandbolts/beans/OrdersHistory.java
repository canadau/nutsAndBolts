package com.nutsandbolts.beans;

import java.io.Serializable;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
	public List<String> dateList = new ArrayList<>();
	public List<Products> productsList;
	public Products products;
	
	public HashMap<String, List<Products>> mapProducts = new HashMap<>();


	// Method to get all the order numbers associate with the user name
	public HashMap<String, List<Products>> ordersNumbers() {

		orderNumberList.clear();
		PreparedStatement pst = null;
		Connection conn = null;
		ResultSet rSet = null;
		
		String sqlQuery = "SELECT orderNumber FROM orders WHERE associateUser = ? GROUP BY orderNumber ORDER BY MIN(dateTime) DESC";
		
		try {
			
			conn = DBConnection.getInstance().getConnection();
			pst = conn.prepareStatement(sqlQuery);
			pst.setString(1, SessionManagement.getUserId());
			rSet = pst.executeQuery();
			
			while (rSet.next()) {
				orderNumberList.add(rSet.getString(1));	
				productsList = new ArrayList<>();
				mapProducts.put(rSet.getString(1), productsList(rSet.getString(1)));
			}
			//System.out.println(orderNumberList);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return mapProducts;
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
			productsList = new ArrayList<>();
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
	public String subTotal(List<Products> list) {
		double subtotal = 0;
		DecimalFormat df = new DecimalFormat("0.00");
		
		for(Products pro : list) {
			subtotal = pro.getPrice() * pro.getQty() + subtotal;
		}		
		return df.format(subtotal);
	}
	
	// Ohio tax rate 7.5 -- this method calculates the tax
	public String tax(List<Products> list) {
		DecimalFormat df = new DecimalFormat("0.00");
		df.setRoundingMode(RoundingMode.DOWN);
		double sub = Double.valueOf(subTotal(list));
		return df.format(sub * 0.075);
	}
	
	// Calculate the total for a single order number
	public String totalCart(List<Products> list) {
		DecimalFormat df = new DecimalFormat("0.00");
		
		return df.format(Double.valueOf(subTotal(list)) + Double.valueOf(tax(list)));
	}
	
	// to get the date time of an order
	public String getDatetime(List<Products> list) {		
		return list.get(0).getDateTime().substring(0,16);
	}
	
	// Method to get the date of the order
	public String getDateTime(String ordeNumber) {
		
		String time = productsList(ordeNumber).get(0).getDateTime();
		return time.substring(0,16);
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

	public HashMap<String, List<Products>> getMapProducts() {
		return mapProducts;
	}

	public void setMapProducts(HashMap<String, List<Products>> mapProducts) {
		this.mapProducts = mapProducts;
	}
	
	// Getter and setter ends here
	
	
}
