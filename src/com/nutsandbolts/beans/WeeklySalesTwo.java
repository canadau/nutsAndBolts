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
import java.util.Map.Entry;
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
	private int selectOne;
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
	
	public HashMap<Integer, List<Products>> getOrders(){		
		int week = 1;
		int weekOfYearN = 0;
		int dayOfWeekN = 0;
		monday = new ArrayList<Products>();
		sunday = new ArrayList<Products>();
		tusday = new ArrayList<Products>();
		wedensday = new ArrayList<Products>();
		thusrday = new ArrayList<Products>();
		friday = new ArrayList<Products>();
		saturday = new ArrayList<Products>();
		Calendar cal = Calendar.getInstance();
		products = new ArrayList<>();
		SimpleDateFormat formater = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
		String dateTime;
		try {

			String createSQL = "SELECT name, price, qty, dateTime, id FROM orders ORDER BY dateTime, id";
			DBConnection inst = DBConnection.getInstance();
			Connection conn = inst.getConnection();

			PreparedStatement pst = conn.prepareStatement(createSQL);	
			
			ResultSet rSet = pst.executeQuery();
			
			while (rSet.next()) {
				dateTime = formater.format(rSet.getTimestamp(4));
				date = rSet.getDate(4);
				cal.setTime(date);
				dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
				weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

				product = new Products(dayOfWeek, weekOfYear, rSet.getString(1), rSet.getDouble(2), rSet.getInt(3), dateTime, rSet.getInt(5));
				
				if (dayOfWeekN == 0) {
					products.add(product);
					
				} else if (weekOfYear == weekOfYearN  ) {
					products.add(product);
					
				} else {
					
					productsList.put(week, products);
					products = new ArrayList<>();
					products.add(product);
					week++;
					productsList.put(week, products);
				}
				
				weekOfYearN = weekOfYear;
				dayOfWeekN = dayOfWeek;
				
				index++;
		
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return productsList;
	}
	
	public List<Integer> weeks() {
		weeksList.clear();
		for (Entry<Integer, List<Products>> map : getOrders().entrySet()) {
			weeksList.add(map.getKey());

		}
		divideDays();
		if (selectOne > 0) isEmpty = true;
		return weeksList;
	}
	
	public void divideDays() {

		for (Entry<Integer, List<Products>> map : getOrders().entrySet()) {
			if (map.getKey() == selectOne) {
				for (Products product : map.getValue()) {
					if (product.getDay() == 1) {
						sunday.add(product);
					} else if (product.getDay() == 2) {
						monday.add(product);
					} else if (product.getDay() == 3) {
						tusday.add(product);
					} else if (product.getDay() == 4) {
						wedensday.add(product);
					} else if (product.getDay() == 5) {
						thusrday.add(product);
					} else if (product.getDay() == 6) {
						friday.add(product);
					} else if (product.getDay() == 7) {
						saturday.add(product);
					}
				}
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
	
	public Double totalSales() {
		double total = 0;

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

	
	

}
