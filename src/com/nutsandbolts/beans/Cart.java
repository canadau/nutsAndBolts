package com.nutsandbolts.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import com.nutsandbolts.Products;
import com.nutsandbolts.tools.DBConnection;


// ApplicationScoped Java Class "As long as the app running on AWS
@ManagedBean(name = "cart")
@ApplicationScoped
public class Cart implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	public Cart() {}
	
	// A product template
	public Products products = new Products();
		
	// a list of products
	public List<Products> productsList = new ArrayList<>();
	
	// A list of SKUs
	public List<Integer> skus = new ArrayList<Integer>();
	
	
	//get a product from DB
	public void getProduct(int sku) {
		PreparedStatement pst =null;
		ResultSet rSet = null;
		
		try {

			String createSQL = "SELECT sku, name, description, price FROM products WHERE sku = ? ";

			Connection conn = DBConnection.getConnection();

			pst = conn.prepareStatement(createSQL);	
			pst.setInt(1, sku);
			rSet = pst.executeQuery();
			
			if (rSet.next()) {
				products = new Products(rSet.getInt(1), rSet.getString(2), rSet.getString(3), rSet.getDouble(4));
				productsList.add(products);
			}
			
		} catch (Exception e) {
			e.getMessage();
		}	
		System.out.println(products.getName());
	}
	
	// remove all products from cart
	public void removeAll() {
		System.out.println("Before Removing...." + productsList.toString());
		System.out.println("Removing....");
		productsList.clear();
		products = null;
		productsList = null;
		System.out.println("After Removing...." + productsList.toString());
	}
	
	// remove from cart "single item"
	public void removeCart(int sku) {
		
		int index;
		index = skus.indexOf(sku);

		if(index >= 0) skus.remove(index);
		
	}
	
	// Add to cart function
	public void addTo(int sku) {
		getAllProducts(skus);
		skus.add(sku);
		System.out.println(sku);
		System.out.println(productsList);
		System.out.println(skus);
		
	}
	
	// Get all products using the SKUs list 
	public List<Products> getAllProducts(List<Integer> skus) {
		productsList.clear();
		for (int sku : skus) {
			getProduct(sku);
		}
		
		return productsList;
	}

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public List<Products> getProductsList() {
		return productsList;
	}

	public void setProductsList(List<Products> productsList) {
		this.productsList = productsList;
	}

	public List<Integer> getSkus() {
		return skus;
	}

	public void setSkus(List<Integer> skus) {
		this.skus = skus;
	}
	
	
}
