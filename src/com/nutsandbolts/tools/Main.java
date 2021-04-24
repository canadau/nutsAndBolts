package com.nutsandbolts.tools;

import java.util.List;
import java.util.Map.Entry;

import com.nutsandbolts.Products;
import com.nutsandbolts.beans.OrdersHistory;

public class Main {

	public static void main(String[] args) {
				
		//Cart cart = new Cart();
		//Date date = null;
		//System.out.println(UUID.randomUUID().toString().substring(19));
		OrdersHistory ordersHistory = new OrdersHistory();
		Products product = new Products();
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
		ordersHistory.ordersNumbers();
		//System.out.println(ordersHistory.mapProducts.entrySet());
		
		for (Entry<String, List<Products>> str : ordersHistory.mapProducts.entrySet()) {
			str.getKey();
			System.out.println(str.getKey());
			
			for (Products pro : str.getValue()) {
				System.out.println(pro.getName());
			}
			
		}
		
	}

}
