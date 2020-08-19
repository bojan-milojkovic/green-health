package com.green.health.store.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.green.health.images.storage.StorageService;
import com.green.health.images.storage.StorageService.ImgType;
import com.green.health.store.dao.StoreCriteriaRepository;
import com.green.health.store.dao.StoreRepository;
import com.green.health.store.entities.ProductJPA;
import com.green.health.store.entities.StoreDTO;
import com.green.health.store.entities.StoreJPA;
import com.green.health.store.service.ProductService;
import com.green.health.store.service.StoreService;
import com.green.health.user.dao.UserRepository;
import com.green.health.user.entities.UserJPA;
import com.green.health.user.service.UserService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class StoreServiceImpl implements StoreService {
	private StoreRepository storeRepo;
	private ProductService productServiceImpl;
	private UserService userServiceImpl;
	private UserRepository userRepo;
	private StoreCriteriaRepository storeCriteriaRepo;
	private StorageService storageServiceImpl;
	
	private final int MAX_NUM_STORES = 3;
	
	@Autowired
	public StoreServiceImpl(StoreRepository storeRepo, ProductService productServiceImpl, UserService userServiceImpl, UserRepository userRepo, StoreCriteriaRepository storeCriteriaRepo, StorageService storageServiceImpl) {
		super();
		this.storeRepo = storeRepo;
		this.productServiceImpl = productServiceImpl;
		this.userRepo = userRepo;
		this.userServiceImpl = userServiceImpl;
		this.storeCriteriaRepo = storeCriteriaRepo;
		this.storageServiceImpl = storageServiceImpl;
	}
	
	private List<StoreDTO> convertJpaListToDtoList(Collection<StoreJPA> jpaCollection) throws MyRestPreconditionsException {
		List<StoreDTO> result = new ArrayList<>();
		for(StoreJPA jpa : jpaCollection){
			result.add(completeConversion(jpa));
		}
		return result;
	}
	
	public List<StoreDTO> getMyStores(final String username) throws MyRestPreconditionsException {
		Set<StoreJPA> jpas = userServiceImpl.getStoreByUser(username);
		RestPreconditions.assertTrue(!jpas.isEmpty(), "Error getting user's stores", "No stores exist for user "+username);
		return convertJpaListToDtoList(jpas);
	}
	
	public List<StoreDTO> getStoresByProperties(final StoreDTO model) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(RestPreconditions.checkString(model.getPhone()) || isPatchDataPresentBasic(model),"Error retreaving stores","You must provide some search criteria.");
		return convertJpaListToDtoList(storeCriteriaRepo.getStoresByProperties(model));
	}
	
	public List<StoreDTO> getAllStores() throws MyRestPreconditionsException {
		return convertJpaListToDtoList(storeRepo.findAll());
	}

	@Override
	public void addNew(final StoreDTO model) throws MyRestPreconditionsException{
		StoreService.super.addNew(model);
		
		// user must exist because it passed security
		UserJPA ujpa = userRepo.findByUsername(model.getUsername());
		
		// check how many stores the user has :
		RestPreconditions.assertTrue(ujpa.getStoreJpa().size()<MAX_NUM_STORES, 
				"Adding new store error", "The user "+model.getUsername()+" already has the maximum number of stores allowed.");
		
		StoreJPA jpa = convertModelToJPA(model);
		jpa.setUserJpa(ujpa);
		
		jpa = getRepository().save(jpa);
		
		if(model.getImage()!=null){
			// save image :
			storageServiceImpl.saveImage(model.getImage(), jpa.getId(), ImgType.store);
		}
	}
	
	public boolean checkUserOwnsTheStore(final String username, final String phone) throws MyRestPreconditionsException {
		// check user is editing his store and not someone elses's :
		return userRepo.findByUsername(username).getStoreJpa().contains(new StoreJPA(phone));
	}
	
	public StoreDTO edit(StoreDTO model, final Long id) throws MyRestPreconditionsException{
		model = StoreService.super.edit(model, id);
		
		RestPreconditions.assertTrue( 
			checkUserOwnsTheStore(model.getUsername(), model.getPhone()),
			"Edit store error", "You cannot edit someone else's store");
		
		StoreJPA jpa = storeRepo.save(convertModelToJPA(model)); 
		
		if(model.getImage()!=null){
			// save image :
			storageServiceImpl.saveImage(model.getImage(), jpa.getId(), ImgType.store);
		}
		
		return completeConversion(jpa);
	}
	
	private StoreDTO completeConversion(StoreJPA jpa) throws MyRestPreconditionsException{
		StoreDTO model = convertJpaToModel(jpa);
		for(ProductJPA so : jpa.getSpecialOffers()){
			model.getSpecialOffers().add(productServiceImpl.completeConversion(so));
		}
		
		return model;
	}
	
	@Override
	public StoreJPA convertModelToJPA(final StoreDTO model) throws MyRestPreconditionsException {
		StoreJPA jpa = null;
		
		if(model.getId()==null){
			jpa = new StoreJPA();
			jpa.setAdded(LocalDate.now());
			jpa.setUsername(model.getUsername());
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
			jpa.setName(model.getName());
		}
		if(RestPreconditions.checkString(model.getStoreWebSite())){
			jpa.setStoreWebSite(model.getStoreWebSite());
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
						addOrEdit(model)+" store for user "+model.getUsername(), "The store with that email belongs to another user");
			}
			jpa.setEmail(model.getEmail());
		}
		if(RestPreconditions.checkString(model.getPhone())){
			if(!model.getPhone().equals(jpa.getPhone())){
				RestPreconditions.checkNull(storeRepo.findByPhoneNumber(model.getPhone()),
						addOrEdit(model)+" store for user "+model.getUsername(), "The store with that phone number belongs to another user");
			}
			jpa.setPhone(model.getPhone());
		}
		if(RestPreconditions.checkString(model.getPostalCode())){
			jpa.setPostalCode(model.getPostalCode());
		}
		if(RestPreconditions.checkString(model.getWorkHours())){
			jpa.setWorkHours(model.getWorkHours());
		}
		if(model.getClosedUntil()!=null){
			jpa.setClosedUntil(model.getClosedUntil());
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
		model.setUsername(jpa.getUsername());
		model.setAddress1(jpa.getAddress1());
		model.setAddress2(jpa.getAddress2());
		model.setCity(jpa.getCity());
		model.setCountry(jpa.getCountry());
		model.setPostalCode(jpa.getPostalCode());
		model.setEmail(jpa.getEmail());
		model.setPhone(jpa.getPhone());
		model.setAdded(jpa.getAdded());
		model.setWorkHours(jpa.getWorkHours());
		model.setClosedUntil(jpa.getClosedUntil());
		
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
		if(!RestPreconditions.checkString(model.getAddress1())){
			list.add("Store address1 is mandatory");
		}
		if(!RestPreconditions.checkString(model.getPhone())){
			list.add("Store phone number is mandatory");
		}
		if(!RestPreconditions.checkString(model.getCity())){
			list.add("City is mandatory for store address");
		}
		if(!RestPreconditions.checkString(model.getCountry())){
			list.add("Country is mandatory for store address");
		}
		if(!RestPreconditions.checkString(model.getPostalCode())){
			list.add("Postal code is mandatory for store address");
		}
		if(model.getClosedUntil()!=null && model.getClosedUntil().isBefore(LocalDate.now())){
			list.add("ClosedUntil date cannot be in the past");
		}
		if(!RestPreconditions.checkString(model.getWorkHours())){
			list.add("Store work hours are mandatory");
		} else if(!checkWorkHours(model.getWorkHours())){
			list.add("Work hours are in an invalid format");
		}
	}
	
	private boolean checkWorkHours(String wh){
		for(String part : wh.split("#")){
			if(!part.isEmpty()){
				String[] numbers = part.split("-");
				if(!(Integer.valueOf(numbers[0])<25 && Integer.valueOf(numbers[1])<25 && Integer.valueOf(numbers[0])<Integer.valueOf(numbers[1]))){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isPatchDataPresentBasic(final StoreDTO model){
		return  RestPreconditions.checkString(model.getName()) ||
		RestPreconditions.checkString(model.getAddress1()) ||
		RestPreconditions.checkString(model.getAddress2()) ||
		RestPreconditions.checkString(model.getCity()) ||
		RestPreconditions.checkString(model.getCountry()) ||
		RestPreconditions.checkString(model.getPostalCode()) ||
		RestPreconditions.checkString(model.getEmail()) ||
		(RestPreconditions.checkString(model.getWorkHours()) && checkWorkHours(model.getWorkHours()))
		;
	}

	@Override
	public boolean isPatchDataPresent(final StoreDTO model) {
		return RestPreconditions.checkString(model.getPhone()) && (isPatchDataPresentBasic(model) ||
				RestPreconditions.checkString(model.getGoogleMapsLocation()) ||
				RestPreconditions.checkString(model.getStoreWebSite()) ||
				(model.getClosedUntil()!=null && model.getClosedUntil().isAfter(LocalDate.now())) ||
				RestPreconditions.checkString(model.getDescription())
				);
				//TODO ratings
	}
	
	@Override
	public void deleteStore(final Long id, final String username) throws MyRestPreconditionsException {
		checkId(id,"Delete "+getName()+" error");
		
		StoreJPA jpa = RestPreconditions.checkNotNull(getRepository().getOne(id), "Delete store error",
				"Store with id = "+ id + " does not exist in our database.");
		
		RestPreconditions.assertTrue(jpa.getUsername().equals(username), "Delete store error", "User "+username+" does not own this store.");
		
		getRepository().delete(jpa);
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