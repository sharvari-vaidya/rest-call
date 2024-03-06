package com.example.main.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.main.entity.ProductEntity;
import com.example.main.entity.UserEntity;
import com.example.main.model.ProductResponseModel;
import com.example.main.model.ResponseModel;
import com.example.main.repo.ProductRepository;
import com.example.main.serviceImpl.ProductServiceImpl;
import com.example.main.util.Constants;

@SpringBootTest
public class ProductServiceTest {

	@Mock
	private ProductRepository prodRepository;

	@Mock
	private RedisService redisService;

	@Mock
	private Util util;
	
	@Mock
	Logger log;

	@InjectMocks
	ProductService productService = new ProductServiceImpl();
	
	String randomToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJtbW0iLCJleHAiOjE3MDk2NjQxOTN9.irJ-RUZNtF2588cQoEJCf1ZiI8V-VN_SVKBxm750Pb_iMmfmd6h9OVOd4U_U6l_g";

	@Test
	@DisplayName("AddProductSuccessTest")
	public void testWhenAddProductToDbExpectSuccess() {
		ProductEntity product= null;;
		Mockito.when(redisService.getValue(Constants.RedisKeys.REDIS_SESSION_KEY + "USER1")).thenReturn(randomToken);
		Mockito.when(util.isNeitherNullNorEmpty(ArgumentMatchers.anyString())).thenReturn(true);
		Mockito.when(prodRepository.findById(123456L)).thenReturn(Optional.ofNullable(product));
		Mockito.when(util.longToTimestamp(ArgumentMatchers.anyLong())).thenReturn(new Timestamp(System.currentTimeMillis()));
		Mockito.when(prodRepository.save(product)).thenReturn(null);
		product = getProduct();
		
		ProductResponseModel response = productService.addProduct(product, log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.PRODUCT_ADDED_SUCCESSFULLY);
		
	
	}
	
	@Test
	@DisplayName("AddProductFailureTest")
	public void testWhenAddProductToDbAndUserNotLOggedInExpectFailure() {
		ProductEntity product= getProduct();
		Mockito.when(redisService.getValue(Constants.RedisKeys.REDIS_SESSION_KEY + "USER1")).thenReturn(null);
		ProductResponseModel response = productService.addProduct(product, log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.USER_NOT_LOGGEDIN);
	}
	
	
	@Test
	@DisplayName("AddProductWhenItIsALreadyPresentFailureTest")
	public void testWhenAddProductAndItIsAlreadyPresentInDbExpectFailure() {
		ProductEntity product= getProduct();
		Mockito.when(redisService.getValue(Constants.RedisKeys.REDIS_SESSION_KEY + "USER1")).thenReturn(randomToken);
		Mockito.when(util.isNeitherNullNorEmpty(ArgumentMatchers.anyString())).thenReturn(true);
		Mockito.when(prodRepository.findById(123456L)).thenReturn(Optional.of(product));
		ProductResponseModel response = productService.addProduct(product, log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.PRODUCT_ALREADY_PRESENT);
	}
	
	
	@Test
	@DisplayName("GetAllProductSuccessTest")
	public void testWhenGetAllProductsFromDbExpectSuccess() {
		ProductEntity product= getProduct();
		List<ProductEntity> products =getAllProductsList();
		Mockito.when(util.isNeitherNullNorEmpty(products)).thenReturn(true);
		Mockito.when(util.isNeitherNullNorEmpty(ArgumentMatchers.anyString())).thenReturn(true);
		Mockito.when(redisService.getValue(Constants.RedisKeys.REDIS_SESSION_KEY + "USER1")).thenReturn(randomToken);
		Mockito.when(prodRepository.findAll()).thenReturn(products);
		ProductResponseModel response = productService.getAllProducts("USER1", log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.PRODUCTS_FOUND);
	}
	
	@Test
	@DisplayName("GetAllProductFailureTest")
	public void testWhenGetAllProductsFromDbExpectFailure() {
		ProductEntity product= getProduct();
		List<ProductEntity> products =null;
		Mockito.when(util.isNeitherNullNorEmpty(ArgumentMatchers.anyString())).thenReturn(true);
		Mockito.when(util.isNeitherNullNorEmpty(products)).thenReturn(false);

		Mockito.when(redisService.getValue(Constants.RedisKeys.REDIS_SESSION_KEY + "USER1")).thenReturn(randomToken);
		Mockito.when(prodRepository.findAll()).thenReturn(null);
		ProductResponseModel response = productService.getAllProducts("USER1", log);
		assertEquals(response.getErrorCode(), Constants.ErrorCode.NO_ANY_PRODUCT_FOUND);
	}
	
	
	private List<ProductEntity> getAllProductsList() {
		List<ProductEntity> products = new ArrayList<>();
		products.add(getProduct());
		return products;
	}

	private ProductEntity getProduct() {
		return ProductEntity.builder()
				.id(123456L)
				.sku("IP12")
				.productName("IPhone")
				.productDesc("IPhone 12 all models")
				.price(80000)
				.weight(200)
				.weightUnit("gm/unit")
				.brand("Apple")
				.category("Electronics")
				.userId("USER1")
				.expiryDate(new Timestamp(1009038365))
				.build();
	}

}
