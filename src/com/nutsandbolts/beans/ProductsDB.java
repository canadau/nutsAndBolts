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

@ManagedBean (name = "products")
@RequestScoped
public class ProductsDB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Products product;
	private List<Products> products = new ArrayList<>();
	
	public ProductsDB() {}
	public List<Products> getProductFromDB(){		
		
		try {


			String createSQL = "SELECT sku, name, description, price, qty FROM products ";
			DBConnection inst = DBConnection.getInstance();
			Connection conn = inst.getConnection();

			PreparedStatement pst = conn.prepareStatement(createSQL);	
			
			ResultSet rSet = pst.executeQuery();
			
			while (rSet.next()) {
				product = new Products(rSet.getInt(1), rSet.getString(2), rSet.getString(3), rSet.getDouble(4), rSet.getInt(5));
				products.add(product);
			}

		} catch (Exception e) {
			e.getMessage();
		}
		return products;
	}
	
	
}
