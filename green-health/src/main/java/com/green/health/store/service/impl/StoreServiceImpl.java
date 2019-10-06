package com.green.health.store.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import com.green.health.security.repositories.UserSecurityRepository;
import com.green.health.store.dao.StoreCriteriaRepository;
import com.green.health.store.dao.StoreRepository;
import com.green.health.store.entities.SpecialOfferDTO;
import com.green.health.store.entities.StoreDTO;
import com.green.health.store.entities.StoreJPA;
import com.green.health.store.service.SpecialOfferService;
import com.green.health.store.service.StoreService;
import com.green.health.user.entities.UserJPA;
import com.green.health.user.service.UserService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class StoreServiceImpl implements StoreService{
	private StoreRepository storeRepo;
	private SpecialOfferService specialOfferServiceImpl;
	private UserService userServiceImpl;
	private UserSecurityRepository userSecurityRepo;
	private StoreCriteriaRepository storeCriteriaRepo;
	
	private final int MAX_NUM_STORES = 3;
	
	@Autowired
	public StoreServiceImpl(StoreRepository storeRepo, UserSecurityRepository userRepo, SpecialOfferService specialOfferServiceImpl, UserService userServiceImpl, StoreCriteriaRepository storeCriteriaRepo) {
		super();
		this.storeRepo = storeRepo;
		this.specialOfferServiceImpl = specialOfferServiceImpl;
		this.userSecurityRepo = userRepo;
		this.userServiceImpl = userServiceImpl;
		this.storeCriteriaRepo = storeCriteriaRepo; 
	}
	
	public List<StoreDTO> getMyStore(final String username) throws MyRestPreconditionsException {
		Set<StoreJPA> jpas = userServiceImpl.getStoreByUser(username);
		RestPreconditions.assertTrue(!jpas.isEmpty(), "Error getting user's stores", "No stores exist for user "+username);
		return jpas.stream().map(j -> convertJpaToModel(j)).collect(Collectors.toList());
	}
	
	public List<StoreDTO> getStoresByProperties(final StoreDTO model) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(isPatchDataPresentBasic(model),"Error retreaving stores","You must provide some search criteria.");
		return storeCriteriaRepo.getStoresByProperties(model).stream().map(j -> convertJpaToModel(j)).collect(Collectors.toList());
	}
	
	public List<StoreDTO> getAllStores() {
		return storeRepo.findAll().stream().map(j -> convertJpaToModel(j)).collect(Collectors.toList());
	}

	@Override
	public void addNew(final StoreDTO model) throws MyRestPreconditionsException{
		StoreService.super.addNew(model);
		
		// TODO : also add contact center phone
		// user must exist because it passed security
		UserJPA ujpa = userSecurityRepo.findByUsername(model.getUsername()).getUserJpa();
		// check how many stores the user has :
		RestPreconditions.assertTrue(ujpa.getStoreJpa().size()<MAX_NUM_STORES, 
				"Adding new store error", "The user "+model.getUsername()+" already has the maximum number of stores allowed.");
		
		StoreJPA jpa = convertModelToJPA(model);
		
		ujpa.getStoreJpa().add(jpa);
		jpa.setUserJpa(ujpa);
		
		storeRepo.save(jpa);
	}
	
	public boolean checkUserOwnsTheStore(final String username, final Long id){
		// check user is editing his store and not someone elses's :
		return userSecurityRepo.findByUsername(username).getUserJpa().getStoreJpa().contains(new StoreJPA(id));
	}
	
	public StoreDTO edit(StoreDTO model, final Long id) throws MyRestPreconditionsException{
		RestPreconditions.assertTrue( 
			checkUserOwnsTheStore(model.getUsername(), id),
			"Edit store error", "You cannot edit someone else's store");
		
		return convertJpaToModel(storeRepo.save(convertModelToJPA(StoreService.super.edit(model, id))));
	}
	
	@Override
	public StoreJPA convertModelToJPA(final StoreDTO model) throws MyRestPreconditionsException {
		StoreJPA jpa = null;
		
		if(model.getId()==null){
			jpa = new StoreJPA();
			jpa.setAdded(LocalDate.now());
		} else {
			jpa = RestPreconditions.checkNotNull(storeRepo.getOne(model.getId()), "Store edit error", "Store with that id does not exist");
		}
		
		if(RestPreconditions.checkString(model.getDescription())){
			jpa.setDescription(model.getDescription());
		}
		if(RestPreconditions.checkString(model.getGoogleMapsLocation())){
			jpa.setGoogleMapsLocation(model.getGoogleMapsLocation());
		}
		if(RestPreconditions.checkString(model.getName())){
			if(!model.getName().equals(jpa.getName())){
				RestPreconditions.checkNull(storeRepo.findByName(model.getName()), 
						"Add/Edit store for user "+model.getUsername(), "The store with that name belongs to another user");
			}
			jpa.setName(model.getName());
		}
		if(RestPreconditions.checkString(model.getStoreWebSite())){
			jpa.setStoreWebSite(model.getStoreWebSite());
		}
		if(!model.getSpecialOffers().isEmpty()){
			for(SpecialOfferDTO so : model.getSpecialOffers()){
				specialOfferServiceImpl.isPostDataPresent(so);
				jpa.getSpecialOffers().add(specialOfferServiceImpl.convertModelToJPA(so));
			}
		}
		if(RestPreconditions.checkString(model.getAddress1())){
			jpa.setAddress1(model.getAddress1());
		}
		if(RestPreconditions.checkString(model.getAddress2())){
			jpa.setAddress2(model.getAddress2());
		}
		if(RestPreconditions.checkString(model.getCity())){
			jpa.setCity(model.getCity());
		}
		if(RestPreconditions.checkString(model.getCountry())){
			jpa.setCountry(model.getCountry());
		}
		if(RestPreconditions.checkString(model.getEmail())){
			if(!model.getEmail().equals(jpa.getEmail())){
				RestPreconditions.checkNull(storeRepo.findByEmail(model.getEmail()), 
						"Add/Edit store for user "+model.getUsername(), "The store with that email belongs to another user");
			}
			jpa.setEmail(model.getEmail());
		}
		if(RestPreconditions.checkString(model.getPhone1())){
			if(!(model.getPhone1().equals(jpa.getPhone1()) || model.getPhone1().equals(jpa.getPhone2()))){
				RestPreconditions.checkNull(storeRepo.findByPhoneNumber(model.getPhone1()), 
						"Add/Edit store for user "+model.getUsername(), "The store with that phone number belongs to another user");
			}
			jpa.setPhone1(model.getPhone1());
		}
		if(RestPreconditions.checkString(model.getPhone2())){
			if(!(model.getPhone2().equals(jpa.getPhone1()) || model.getPhone2().equals(jpa.getPhone2()))){
				RestPreconditions.checkNull(storeRepo.findByPhoneNumber(model.getPhone2()), 
						"Add/Edit store for user "+model.getUsername(), "The store with that phone number belongs to another user");
			}
			jpa.setPhone2(model.getPhone2());
		}
		if(RestPreconditions.checkString(model.getPostalCode())){
			jpa.setPostalCode(model.getPostalCode());
		}
		
		return jpa;
	}

	@Override
	public StoreDTO convertJpaToModel(final StoreJPA jpa) {
		StoreDTO model = new StoreDTO();
		
		model.setDescription(jpa.getDescription());
		model.setGoogleMapsLocation(jpa.getGoogleMapsLocation());
		model.setId(model.getId());
		model.setName(jpa.getName());
		model.setStoreWebSite(jpa.getStoreWebSite());
		model.setOwner(userServiceImpl.convertJpaToModel(jpa.getUserJpa()));
		model.setUsername(jpa.getUserJpa().getUserSecurityJpa().getUsername());
		model.setAddress1(jpa.getAddress1());
		model.setAddress2(jpa.getAddress2());
		model.setCity(jpa.getCity());
		model.setCountry(jpa.getCountry());
		model.setPostalCode(jpa.getPostalCode());
		model.setEmail(jpa.getEmail());
		model.setPhone1(jpa.getPhone1());
		model.setPhone2(jpa.getPhone2());
		model.setAdded(jpa.getAdded());
		
		jpa.getSpecialOffers().stream().forEach(so -> model.getSpecialOffers().add(specialOfferServiceImpl.convertJpaToModel(so)));
		
		return model;
	}
	
	@Override
	public void getPostValidationErrors(StoreDTO model, List<String> list) {
		if(!RestPreconditions.checkString(model.getDescription())){
			list.add("Store description is mandatory");
		}
		if(!RestPreconditions.checkString(model.getName())){
			list.add("Store name is mandatory");
		}
		if(!(RestPreconditions.checkString(model.getAddress1()) && RestPreconditions.checkString(model.getAddress2()))){
			list.add("Store address is mandatory");
		}
		if(!(RestPreconditions.checkString(model.getPhone1()) && RestPreconditions.checkString(model.getPhone2()))){
			list.add("Store phone number is mandatory");
		}
		if(!RestPreconditions.checkString(model.getCity())){
			list.add("City is mandatory for store address");
		}
		if(!RestPreconditions.checkString(model.getCountry())){
			list.add("Country is mandatory for store address");
		}
		if(RestPreconditions.checkString(model.getPostalCode())){
			list.add("Postal code is mandatory for store address");
		}
		if(!RestPreconditions.checkString(model.getEmail())){
			list.add("Store email is mandatory");
		}
	}
	
	private boolean isPatchDataPresentBasic(final StoreDTO model){
		return RestPreconditions.checkString(model.getName()) ||
		RestPreconditions.checkString(model.getAddress1()) ||
		RestPreconditions.checkString(model.getAddress2()) ||
		RestPreconditions.checkString(model.getPhone1()) ||
		RestPreconditions.checkString(model.getPhone2()) ||
		RestPreconditions.checkString(model.getCity()) ||
		RestPreconditions.checkString(model.getCountry()) ||
		RestPreconditions.checkString(model.getPostalCode()) ||
		RestPreconditions.checkString(model.getEmail()) 
		;
	}

	@Override
	public boolean isPatchDataPresent(final StoreDTO model) {
		return isPatchDataPresentBasic(model) ||
				RestPreconditions.checkString(model.getGoogleMapsLocation()) ||
				RestPreconditions.checkString(model.getStoreWebSite()) ||
				RestPreconditions.checkString(model.getDescription())
				;
				//TODO ratings
	}

	@Override
	public JpaRepository<StoreJPA, Long> getRepository() {
		return storeRepo;
	}

	@Override
	public String getName() {
		return "Store";
	}

	
}
