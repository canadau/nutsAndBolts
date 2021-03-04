package com.nutsandbolts.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.nutsandbolts.tools.DBConnection;
import com.nutsandbolts.tools.ShowMessages;

@ManagedBean (name="updateProducts")
@RequestScoped
public class UpdateProductsBeans implements Serializable {
private static final long serialVersionUID = 1L;
private int fSku;
private String fName;
private String fDescription;
private double fPrice;
public UpdateProductsBeans() {}


public int getfSku() {
return fSku;
}


public void setfSku(int fSku) {
this.fSku = fSku;
}


public String getfName() {
return fName;
}


public void setfName(String fName) {
this.fName = fName;
}


public String getfDescription() {
return fDescription;
}


public void setfDescription(String fDescription) {
this.fDescription = fDescription;
}


public double getfPrice() {
return fPrice;
}


public void setfPrice(double fPrice) {
this.fPrice = fPrice;
}
public void getProductsFromDB(int skuNumber) {
Connection conn = null; 
int count = 0;
try {
String createSQL = "SELECT sku, name, description, price FROM products WHERE sku = ?;";
conn = DBConnection.getConnection();

PreparedStatement pst = conn.prepareStatement(createSQL); 
pst.setInt(1, skuNumber);

ResultSet rs = pst.executeQuery();
if (rs.next()) {
fSku = rs.getInt(1);
fName = rs.getString(2);
fDescription = rs.getString(3);
fPrice = rs.getDouble(4);
count++;
}
if (count > 0) {
ShowMessages.showSuccessMessage("The Product Was Retrieved");
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
 public void updateProductsINDB(int sku, String name, Double price, String description ) {
 Connection conn = null; 

try {
String createSQL = "UPDATE products SET sku = ?, name = ?, description = ?, price = ? WHERE sku = ?;";
conn = DBConnection.getConnection();

PreparedStatement pst = conn.prepareStatement(createSQL); 
pst.setInt(1, sku);
pst.setString(2, name);
pst.setString(3, description);
pst.setDouble(4, price);
pst.setInt(5, sku);

int rs = pst.executeUpdate();
if (rs > 0) {
ShowMessages.showSuccessMessage("The Product Was Updated Successfully");
} else {
ShowMessages.showErrorMessage("Error, Please Try Again"); 
} 
pst.close();
} catch (Exception e) {
e.getCause();
System.out.println(e.getCause());
} finally {
DBConnection.close(conn);
} 
 
 }
 

}
