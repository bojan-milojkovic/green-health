package com.green.health.store.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.green.health.store.entities.ProductDTO;
import com.green.health.store.service.ProductService;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Controller
@RequestMapping(value="/store/pr")
public class ProductController {

	@Autowired
	private ProductService productServiceImpl;
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers="Content-Type!=multipart/form-data")
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.CREATED)
	public void addNewProduct(@RequestBody @Valid ProductDTO model, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" adding new product a store with id = "+model.getStoreId());
		model.setUsername(principal.getName());
		productServiceImpl.addNew(model);
	}
	
	@RequestMapping(value = "/{sid}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<ProductDTO> getProductsForStore(@PathVariable("sid") final Long storeId, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" adding new product a store with id = "+storeId);
		return productServiceImpl.getProductsForStore(storeId);
	}
	
	@RequestMapping(value = "/bypar", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<ProductDTO> findProductsByParameters(@RequestBody @Valid ProductDTO model, 
			@RequestParam(value="city", required=true) String city,
			Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" getting products by properties...");
		model.setCity(city);
		return productServiceImpl.getProductsByProperties(model);
	}
	
	@RequestMapping(value = "", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.OK)
	public void deleteProduct(@RequestParam(value="sid", required=true) final Long id, 
			@RequestParam(value="sid", required=true) final Long sid, 
			Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" deleting product id = "+id);
		productServiceImpl.deleteProduct(id, sid, principal.getName());
	}
}
