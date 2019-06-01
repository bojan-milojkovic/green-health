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
import com.green.health.ratings.service.RatingsService;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.security.repositories.UserSecurityRepository;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class RatingsServiceImpl implements RatingsService {

	private UserSecurityRepository userSecurityRepo;
	private HerbRepository herbRepo;
	private IllnessRepository illnessRepo;
	private HerbService herbServiceImpl;

	@Autowired
	public RatingsServiceImpl(UserSecurityRepository userSecurityRepo, HerbRepository herbRepo,
			IllnessRepository illnessRepo, HerbService herbServiceImpl) {
		this.userSecurityRepo = userSecurityRepo;
		this.herbRepo = herbRepo;
		this.illnessRepo = illnessRepo;
		this.herbServiceImpl = herbServiceImpl;
	}
	
	private LinkJPA findOneByHerbAndIllness(final Long herbId, final Long illnessId) {
		// ids are already checked.
		if(herbRepo.getOne(herbId)==null) {
			return null;
		}
		for(LinkJPA jpa : herbRepo.getOne(herbId).getLinks()){
			if(jpa.getIllness().getId() == illnessId){
				return jpa;
			}
		}
		return null;
	}
	
	@Override
	public void addNew(RatingDTO model) throws MyRestPreconditionsException {
		// model's herb/illness ids are already checked by @Valid
		
		// check username (not necessary, but just in case)
		UserSecurityJPA user = RestPreconditions.checkNotNull(userSecurityRepo.findByUsername(model.getUsername()), 
				"Add new herb-illness rating error!", "Cannot find the user with username = "+model.getUsername());
		
		// get the link :
		LinkJPA link = RestPreconditions.checkNotNull(
				findOneByHerbAndIllness(model.getHerbId(), model.getIllnessId()),
				"Add new herb-illness rating error!",
				"Cannot find link with herbId="+model.getHerbId()+" and illnessId="+ model.getIllnessId());
		
		// check that this user did not rate this link yet :
		RestPreconditions.assertTrue(!user.getLinks().contains(link), 
				"Add new herb-illness rating error!", 
				"The user "+model.getUsername()+" has already rated this herb-illness link");
		
		switch(model.getNewRatings()){
			case 1:
				link.setRatingOnes(link.getRatingOnes()+1);
				break;
			case 2:
				link.setRatingTwos(link.getRatingTwos()+1);
				break;
			case 3:
				link.setRatingThrees(link.getRatingThrees()+1);
				break;
			case 4:
				link.setRatingFours(link.getRatingFours()+1);
				break;
			default:
				link.setRatingFives(link.getRatingFives()+1);
				break;
		};
		
		// mark that user has rated this link
		user.getLinks().add(link);
		link.getRaters().add(user);
		
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
		
		LinkJPA jpa = RestPreconditions.checkNotNull(findOneByHerbAndIllness(herbId, illnessId),
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
}