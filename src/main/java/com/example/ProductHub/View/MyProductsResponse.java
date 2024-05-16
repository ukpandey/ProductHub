package com.example.ProductHub.View;

public class MyProductsResponse {
    private MyProducts products;
    private String message;
    

	public MyProducts getProducts() {
		return products;
	}


	public void setProducts(MyProducts products) {
		this.products = products;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public MyProductsResponse(MyProducts products, String message) {
        this.products = products;
        this.message = message;
    }
	public MyProductsResponse(MyProducts products) {
        this.products = products;
    }
	public MyProductsResponse(String message) {
        this.message = message;
    }
}