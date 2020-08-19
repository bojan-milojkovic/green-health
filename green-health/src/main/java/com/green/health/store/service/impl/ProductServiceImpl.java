package com.green.health.store.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import com.green.health.herb.dao.HerbRepository;
import com.green.health.herb.service.HerbService;
import com.green.health.illness.dao.IllnessRepository;
import com.green.health.illness.service.IllnessService;
import com.green.health.store.dao.ProductCriteriaRepository;
import com.green.health.store.dao.ProductRepository;
import com.green.health.store.dao.StoreRepository;
import com.green.health.store.entities.ProductJPA;
import com.green.health.store.entities.ProductDTO;
import com.green.health.store.entities.StoreJPA;
import com.green.health.store.service.ProductService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepo;
	private StoreRepository storeRepo;
	private HerbRepository herbRepo;
	private IllnessRepository illnessRepo;
	private IllnessService illnessServiceImpl;
	private HerbService herbServiceImpl;
	private ProductCriteriaRepository productCriteriaRepository;
	
	@Autowired
	public ProductServiceImpl(ProductRepository productRepo, StoreRepository storeRepo, HerbRepository herbRepo, 
			IllnessRepository illnessRepo, HerbService herbServiceImpl, IllnessService illnessServiceImpl, ProductCriteriaRepository productCriteriaRepository) {
		super();
		this.productRepo = productRepo;
		this.storeRepo = storeRepo;
		this.herbRepo = herbRepo;
		this.illnessRepo = illnessRepo;
		this.illnessServiceImpl = illnessServiceImpl;
		this.herbServiceImpl = herbServiceImpl;
		this.productCriteriaRepository = productCriteriaRepository;
	}
	
	public void deleteProduct(final Long id, final Long sid, final String username) throws MyRestPreconditionsException {
		checkId(id,"Delete product error (special offer id)");
		checkId(sid,"Delete product error (store id)");
		
		ProductJPA jpa = getRepository().getOne(id);
		StoreJPA sjpa = jpa.getStoreJpa();
		
		RestPreconditions.assertTrue(sjpa.getId()==sid && sjpa.getUsername().equals(username),
				"Delete product error", "That product does not belong to your store.");
		
		ProductService.super.delete(id);
	}
	
	public List<ProductDTO> getProductsForStore(final Long storeId) throws MyRestPreconditionsException {
		checkId(storeId, "Error getting the products for the store");
		List<ProductDTO> result = new ArrayList<ProductDTO>();
		for(ProductJPA jpa : RestPreconditions.checkNotNull(storeRepo.getOne(storeId), 
				"Error getting the products of the store", "No store exists for that id").getSpecialOffers()){
			result.add(completeConversion(jpa));
		}
		return result;
	}

	public void addNew(final ProductDTO model) throws MyRestPreconditionsException {
		
		ProductService.super.addNew(model);
		
		// check if the store exists :
		checkId(model.getStoreId(), "Adding new product error - store id");
		StoreJPA sjpa = RestPreconditions.checkNotNull(storeRepo.findStoreById(model.getStoreId()), "Adding new product error", "There is no store for that id.");
		
		// check if user is adding special offer to someone else's store
		RestPreconditions.assertTrue(sjpa.getUsername().equals(model.getUsername()),
				"Adding new product error", "You are trying to add a product to someone else's store.");
		
		ProductJPA jpa = convertModelToJPA(model);
		
		jpa.setStoreJpa(sjpa);
		jpa.setCity(sjpa.getCity());
		
		// check that this special offer does not exist in this store already :
		RestPreconditions.assertTrue(!sjpa.getSpecialOffers().contains(jpa),
				"Adding new product error", "This store already has a product containing those herbs, intended for those illnesses");
		
		productRepo.save(jpa);
	}
	
	public ProductDTO edit(ProductDTO model, final Long id) throws MyRestPreconditionsException {
		model = ProductService.super.edit(model, id);
		
		StoreJPA sjpa = RestPreconditions.checkNotNull(storeRepo.getOne(model.getStoreId()), 
				"Editing product error", "There is no store with id = "+model.getStoreId());
		
		//TODO: Add username to store JPA
		RestPreconditions.assertTrue(sjpa.getUsername().equals(model.getUsername()),
				"Editing special offer error", "You are trying to edit a product of someone else's store.");
		
		ProductJPA jpa = convertModelToJPA(model);
		
		// check that this special offer does not exist in this store already :
		if(sjpa.getSpecialOffers().contains(jpa)){
			RestPreconditions.assertTrue(
				sjpa.getSpecialOffers().stream().filter(tmp -> tmp.equals(jpa) && tmp.getId()!=jpa.getId()).findFirst().isPresent(),
					"Editing product error", "This store already has a product containing those herbs, intended for those illnesses");
		}
		
		return addListsToModel(convertJpaToModel(getRepository().save(jpa)), jpa);
	}
	
	@Override
	public ProductJPA convertModelToJPA(final ProductDTO model) throws MyRestPreconditionsException {
		ProductJPA jpa = null;
		
		if(model.getId() == null){
			jpa = new ProductJPA();
		} else {
			jpa = RestPreconditions.checkNotNull(productRepo.getOne(model.getId()),
					"Edit product error", "Product with id = "+model.getId()+" does not exist in your store");
		}
		
		if(!(model.getContains()==null || model.getContains().isEmpty())){
			String contains = "";
			for(Long id : model.getContains()){
				RestPreconditions.checkNotNull(herbRepo.getOne(id), "Add new product error", "Herb contained in that product (id = "+id+") does not exist in our database");
				contains = contains + id +",";
			}
			jpa.setContains(contains.replaceFirst(".$",""));
		}
		if(!(model.getTreats()==null || model.getTreats().isEmpty())){
			String treats="";
			for(Long id : model.getTreats()){
				RestPreconditions.checkNotNull(illnessRepo.getOne(id), "Add new product error", "Illness that product treats (id = "+id+") does not exist in our database");
				treats = treats + id + ",";
			}
			jpa.setTreats(treats.replaceFirst(".$",""));
		}		
		if(RestPreconditions.checkString(model.getLocalDescription())){
			jpa.setLocalDescription(model.getLocalDescription());
		}
		if(RestPreconditions.checkString(model.getLocalName())){
			jpa.setLocalName(model.getLocalName());
		}
		if(model.getPrice()!=null){
			jpa.setPrice(model.getPrice());
		}
		if(RestPreconditions.checkString(model.getCurrency())){
			jpa.setCurrency(model.getCurrency());
		}
		if(model.getExpires()!=null){
			jpa.setExpires(model.getExpires());
		}
		
		return jpa;
	}
	
	private ProductDTO addListsToModel(ProductDTO model, ProductJPA jpa) throws MyRestPreconditionsException{
		model.setHerbs(herbServiceImpl.getListOfMiniHerbs(jpa.getContains()));
		model.setIllnesses(illnessServiceImpl.getListOfMinIllnesses(jpa.getTreats()));
		return model;
	}
	
	public ProductDTO completeConversion(ProductJPA jpa) throws MyRestPreconditionsException{
		return addListsToModel(convertJpaToModel(jpa), jpa);
	}

	@Override
	public ProductDTO convertJpaToModel(final ProductJPA jpa) {
		ProductDTO model = new ProductDTO();
		
		model.setCurrency(jpa.getCurrency());
		model.setLocalDescription(jpa.getLocalDescription());
		model.setId(jpa.getId());
		model.setStoreId(jpa.getStoreJpa().getId());
		model.setExpires(jpa.getExpires());
		model.setLocalName(jpa.getLocalName());
		model.setPrice(jpa.getPrice());
		
		return model;
	}

	@Override
	public boolean isPatchDataPresent(final ProductDTO model) {
		return model.getStoreId() != null && // mandatory for patch
				(  RestPreconditions.checkString(model.getLocalDescription())
				|| RestPreconditions.checkString(model.getLocalName())
				|| model.getPrice() != null
				|| RestPreconditions.checkString(model.getCurrency())
				|| !model.getContains().isEmpty()
				|| !model.getTreats().isEmpty()
				);
	}

	@Override
	public JpaRepository<ProductJPA, Long> getRepository() {
		return productRepo;
	}

	@Override
	public String getName() {
		return "Product";
	}

	@Override
	public void getPostValidationErrors(ProductDTO model, List<String> list) {
		if(!RestPreconditions.checkString(model.getCurrency())){
			list.add("The currency for the price is mandatory");
		}
		if(!RestPreconditions.checkString(model.getLocalDescription())){
			list.add("Description of the product is mandatory");
		}
		if(!RestPreconditions.checkString(model.getLocalName())){
			list.add("Local name of the productIsMandatory");
		}
		if(model.getPrice()==null){
			list.add("Price of the product is mandatory");
		}
		if(!RestPreconditions.checkString(model.getCurrency())){
			list.add("Currency for the product price is mandatory");
		}
		if(model.getStoreId() == null){
			list.add("Id of your store is mandatory");
		}
		if(model.getContains().isEmpty()){
			list.add("You must list herbs contained in this product");
		}
		if(model.getTreats().isEmpty()){
			list.add("You must list ailments yourt product is meant for");
		}
	}

	@Override
	public List<ProductDTO> getProductsByProperties(ProductDTO model) throws MyRestPreconditionsException {
		List<ProductDTO> result = new ArrayList<ProductDTO>();
		
		productCriteriaRepository.getProductByProperties(model);
		for(ProductJPA jpa : productCriteriaRepository.getProductByProperties(model)){
			result.add(completeConversion(jpa));
		}
		return result;
	}
}
