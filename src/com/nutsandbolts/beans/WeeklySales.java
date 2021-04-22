package com.nutsandbolts.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.nutsandbolts.tools.DBConnection;
import com.nutsandbolts.tools.ShowMessages;

@ManagedBean (name="weeklySales")
@RequestScoped
public class WeeklySales implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String associateUser;
	private String orderNumber;
	private int sku;
	private String name;
	private float price;
	private int qty;
	private String dateTime;
	
	public WeeklySales() {}


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
	
	public int whichWeekDay(String dateTime){
		/* This function needs to parse dateTime, do math, and probably return 
		   the integer corresponding to the day of the week.
		   I will leave my reference and my python code as notes.  
		   https://cs.uwaterloo.ca/~alopez-o/math-faq/node73.html
		*/
		// W will store the value 0-6 corresponding to 0=Sunday,...,6=Saturday
		int W;
		
		// This bit of code should read the day from the database to string d.
		char placeholder = dateTime.charAt(8);
		String d = Character.toString(placeholder);
		placeholder = dateTime.charAt(9);
		d.concat(Character.toString(placeholder));
		System.out.println(d);
		
		// This code should read the month into string m.
		placeholder = dateTime.charAt(5);
		String m = Character.toString(placeholder);
		placeholder = dateTime.charAt(6);
		m.concat(Character.toString(placeholder));
		System.out.println(m);
		
		// This should read the century to C.  (1987 would read 19)
		placeholder = dateTime.charAt(0);
		String C = Character.toString(placeholder);
		placeholder = dateTime.charAt(1);
		C.concat(Character.toString(placeholder));
		System.out.println(C);
		
		// This should read the year to Y. (1987 would read 87)
		placeholder = dateTime.charAt(2);
		String Y = Character.toString(placeholder);
		placeholder = dateTime.charAt(3);
		Y.concat(Character.toString(placeholder));
		System.out.println(Y);
		
		// We will now convert what we've read to integers.
		int dint = Integer.parseInt(d);
		int mint = Integer.parseInt(m);
		int Cint = Integer.parseInt(C);
		int Yint = Integer.parseInt(Y);
		
		/* With that bad code out of the way, we can begin to manipulate our data.
		   We will need to edit month to (1-12; 1=Mar,...,10=Dec,11=Jan,12=Feb)
		   when it is already 1=Jan,...,12=Dec.
		 */
		if (mint == 01) {
			mint = 11;
		} else if (mint == 02){
			mint = 12;
		} else {
			mint = mint - 2;
		}
		
		// We will also need to manipulate year to be the previous year if m = Jan or Feb
		if (Yint == 11 || Yint == 12 && Yint != 00) {
			Yint = Yint - 1;
		} else if (Yint == 11 || Yint == 12){
			Yint = 99;
		}
		
		/* Here is the overall calculation, we'll take it step by step since
		   we are relying on integers truncating at certain steps
		   W = (d + (2.6*m - 0.2) - 2*C + Y + Y/4 + C/4) % 7
		*/ 
		Double mdouble = Double.valueOf(mint);
		Double mfix = 2.6 * mdouble - 0.2;
		int mfixed = mfix.intValue();
		
		W = (dint + mfixed - 2*Cint + Yint + Yint/4 + Cint/4) % 7;
		
		/*
		   -------------------------------------------------------------------
		    # d for Day (1 - 31)
    		d = int(input("What is the day of the month (1-31): "))
    		# m for Month (1 = March,...,10 = December,11 = Jan, 12 = Feb)
    		m = int(input("What is the month (1-12; 1=Mar,...,10=Dec,11=Jan,12=Feb): "))
    		# C for Century (1987 has C = 19)
    		C = int(input("What is the first two digits of the year (00-99): "))
    		# Y for Year (1987 has Y = 87, except Y = 86 for Jan & Feb)
    		Y = int(input("What is the second two digits of the year (00-99): "))
    		# Treat Jan & Feb like previous year
    		if m == 11 or m == 12 and Y != 0:
        		Y = Y - 1
    		elif m == 11 or m == 12:
        		Y = 99
    		# W is week day (0 = Sunday,...,6 = Saturday)

    		# Here is the overall calculation, we'll take it step by step
    		# W = (d + (2.6*m - 0.2) - 2*C + Y + Y/4 + C/4) % 7

    		k = int(2.6*m - 0.2)
    		j = int(Y/4)
    		q = int(C/4)

    		W = (d + k - 2*C + Y + j + q) % 7

    		if W == 0:
        		Weekday = "Sunday"
    		elif W == 1:
        		Weekday = "Monday"
    		elif W == 2:
    		    Weekday = "Tuesday"
    		elif W == 3:
        		Weekday = "Wednesday"
    		elif W == 4:
        		Weekday = "Thursday"
    		elif W == 5:
        		Weekday = "Friday"
    		elif W == 6:
        		Weekday = "Saturday"
    		else:
        		Weekday = "Invalid"
		   -------------------------------------------------------------------
		*/
		
		// Return the integer (could also return String if we want)
		return W;
	}

	
	/* Commenting this out for later reference
	public void getProductsFromDB(int skuNumber) {
		Connection conn = null; 
		int count = 0;
		try {
			String createSQL = "SELECT sku, name, description, price, qty FROM products WHERE sku = ?;";
			DBConnection inst = DBConnection.getInstance();
			conn = inst.getConnection();

			PreparedStatement pst = conn.prepareStatement(createSQL); 
			pst.setInt(1, skuNumber);

			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				fSku = rs.getInt(1);
				fName = rs.getString(2);
				fDescription = rs.getString(3);
				fPrice = rs.getDouble(4);
				fQuantity = rs.getInt(5);
				count++;
			}
			if (count > 0) {
				ShowMessages.showSuccessMessage("The product was retrieved");
			} else {
				ShowMessages.showErrorMessage("Error, the product is not in the inventory"); 
			} 
			pst.close();
		} catch (Exception e) {
			e.getCause();
			System.out.println(e.getCause());
		} finally {
			DBConnection.close(conn);
		} 
	}
	public void updateProductsINDB(int sku, String name, Double price, String description , int fQuantity) {
		Connection conn = null; 

		try {
			String createSQL = "UPDATE products SET sku = ?, name = ?, description = ?, price = ?, qty = ? WHERE sku = ?;";
			DBConnection inst = DBConnection.getInstance();
			conn = inst.getConnection();

			PreparedStatement pst = conn.prepareStatement(createSQL); 
			pst.setInt(1, sku);
			pst.setString(2, name);
			pst.setString(3, description);
			pst.setDouble(4, price);
			pst.setInt(5, fQuantity);
			pst.setInt(6, sku);

			int rs = pst.executeUpdate();
			if (rs > 0) {
				ShowMessages.showSuccessMessage("The product was updated successfully");
			} else {
				ShowMessages.showErrorMessage("Error, please try again"); 
			} 
			pst.close();
		} catch (Exception e) {
			e.getCause();
			System.out.println(e.getCause());
		} finally {
			DBConnection.close(conn);
		} 

	}
	*/

}