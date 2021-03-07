package com.nutsandbolts.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.nutsandbolts.Products;
import com.nutsandbolts.tools.DBConnection;

@ManagedBean (name = "products")
@RequestScoped
public class ProductsDB {
	
	Products productsBeans;
	private Products product;
	private List<Products> products = new ArrayList<>();
	
	public List<Products> getProductFromDB(){		
		
		try {


			String createSQL = "SELECT sku, name, description, price FROM products ";
			Connection conn = DBConnection.getConnection();

			PreparedStatement pst = conn.prepareStatement(createSQL);	
			
			ResultSet rSet = pst.executeQuery();
			
			while (rSet.next()) {
				product = new Products(rSet.getInt(1), rSet.getString(2), rSet.getString(3), rSet.getDouble(4));
				products.add(product);
			}

		} catch (Exception e) {
			e.getMessage();
		}
		return products;
	}
	
	
}
