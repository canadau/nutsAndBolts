package com.nutsandbolts.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.NamingException;

import com.nutsandbolts.Products;
import com.nutsandbolts.tools.DBConnection;
import com.nutsandbolts.tools.SessionManagement;
import com.nutsandbolts.tools.ShowMessages;

// ApplicationScoped Java Class "It lasts as long as the app is running on AWS server"
@ManagedBean(name = "cart")
@SessionScoped
public class Cart implements Serializable {

	private static final long serialVersionUID = 1L;

	public Cart() {}
	
	//
	//public int isInStock;
	
	// A product template
	public Products products2 = null;
	
	// Unique Identifier
	public String UID = null;
	
	// A list of products no duplicated items only use qty to know the quantity
	public List<Products> productsListNoDub = new ArrayList<Products>();
	public List<Products> productsReceipt = new ArrayList<Products>();

	// Add to cart method
	public void addToCart2(Products pro) {
		int existIndex = -1;
		boolean isExist = false;

		for (Products pp : productsListNoDub) {
			if (pp.getSku() == pro.getSku()) {
				isExist = true;
				existIndex = productsListNoDub.indexOf(pp);
				//System.out.println("Index of " +productsListNoDub.indexOf(pp));
			}
		}

		//System.out.println("IsExist " +isExist);

		if (isExist) {

			productsListNoDub.get(existIndex).newQty++;

		} else {
			
			products2 = new Products(pro.getSku(),pro.getName(),pro.getDescription(),pro.getPrice(),pro.getQty(), 1);
			productsListNoDub.add(products2);
		}

		System.out.println(pro.getName() + " was added");
		ShowMessages.addMessage(FacesMessage.SEVERITY_INFO, "Info Message", "The product was added to the cart");
		//return "products?faces-redirect=true";
	}

	// Clear all items in the cart
	public void clearAll() {
		productsListNoDub.clear();
	}

	// Clear a single item in the cart
	public void clearItem(Products prod) { 
		
		int index = 0; 
		for(Products pr : productsListNoDub) { 
			if (pr.equals(prod)) { 
				index = productsListNoDub.indexOf(pr);
				break; 
			} 
		} 
		productsListNoDub.remove(index); 
	}

	// To get a list of products form this Java class
	public List<Products> productsList() { return productsListNoDub; }
	
	// Total items in the cart
	public int quantity(List<Products> list) {
		int qty = 0;
		
		for(Products pro : list) {
			qty = pro.newQty + qty;
		}		
		return qty;
	}
	
	// To calculate the subtotal 
	public double subTotal(List<Products> list) {
		double subtotal = 0;
		DecimalFormat df = new DecimalFormat("#.00");
		
		for(Products pro : list) {
			subtotal = pro.getPrice() * pro.getNewQty() + subtotal;
		}		
		return Double.valueOf(df.format(subtotal));
	}
	
	// I am using Ohio tax rate 7.5
	public double tax(List<Products> list) {
		DecimalFormat df = new DecimalFormat("#.00");
		return Double.valueOf(df.format(subTotal(list) * .075));
	}
	
	//Calculate the total
	public double totalCart(List<Products> list) {
		DecimalFormat df = new DecimalFormat("#.00");
		
		return Double.valueOf(df.format(subTotal(list) + tax(list)));
	}
	
	// Increase item qty
	public void increaseQty(Products pro) {
		int itemIndex;
		itemIndex = productsListNoDub.indexOf(pro);
		if (itemIndex >= 0 && pro.newQty >=0) productsListNoDub.get(itemIndex).newQty++;
		System.out.println("increase");
	}
	
	// Decrease item qty
	public void decreaseQty(Products pro) {
		int itemIndex;
		itemIndex = productsListNoDub.indexOf(pro);
		if (itemIndex >= 0 && pro.newQty >=1) productsListNoDub.get(itemIndex).newQty--;
		if (pro.newQty == 0) clearItem(pro);
		System.out.println("decrease");	
	}
	
	// Getter for the productsListNoDub list
	public List<Products> getProductsListNoDub() {
		return productsListNoDub;
	}
	
	// Setter for the productsListNoDub list
	public void setProductsListNoDub(List<Products> productsListNoDub) {
		this.productsListNoDub = productsListNoDub;
	}
	
	// A simple method to get the order number
	public String getUID() {
		return UID;
	}
	
	// A method to create a receipt and clear the cart
	public String viewReceipt() {
		orderNumber();		
		productsReceipt.clear();
		productsReceipt.addAll(productsListNoDub);
		reduceInventory(productsReceipt);
		clearAll();
		
		return "receipt?faces-redirect=true";
	}
	
	// A method to get a list of products for the receipt page
	public List<Products> productsReceipt() {
		return productsReceipt;
	}
	
	// To generate a random number as an order number
	public String orderNumber() {
		UID = UUID.randomUUID().toString().substring(19);
		return UID;
	}
	
	//method to reduct the inventory
	public void reduceInventory(List<Products> list) {
		PreparedStatement pst = null; 
		Connection conn = null;
		
		//sql query that updates the DB
		String sqlQuery = "UPDATE products SET qty = ? WHERE sku = ?";
		
		//holds the difference between the products qty in the inventory and the cart
		int inventoryProductQty = 0;
			
		// Try block to keep the app safe in case of resource failure "table not found", "database not found", or "database is down" 
			try {
				
				conn = DBConnection.getInstance().getConnection();
				
				pst = conn.prepareStatement(sqlQuery);
				
				// For each loop to loop through the products list, calculate the qty, and push the new qty to our database
				for (Products prod : list) {
					inventoryProductQty = prod.getQty() - prod.getNewQty();
					pst.setInt(1, inventoryProductQty);
					pst.setInt(2, prod.getSku());
					pst.executeUpdate();
				}
				// catch statement to catch any error while connecting to our database. This saves the app from crash in case of an error occurs
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			} catch (NamingException e) {
				System.out.println(e.getMessage());
			}
	}
	
	

}
