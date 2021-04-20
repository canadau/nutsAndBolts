package com.nutsandbolts;

public class Orders {
	
	private int id;
	private String associateUser;
	private String orderNumber;
	private int sku;
	private String name;
	private float price;
	private int qty;
	private String dateTime;
	
	public Orders() {}
		
	public Orders(int id, String associateUser, String orderNumber, int sku, String name, float price, int qty, String dateTime) {
		super();
		this.id = id;
		this.associateUser = associateUser;
		this.orderNumber = orderNumber;
		this.sku = sku;
		this.name = name;
		this.price = price;
		this.qty = qty;
		this.dateTime = dateTime;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
		public String getDateTime(){
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	
}
