package com.green.health.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import com.green.health.store.dao.SpecialOfferRepository;
import com.green.health.store.dao.StoreRepository;
import com.green.health.store.entities.SpecialOfferDTO;
import com.green.health.store.entities.SpecialOfferJPA;
import com.green.health.store.entities.StoreJPA;
import com.green.health.store.service.SpecialOfferService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class SpecialOfferServiceImpl implements SpecialOfferService {

	private SpecialOfferRepository specialOfferRepo;
	private StoreRepository storeRepo;
	
	@Autowired
	public SpecialOfferServiceImpl(SpecialOfferRepository specialOfferRepo, StoreRepository storeRepo) {
		super();
		this.specialOfferRepo = specialOfferRepo;
		this.storeRepo = storeRepo;
	}

	public void addNew(final SpecialOfferDTO model) throws MyRestPreconditionsException {
		// check if the store exists :
		checkId(model.getStoreId(), "Adding new special offer error");
		StoreJPA sjpa = RestPreconditions.checkNotNull(storeRepo.getOne(model.getStoreId()), "Adding new special offer error", "There is no store for that id.");
		
		// check that this special offer does not exist in this store yet :
		RestPreconditions.assertTrue(!sjpa.getSpecialOffers().contains(new SpecialOfferJPA(model.getName(), model.getStoreId())),
				"Adding new special offer error", "This store already has a special offer for the product named "+model.getName());
		
		SpecialOfferJPA jpa = convertModelToJPA(model);
		
		jpa.setStoreJpa(sjpa);
		sjpa.getSpecialOffers().add(jpa);
		
		specialOfferRepo.save(jpa);
	}
	
	public SpecialOfferDTO edit(SpecialOfferDTO model, final Long id) throws MyRestPreconditionsException {
		model = SpecialOfferService.super.edit(model, id);
		
		RestPreconditions.assertTrue(getRepository().getOne(id).getStoreJpa().getId() == model.getStoreId(),
				"Edit special offer error", "You are trying to edit a special offer for the wrong store id");
		
		return convertJpaToModel(getRepository().save(convertModelToJPA(model)));
	}
	
	@Override
	public SpecialOfferJPA convertModelToJPA(final SpecialOfferDTO model) throws MyRestPreconditionsException {
		SpecialOfferJPA jpa = null;
		
		if(model.getId() == null){
			jpa = new SpecialOfferJPA();
		} else {
			jpa = specialOfferRepo.getOne(model.getId());
		}
		
		if(model.getExpirationDate()!=null){
			jpa.setExpirationDate(model.getExpirationDate());
		}
		if(RestPreconditions.checkString(model.getCurrency())){
			jpa.setCurrency(model.getCurrency());
		}
		if(RestPreconditions.checkString(model.getDescription())){
			jpa.setDescription(model.getDescription());
		}
		if(RestPreconditions.checkString(model.getName())){
			jpa.setName(model.getName());
		}
		if(model.getPrice()!=null){
			jpa.setPrice(model.getPrice());
		}
		
		return jpa;
	}

	@Override
	public SpecialOfferDTO convertJpaToModel(final SpecialOfferJPA jpa) {
		SpecialOfferDTO model = new SpecialOfferDTO();
		model.setCurrency(jpa.getCurrency());
		model.setDescription(jpa.getDescription());
		model.setId(jpa.getId());
		model.setStoreId(jpa.getStoreJpa().getId());
		model.setExpirationDate(jpa.getExpirationDate());
		model.setName(jpa.getName());
		model.setPrice(jpa.getPrice());
		return model;
	}

	@Override
	public boolean isPatchDataPresent(final SpecialOfferDTO model) {
		return model.getStoreId() != null && // mandatory for patch
				(RestPreconditions.checkString(model.getCurrency())
				|| RestPreconditions.checkString(model.getDescription())
				|| model.getExpirationDate() != null
				|| RestPreconditions.checkString(model.getName())
				|| model.getPrice() != null);
	}

	@Override
	public JpaRepository<SpecialOfferJPA, Long> getRepository() {
		return specialOfferRepo;
	}

	@Override
	public String getName() {
		return "Special Offer";
	}

	@Override
	public void getPostValidationErrors(SpecialOfferDTO model, List<String> list) {
		if(!RestPreconditions.checkString(model.getCurrency())){
			list.add("The currency for the price is mandatory");
		}
		if(!RestPreconditions.checkString(model.getDescription())){
			list.add("Description of the product is mandatory");
		}
		if(!RestPreconditions.checkString(model.getName())){
			list.add("Name of the productIsMandatory");
		}
		if(model.getPrice() == null){
			list.add("Price of the product is mandatory");
		}
		if(model.getStoreId() == null){
			list.add("Id of your store is missing");
		}
	}
}
