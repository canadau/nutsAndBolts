package com.nutsandbolts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import com.nutsandbolts.tools.DBConnection;


public class Products {
	private int sku;
	private String name;
	private String description;
	private double price;
	public int qty;
	public int newQty;
	private String associateUser;
	private String orderNumber;
	private String dateTime;
	private String date;
	private int day;
	private int week;
	private int id;
	private String picture;
	private Boolean greatDeal;
	
	public Products() {}
	public Products(int sku, String name, String description, double price, int qty, String picture) {
		super();
		this.sku = sku;
		this.name = name;
		this.description = description;
		this.price = price;
		this.qty = qty;
		this.picture = picture;
	}
	
	// For home page
	public Products(int sku, String name, String description, double price, int qty, String picture, Boolean greatDeal) {
		super();
		this.sku = sku;
		this.name = name;
		this.description = description;
		this.price = price;
		this.qty = qty;
		this.picture = picture;
		this.greatDeal = greatDeal;
	}
	
	// For cart and receipt pages
	public Products(int sku, String name, String description, double price, int qty, int newQty, String picture) {
		super();
		this.sku = sku;
		this.name = name;
		this.description = description;
		this.price = price;
		this.qty = qty;
		this.newQty = newQty;
		this.picture = picture;
	}
	
	// For order history page
	public Products(int sku, String name, double price, int qty, String dateTime) {
		super();
		this.sku = sku;
		this.name = name;
		this.price = price;
		this.qty = qty;
		this.dateTime = dateTime;
	}
	
	// For weekly sales page
	public Products(int day, int week, String name,Double price, int qty, String date, int id) {
		super();
		this.day = day;
		this.week = week;
		this.name = name;
		this.price = price;
		this.qty = qty;
		this.date = date;	
		this.id = id;
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

	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public int getNewQty() {
		return newQty;
	}
	public void setNewQty(int newQty) {
		this.newQty = newQty;
	}
	
	public String getAssociateUser() {
		return associateUser;
	}
	public void setAssociateUser(String associateUser) {
		this.associateUser = associateUser;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getDate() {
		return date;
	}
	public void setString(String date) {
		this.date = date;
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
		
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public Boolean getGreatDeal() {
		return greatDeal;
	}
	
	public void setGreatDeal() {
		this.greatDeal = greatDeal;
	}
	
	public void addProduct(int sku, String name, String description, double price ) {

		try {
			System.out.println("It is going to push the product to DB");

			String createSQL = "INSERT INTO products (sku, name, description, price) VALUES (?, ?, ?, ?)";
			DBConnection inst = DBConnection.getInstance();
			Connection conn = inst.getConnection();

			PreparedStatement pst = conn.prepareStatement(createSQL);	
			pst.setInt(1, sku);
			pst.setString(2, name);
			pst.setString(3, description);
			pst.setDouble(4, price);

			pst.executeQuery();


		} catch (Exception e) {
			e.getMessage();
		}		 
	}
	
	
	
}
	
	
	
