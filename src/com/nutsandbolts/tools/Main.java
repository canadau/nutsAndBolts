package com.nutsandbolts.tools;

import java.util.UUID;

import com.nutsandbolts.Products;
import com.nutsandbolts.beans.OrdersHistory;

public class Main {

	public static void main(String[] args) {
				
		//Cart cart = new Cart();
				
		System.out.println(UUID.randomUUID().toString().substring(19));
		
		OrdersHistory ordersHistory = new OrdersHistory();
		
		for (String str : ordersHistory.ordersNumbers()) {
			System.out.println(str);
			for (Products pro : ordersHistory.productsList(str)) {
				System.out.println(pro.getName());
				System.out.println(pro.getDateTime());
				System.out.println(ordersHistory.quantity(ordersHistory.getProductsList()));
			}
		}
	}

}
