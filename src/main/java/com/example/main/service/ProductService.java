package com.example.main.service;


import org.apache.logging.log4j.Logger;

import com.example.main.entity.ProductEntity;
import com.example.main.model.ProductResponseModel;
import com.example.main.model.ResponseModel;

public interface ProductService {

	ProductResponseModel addProduct(ProductEntity request, Logger log);

	ProductResponseModel getAllProducts(String user,Logger log);

}
