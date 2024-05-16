package com.example.ProductHub.View;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MyProducts {
	@Id
	private int pid;
	private String ptype;
	private String pname;
	private int quantity;
	private String storage_id;
	
	public MyProducts() {
		super();
	}

	public MyProducts(int pid, String ptype, String pname, int quantity, String storage_id, int price, int warranty) {
		super();
		this.pid = pid;
		this.ptype = ptype;
		this.pname = pname;
		this.quantity = quantity;
		this.storage_id = storage_id;
		this.price = price;
		this.warranty = warranty;
	}

	private int price;
	private int warranty;
	
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getStorage_id() {
		return storage_id;
	}

	public void setStorage_id(String storage_id) {
		this.storage_id = storage_id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getWarranty() {
		return warranty;
	}

	public void setWarranty(int warranty) {
		this.warranty = warranty;
	}

	@Override
	public String toString() {
		return "MyPrducts [pid=" + pid + ", ptype=" + ptype + ", pname=" + pname + ", quantity=" + quantity
				+ ", storage_id=" + storage_id + ", price=" + price + ", warranty=" + warranty + "]";
	}
}
