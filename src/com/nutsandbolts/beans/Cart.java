package com.nutsandbolts.beans;

import java.io.Serializable;
import java.math.RoundingMode;
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
	
	public String date;
	
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

		if (isExist && productsListNoDub.get(existIndex).newQty < pro.qty) {

			productsListNoDub.get(existIndex).newQty++;
			ShowMessages.addMessage(FacesMessage.SEVERITY_INFO, "Info Message", "The product was added to the cart");
			System.out.println(pro.getName() + " was added");
		
		} else if (existIndex == -1) {

			products2 = new Products(pro.getSku(),pro.getName(),pro.getDescription(),pro.getPrice(),pro.getQty(), 1, pro.getPicture());
			productsListNoDub.add(products2);
			ShowMessages.addMessage(FacesMessage.SEVERITY_INFO, "Info Message", "The product was added to the cart");
			System.out.println(pro.getName() + " was added");
		
		} else {
			ShowMessages.addMessage(FacesMessage.SEVERITY_ERROR, "Info Message", "Only "+pro.getQty()+" avaliable" );
		}
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
	public String subTotal(List<Products> list) {
		double subtotal = 0;
		DecimalFormat df = new DecimalFormat("0.00");
		
		for(Products pro : list) {
			subtotal = pro.getPrice() * pro.getNewQty() + subtotal;
		}		
		return df.format(subtotal);
	}
	
	// I am using Ohio tax rate 7.5
	public String tax(List<Products> list) {
		DecimalFormat df = new DecimalFormat("0.00");
		df.setRoundingMode(RoundingMode.DOWN);
		return df.format(Double.valueOf(subTotal(list)) * (0.075));
	}
	
	//Calculate the total
	public String totalCart(List<Products> list) {
		DecimalFormat df = new DecimalFormat("0.00");
		
		return df.format(Double.valueOf(subTotal(list)) + Double.valueOf(tax(list)));
	}
	
	// Increase item qty
	public void increaseQty(Products pro) {
		int itemIndex;
		itemIndex = productsListNoDub.indexOf(pro);
		
		if (itemIndex >= 0 && pro.newQty >=0 && productsListNoDub.get(itemIndex).getNewQty() < pro.getQty()) {
			productsListNoDub.get(itemIndex).newQty++;
		} else {
			ShowMessages.addMessage(FacesMessage.SEVERITY_ERROR, "Info Message", "Only "+pro.getQty()+" avalabile");
		}
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
	public String viewReceipt(String date) {
		orderNumber();		
		productsReceipt.clear();
		productsReceipt.addAll(productsListNoDub);
		pushProductsToDB(productsReceipt, date);
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
	
	// push a list of products from the cart to the database after buying
			public void pushProductsToDB(List<Products> products, String date) {
				PreparedStatement pst = null;
				Connection conn = null;
				String associateUser = "guest";
				
				if (SessionManagement.getUserId() != null) associateUser = SessionManagement.getUserId();
				
				try {
					String sqlQuery = "INSERT INTO orders (associateUser, orderNumber,sku,name,price,qty, dateTime) "
							+ "VALUES(?,?,?,?,?,?, ?)";
					conn = DBConnection.getInstance().getConnection();
					pst = conn.prepareStatement(sqlQuery);
					
					for (Products product : products) {
						pst.setString(1, associateUser);
						pst.setString(2, UID);
						pst.setInt(3, product.getSku());
						pst.setString(4, product.getName());
						pst.setDouble(5, product.getPrice());
						pst.setInt(6, product.getNewQty());
						pst.setString(7, date);
						
						pst.execute();
					}
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}

		public String getDate() {
			System.out.println(date);
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

			
			
			
}
