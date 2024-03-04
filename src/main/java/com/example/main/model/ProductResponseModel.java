package com.example.main.model;

import java.util.List;

import com.example.main.entity.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductResponseModel {
	
	private String errorMessage;
	private int errorCode;
	private List<ProductEntity> products;
	

}
