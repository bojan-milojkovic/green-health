package com.green.health.store.service;

import java.util.List;

import com.green.health.parents.ServiceParent;
import com.green.health.store.entities.ProductDTO;
import com.green.health.store.entities.ProductJPA;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface ProductService extends ServiceParent<ProductJPA, ProductDTO> {

	public void deleteProduct(final Long id, final Long sid, final String username) throws MyRestPreconditionsException;
	
	public List<ProductDTO> getProductsForStore(final Long storeId) throws MyRestPreconditionsException;
	
	public List<ProductDTO> getProductsByProperties(final ProductDTO model) throws MyRestPreconditionsException;
	
	public ProductDTO completeConversion(ProductJPA jpa) throws MyRestPreconditionsException;
}