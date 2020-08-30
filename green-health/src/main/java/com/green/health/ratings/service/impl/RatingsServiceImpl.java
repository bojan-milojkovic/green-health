package com.green.health.ratings.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import com.green.health.herb.dao.HerbRepository;
import com.green.health.herb.entities.HerbLocaleJPA;
import com.green.health.herb.service.HerbService;
import com.green.health.illness.dao.IllnessRepository;
import com.green.health.illness.entities.IllnessLocaleJPA;
import com.green.health.ratings.entities.LinkJPA;
import com.green.health.ratings.entities.RatingDTO;
import com.green.health.ratings.repository.RatingsRepository;
import com.green.health.ratings.service.RatingsService;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.security.repositories.UserSecurityRepository;
import com.green.health.store.dao.ProductRepository;
import com.green.health.store.dao.StoreRepository;
import com.green.health.store.entities.ProductJPA;
import com.green.health.store.entities.StoreJPA;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class RatingsServiceImpl implements RatingsService {

	private UserSecurityRepository userSecurityRepo;
	private HerbRepository herbRepo;
	private IllnessRepository illnessRepo;
	private HerbService herbServiceImpl;
	private StoreRepository storeRepository;
	private ProductRepository productRepository;
	private RatingsRepository ratingsRepository;

	@Autowired
	public RatingsServiceImpl(UserSecurityRepository userSecurityRepo, HerbRepository herbRepo, IllnessRepository illnessRepo,
			 HerbService herbServiceImpl, StoreRepository storeRepository, ProductRepository productRepository, RatingsRepository ratingsRepository) {
		this.userSecurityRepo = userSecurityRepo;
		this.herbRepo = herbRepo;
		this.illnessRepo = illnessRepo;
		this.herbServiceImpl = herbServiceImpl;
		this.storeRepository = storeRepository;
		this.productRepository = productRepository;
		this.ratingsRepository = ratingsRepository;
	}
	
	public void addNewRatingStore(RatingDTO model) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(model.getStoreId()!=null, "Add new store rating error!", "You need a storeId to rate a store");
		
		// check username (not necessary, but just in case)
		UserSecurityJPA user = RestPreconditions.checkNotNull(userSecurityRepo.findByUsername(model.getUsername()), 
				"Add new store rating error!", "Cannot find the user with username = "+model.getUsername());
		
		// find store by storeId :
		StoreJPA sjpa = RestPreconditions.checkNotNull(storeRepository.getOne(model.getStoreId()),
				"Add new store rating error!", "Cannot find store with id = "+model.getStoreId());
		
		// check that this user did not rate this link yet :
		RestPreconditions.assertTrue(user.getStores().contains(sjpa),
				"Add new store rating error!", "The user "+model.getUsername()+" has already rated this store");
		
		sjpa.addNewRating(model.getNewRatings(), user);
		
		// mark that user has rated this link
		user.getStores().add(sjpa);
		
		userSecurityRepo.save(user);
	}
	
	public void addNewRatingProduct(RatingDTO model) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(model.getProductId()!=null, "Add new product rating error!", "You need a productId to rate a product");
		
		UserSecurityJPA user = RestPreconditions.checkNotNull(userSecurityRepo.findByUsername(model.getUsername()), 
				"Add new product rating error!", "Cannot find the user with username = "+model.getUsername());
		
		ProductJPA pjpa = RestPreconditions.checkNotNull(productRepository.getOne(model.getProductId()),
				"Add new product rating error!", "Cannot find product with id = "+model.getProductId());
		
		RestPreconditions.assertTrue(!user.getProducts().contains(pjpa),
				"Add new product rating error!", "The user "+model.getUsername()+" has already rated this product");
		
		pjpa.addNewRating(model.getNewRatings(), user);
		user.getProducts().add(pjpa);
		
		userSecurityRepo.save(user);
	}
	
	@Override
	public void addNewRatingLink(RatingDTO model) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(model.getHerbId()!=null && model.getIllnessId()!=null, 
				"Add new herb-illness rating error!", "You need a herbId and illnessId to rate a herb-illness link");
		
		// check username (not necessary, but just in case)
		UserSecurityJPA user = RestPreconditions.checkNotNull(userSecurityRepo.findByUsername(model.getUsername()), 
				"Add new herb-illness rating error!", "Cannot find the user with username = "+model.getUsername());
		
		// get the link :
		LinkJPA link = RestPreconditions.checkNotNull(
				ratingsRepository.findByHerbAndIllness(model.getHerbId(), model.getIllnessId()),
				"Add new herb-illness rating error!",
				"Cannot find link with herbId="+model.getHerbId()+" and illnessId="+ model.getIllnessId());
		
		// check that this user did not rate this link yet :
		RestPreconditions.assertTrue(!user.getLinks().contains(link), 
				"Add new herb-illness rating error!", "The user "+model.getUsername()+" has already rated this herb-illness link");
		
		link.addNewRating(model.getNewRatings(), user);
		
		// mark that user has rated this link
		user.getLinks().add(link);
		
		userSecurityRepo.save(user);
	}

	private RatingDTO convertJpaToModel(LinkJPA jpa) {
		RatingDTO model = new RatingDTO();
		
		model.setIllnessId(jpa.getIllness().getId());
		model.setIllnessLatinName(jpa.getIllness().getLatinName());
		model.setIllnessLocalName(jpa.getIllness().getEngName());
		
		model.setHerbId(jpa.getHerb().getId());
		model.setHerbLatinName(jpa.getHerb().getLatinName());
		model.setHerbLocalName(jpa.getHerb().getEngName());

		// if locale is not English, set local names :
		if(!RestPreconditions.checkLocaleIsEnglish()){
			String locale = LocaleContextHolder.getLocale().toString();
			
			IllnessLocaleJPA itmp = jpa.getIllness().getForSpecificLocale(locale);
			if(itmp!=null) {
				model.setIllnessLocalName(itmp.getLocalName());
			}
			
			HerbLocaleJPA htmp = jpa.getHerb().getForSpecificLocale(locale);
			if(htmp!=null) {
				model.setHerbLocalName(htmp.getLocalName());
			}
		}
		model.setRatings(jpa.calculateRating());
		
		return model;
	}
	
	@Override
	public RatingDTO getRatingForLink(final Long herbId, final Long illnessId) throws MyRestPreconditionsException {
		herbServiceImpl.checkId(herbId, "Get ratings for link - herbId invalid");
		herbServiceImpl.checkId(illnessId, "Get ratings for link - illnessId invalid");
		
		LinkJPA jpa = RestPreconditions.checkNotNull(ratingsRepository.findByHerbAndIllness(herbId, illnessId),
				"Retreaving ratings for herb-illness link failed !",
				"Cannot find link with herbId="+herbId+" and illnessId="+illnessId);
			
		return convertJpaToModel(jpa);
	}
	
	@Override
	public List<RatingDTO> getRatingsForHerbOrIllness(final Long id, final boolean ih) throws MyRestPreconditionsException {
		
		herbServiceImpl.checkId(id, "Find herb-illness rating error !");
		
		Set<LinkJPA> result = null;
		if(ih){
			result = RestPreconditions.checkNotNull(herbRepo.getOne(id), 
				"Find herb-illness rating error !", "No herb found for that id").getLinks();
		} else {
			result = RestPreconditions.checkNotNull(illnessRepo.getOne(id), 
				"Find herb-illness rating error !", "No illness found for that id").getLinks();
		}
		
		RestPreconditions.assertTrue(result!=null && !result.isEmpty(), 
				"Find herb-illness rating error !", "No herb-illness link exists");
		
		return result
				.stream()
				.map(i -> convertJpaToModel(i))
				.collect(Collectors.toList());
		
	}

	@Override
	public RatingDTO getRatingForStore(Long sid) throws MyRestPreconditionsException {
		return RatingDTO.buildStoreRating(sid, getRatingFor(sid, true));
	}

	@Override
	public RatingDTO getRatingForProduct(Long pid) throws MyRestPreconditionsException {
		return RatingDTO.buildProductRating(pid, getRatingFor(pid, false));
	}
	
	private double getRatingFor(Long id, boolean type) throws MyRestPreconditionsException {
		String stype = type ? "store" : "product";
		
		herbServiceImpl.checkId(id, "Get ratings for "+stype+" error");
		
		return RestPreconditions.checkNotNull((type ? storeRepository : productRepository).getOne(id),
				"Get ratings for "+stype+" error", "Cannot find "+stype+" with id = "+id).calculateRating();
	}
}