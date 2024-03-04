package com.example.main.serviceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.entity.ProductEntity;
import com.example.main.model.ProductResponseModel;
import com.example.main.model.ResponseModel;
import com.example.main.repo.ProductRepository;
import com.example.main.service.ProductService;
import com.example.main.util.Constants;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository prodRepository;

	@Override
	public ProductResponseModel addProduct(ProductEntity request, Logger log) {
		ProductResponseModel response = new ProductResponseModel();
		List<ProductEntity> products = new ArrayList();
		try {
			Optional<ProductEntity> product = prodRepository.findById(request.getId());
			if (product.isPresent()) {
				response.setErrorCode(Constants.ErrorCode.PRODUCT_ALREADY_PRESENT);
				response.setErrorMessage("PRODUCT ALREADY PRESENT");
			} else {
				request.setCreatedDate(Instant.now());
				ProductEntity prod = prodRepository.save(request);
				response.setErrorCode(Constants.ErrorCode.PRODUCT_CREATED);
				response.setErrorMessage("PRODUCT CREATED SUCCESSFULLY ");
				products.add(prod);
				response.setProducts(products);
			}
		} catch (Exception e) {
			log.info("Exception occured in addProduct service :: " + e.getMessage());
			response.setErrorCode(Constants.ErrorCode.FAILURE);
			response.setErrorMessage("FAILURE");
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public ProductResponseModel getAllProducts(Logger log) {
		ProductResponseModel response = new ProductResponseModel();
		List<ProductEntity> products = new ArrayList();
		try {
			products = prodRepository.findAll();
			if(products.isEmpty()) {
				response.setErrorCode(Constants.ErrorCode.NO_ANY_PRODUCT_PRESENT);
				response.setErrorMessage("NO ANY PRODUCT PRESENT");
			}else{
				response.setErrorCode(Constants.ErrorCode.PRODUCTS_FOUND);
				response.setErrorMessage("PRODUCTS FOUND");
				response.setProducts(products);
			}
		}catch(Exception e) {
			log.info("Exception occured in getAllProducts service :: " + e.getMessage());
			response.setErrorCode(Constants.ErrorCode.FAILURE);
			response.setErrorMessage("FAILURE");
			e.printStackTrace();
		}
		return response;
	}

}