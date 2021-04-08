package com.nutsandbolts.beans;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import com.nutsandbolts.Products;
import com.nutsandbolts.tools.ShowMessages;

// ApplicationScoped Java Class "It lasts as long as the app is running on AWS server"
@ManagedBean(name = "cart")
@ApplicationScoped
public class Cart implements Serializable {

	private static final long serialVersionUID = 1L;

	public Cart() {}

	// A product template
	public Products products2 = null;

	// A list of products no duplicated items only use qty to know the quantity
	public List<Products> productsListNoDub = new ArrayList<Products>();

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
	public int quantity() {
		int qty = 0;
		
		for(Products pro : productsListNoDub) {
			qty = pro.newQty + qty;
		}		
		return qty;
	}
	
	// To calculate the subtotal 
	public double subTotal() {
		double subtotal = 0;
		DecimalFormat df = new DecimalFormat("#.00");
		
		for(Products pro : productsListNoDub) {
			subtotal = pro.getPrice() * pro.getNewQty() + subtotal;
		}		
		return Double.valueOf(df.format(subtotal));
	}
	
	// I am using Ohio tax rate 7.5
	public double tax() {
		DecimalFormat df = new DecimalFormat("#.00");
		return Double.valueOf(df.format(subTotal() * .075));
	}
	
	//Calculate the total
	public double totalCart() {
		DecimalFormat df = new DecimalFormat("#.00");
		
		return Double.valueOf(df.format(subTotal() + tax()));
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
	
	// Getter and Setter for the productsListNoDub list
	public List<Products> getProductsListNoDub() {
		return productsListNoDub;
	}

	public void setProductsListNoDub(List<Products> productsListNoDub) {
		this.productsListNoDub = productsListNoDub;
	}
	
	

}
