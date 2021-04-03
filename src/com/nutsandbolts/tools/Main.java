package com.nutsandbolts.tools;

import com.nutsandbolts.Products;
import com.nutsandbolts.beans.Cart;

public class Main {

	public static void main(String[] args) {
				
		Cart cart = new Cart();
		
		cart.addTo(150);
		cart.addTo(777);
		cart.addTo(500);
		cart.addTo(150);
		
		System.out.println(cart.skus);
		
		//cart.removeCart(777);
		//cart.removeCart(150);
		//cart.removeCart(777);
		//cart.removeCart(150);
		
		System.out.println(cart.skus);
		
		cart.getAllProducts(cart.skus);
		
		for (Products pro : cart.productsList) {
			System.out.println(pro.getName());
		}
		
		
	}

}
