package edu.hanu.a2_2001040024.models;
import androidx.annotation.NonNull;

import java.io.Serializable;
public class Product implements Serializable {
	private final String thumbnail;
	private final String category;
	private final int unitPrice;
	private int id;
	private String name;
	private int numberInCart;
	public Product(int id, String thumbnail, String name, String category, int unitPrice) {
		this.id = id;
		this.thumbnail = thumbnail;
		this.name = name;
		this.category = category;
		this.unitPrice = unitPrice;
		this.numberInCart = 0;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUnitPrice() {
		return unitPrice;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public int getNumberInCart() {
		return numberInCart;
	}
	public void setNumberInCart(int numberInCart) {
		this.numberInCart = numberInCart;
	}
	@NonNull
	@Override
	public String toString() {
		return "Product{" + "id=" + id + ", category='" + category + '\'' + ", thumbnail='" + thumbnail + '\'' + ", name='" + name + '\'' + ", unitPrice=" + unitPrice + '}';
	}
}
