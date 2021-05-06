package com.nutsandbolts.beans;

import java.io.Serializable;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.nutsandbolts.Products;
import com.nutsandbolts.tools.DBConnection;

@ManagedBean(name="sale")
@SessionScoped
public class WeeklySalesTwo implements Serializable {
	public WeeklySalesTwo() {}
	
	private static final long serialVersionUID = 1L;
	List<Products> products;
	HashMap<Integer, List<Products>> productsList = new HashMap<Integer, List<Products>>();
	Products product;
	Date date;
	int dayOfWeek;
	int weekOfYear;
	public int index = 0;
	private int selectOne= 1;
	public List<String> range;
	public List<Integer> weeksList = new ArrayList<Integer>();
	public List<Products> sunday;
	public List<Products> monday;
	public List<Products> tusday;
	public List<Products> wedensday;
	public List<Products> thusrday;
	public List<Products> friday;
	public List<Products> saturday;
	public boolean isEmpty = false;
	public List<Date> rangeDate = new ArrayList<Date>();
	
	public List<Products> getOrders() {

		monday = new ArrayList<Products>();
		sunday = new ArrayList<Products>();
		tusday = new ArrayList<Products>();
		wedensday = new ArrayList<Products>();
		thusrday = new ArrayList<Products>();
		friday = new ArrayList<Products>();
		saturday = new ArrayList<Products>();
		Calendar cal = Calendar.getInstance();
		products = new ArrayList<>();
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(rangeDate.get(1));
		c.add(Calendar.DATE, 1);
		Date dt = c.getTime();

		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

		String dateTime;
		try {

			String createSQL = "SELECT name, price, qty, dateTime, id FROM orders WHERE dateTime BETWEEN ? AND ? ORDER BY dateTime, id;";

			DBConnection inst = DBConnection.getInstance();
			Connection conn = inst.getConnection();
			PreparedStatement pst = conn.prepareStatement(createSQL);	
			
			pst.setString(1, formater.format(rangeDate.get(0)));
			pst.setString(2, formater.format(dt));
			
			ResultSet rSet = pst.executeQuery();
			
			while (rSet.next()) {
				dateTime = formater.format(rSet.getTimestamp(4));
				date = rSet.getDate(4);
				cal.setTime(date);
				dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
				weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

				product = new Products(dayOfWeek, weekOfYear, rSet.getString(1), rSet.getDouble(2), rSet.getInt(3), dateTime, rSet.getInt(5));
				
				products.add(product);
					
				index++;
		
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		divideDays();
		return products;
	}
	

	public void divideDays() {

		for (Products prod : products) {

				if (prod.getDay() == 1) {
					sunday.add(prod);
					isEmpty = true;
				} else if (prod.getDay() == 2) {
					monday.add(prod);
				} else if (prod.getDay() == 3) {
					tusday.add(prod);
				} else if (prod.getDay() == 4) {
					wedensday.add(prod);
				} else if (prod.getDay() == 5) {
					thusrday.add(prod);
				} else if (prod.getDay() == 6) {
					friday.add(prod);
				} else if (prod.getDay() == 7) {
					saturday.add(prod);
				}
				
		}

	}
	
	public String total(List<Products> days) {
		double totalValue = 0;
		DecimalFormat df = new DecimalFormat("0.00");
		df.setRoundingMode(RoundingMode.DOWN);
		
		for (Products pro : days) {
			totalValue = (pro.getPrice() * pro.getQty()) + totalValue;
		}
		
		return df.format(totalValue);
	}
	
	public int totalItems() {
		int quantities = 0;
		
		quantities = itemQuantity(sunday) + quantities;
		quantities = itemQuantity(monday) + quantities;
		quantities = itemQuantity(tusday) + quantities;
		quantities = itemQuantity(wedensday) + quantities;
		quantities = itemQuantity(thusrday) + quantities;
		quantities = itemQuantity(friday) + quantities;
		quantities = itemQuantity(saturday) + quantities;
		
		return quantities;
	}
	
	public double totalSales() {
		double total = 0;
		DecimalFormat df = new DecimalFormat("0.00");
		df.setRoundingMode(RoundingMode.DOWN);
		
		total = Double.parseDouble(total(sunday)) + total;
		total = Double.parseDouble(total(monday)) + total;
		total = Double.parseDouble(total(tusday)) + total;
		total = Double.parseDouble(total(wedensday)) + total;
		total = Double.parseDouble(total(thusrday)) + total;
		total = Double.parseDouble(total(friday)) + total;
		total = Double.parseDouble(total(saturday)) + total;

		return total;
	}
	
	public int itemQuantity(List<Products> products) {
		int quantity = 0;
		for (Products prod : products) {
			quantity = prod.getQty() + quantity;
		}
		return quantity;
	}
	
	public int getSelectOne() {
		return selectOne;
	}

	public void setSelectOne(int selectOne) {
		this.selectOne = selectOne;
	}

	public List<Products> getSunday() {
		return sunday;
	}

	public void setSunday(List<Products> sunday) {
		this.sunday = sunday;
	}

	public List<Products> getMonday() {
		return monday;
	}

	public void setMonday(List<Products> monday) {
		this.monday = monday;
	}

	public List<Products> getTusday() {
		return tusday;
	}

	public void setTusday(List<Products> tusday) {
		this.tusday = tusday;
	}

	public List<Products> getWedensday() {
		return wedensday;
	}

	public void setWedensday(List<Products> wedensday) {
		this.wedensday = wedensday;
	}

	public List<Products> getThusrday() {
		return thusrday;
	}

	public void setThusrday(List<Products> thusrday) {
		this.thusrday = thusrday;
	}

	public List<Products> getFriday() {
		return friday;
	}

	public void setFriday(List<Products> friday) {
		this.friday = friday;
	}

	public List<Products> getSaturday() {
		return saturday;
	}

	public void setSaturday(List<Products> saturday) {
		this.saturday = saturday;
	}

	public List<String> getRange() {
		return range;
	}

	public void setRange(List<String> range) {
		this.range = range;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public List<Date> getRangeDate() {
		return rangeDate;
	}

	public void setRangeDate(List<Date> rangeDate) {
		this.rangeDate = rangeDate;
	}

	
	

}
