package com.example.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.entity.ProductEntity;
import com.example.main.model.ProductResponseModel;
import com.example.main.model.ResponseModel;
import com.example.main.service.ProductService;

import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.JedisCluster;

@RestController
@Log4j2
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping(value="/addProduct",method=RequestMethod.POST)
	public ResponseEntity<ProductResponseModel> addProduct(@RequestBody ProductEntity request){
		log.info("Entering in product controller :: addPRoduct method with request :: "+request.toString());
		ProductResponseModel response = productService.addProduct(request,log);
		log.info("Exiting with response :: "+response.toString());
		return new ResponseEntity<ProductResponseModel>(response, HttpStatus.OK); 

	}
	@RequestMapping(value="/getAllProducts/{userId}",method=RequestMethod.GET)
	public ResponseEntity<ProductResponseModel> addProduct(@PathVariable("userId") String user){
		log.info("Entering in product controller :: getAllProducts method ");
		ProductResponseModel products = productService.getAllProducts(user,log);
		log.info("Exiting with response :: "+products.toString());
		return new ResponseEntity<ProductResponseModel>(products, HttpStatus.OK); 

	}
	

}
