package com.example.main.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="product_table")
public class ProductEntity implements Serializable{
	
	private static final long serialVersionUID = -3958032382814225920L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="product_id")
	private long id;
	
	@Column(name="product_sku")
	private String sku;
	
	@Column(name="product_name")
	private String productName;
	
	@Column(name="product_desc")
	private String productDesc;
	
	@Column(name="price")
	private double price;
	
	@Column(name="weight")
	private double weight;
	
	@Column(name="weight_unit")
	private String weightUnit;
	
	@Column(name="brand")
	private String brand;
	
	@Column(name="category")
	private String category;
	
	@Column(name="expiry_date")
	private Instant expiryDate;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="inventory")
	private String inventory;
	
	@Column(name="created_date")
	private Instant createdDate;

}
