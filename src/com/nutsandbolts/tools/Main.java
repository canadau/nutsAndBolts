package com.nutsandbolts.tools;

import java.util.List;
import java.util.Map.Entry;

import com.nutsandbolts.Products;
import com.nutsandbolts.beans.Cart;
import com.nutsandbolts.beans.OrdersHistory;
import com.nutsandbolts.beans.WeeklySales;
import com.nutsandbolts.beans.WeeklySalesTwo;

public class Main {

	public static void main(String[] args) throws Exception {
				
		Cart cart = new Cart();
		//Date date = null;
		//System.out.println(UUID.randomUUID().toString().substring(19));
		OrdersHistory ordersHistory = new OrdersHistory();
		//Products product = new Products();
		/*
		 * OrdersHistory ordersHistory = new OrdersHistory();
		 * 
		 * for (String str : ordersHistory.ordersNumbers()) { System.out.println(str);
		 * for (Products pro : ordersHistory.productsList(str)) {
		 * System.out.println(pro.getName()); System.out.println(pro.getDateTime());
		 * System.out.println(ordersHistory.quantity(ordersHistory.getProductsList()));
		 * } 
		 * }
		 */
		
		//System.out.println(ordersHistory.productsList("9375-09b7c31a864f").get(0).getDateTime());
		//ordersHistory.ordersNumbers();
		//System.out.println(ordersHistory.mapProducts.entrySet());
		/*
		 * for (Entry<String, List<Products>> str :
		 * ordersHistory.mapProducts.entrySet()) { str.getKey();
		 * System.out.println(str.getKey());
		 * 
		 * for (Products pro : str.getValue()) { System.out.println(pro.getName()); }
		 * 
		 * }
		 */
		/*
		 * WeeklySalesTwo sale = new WeeklySalesTwo(); for (Entry<Integer,
		 * List<Products>> map : sale.getOrders().entrySet()) {
		 * 
		 * for (Products pro : map.getValue()) { System.out.println("Week "+
		 * map.getKey() + " Day: " + pro.getDay()); System.out.println("ID: " +
		 * pro.getId()); System.out.println("Name: " + pro.getName() +" Qty: " +
		 * pro.getQty()); System.out.println("\t\tDate: "+ pro.getDate());
		 * System.out.println("\tWeek Name: "+ pro.getWeek());
		 * 
		 * } System.out.println("---------------------------------"); }
		 * System.out.println("Total rows: " + sale.index);
		 */
		/*
		 * for (int in : sale.weeks()) { System.out.println(in); }
		 */
		
	}

}
