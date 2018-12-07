package com.green.health.ratings.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.green.health.herb.dao.HerbRepository;
import com.green.health.illness.dao.IllnessRepository;
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

	@Autowired
	public RatingsServiceImpl(UserSecurityRepository userSecurityRepo, HerbRepository herbRepo,
			IllnessRepository illnessRepo) {
		this.userSecurityRepo = userSecurityRepo;
		this.herbRepo = herbRepo;
		this.illnessRepo = illnessRepo;
	}
	
	private LinkJPA findOneByHerbAndIllness(Long herbId, Long illnessId) {
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
		model.setIllnessLocalName(jpa.getIllness().getSrbName());
		
		model.setHerbId(jpa.getHerb().getId());
		model.setHerbLatinName(jpa.getHerb().getLatinName());
		model.setHerbLocalName(jpa.getHerb().getEngName());
		
		model.setRatings(jpa.calculateRating());
		
		return model;
	}
	
	@Override
	public RatingDTO getRatingForLink(Long herbId, Long illnessId) throws MyRestPreconditionsException {
		if(checkId(herbId) && checkId(illnessId)){
			LinkJPA jpa = RestPreconditions.checkNotNull(findOneByHerbAndIllness(herbId, illnessId),
					"Retreaving ratings for herb-illness link failed !",
					"Cannot find link with herbId="+herbId+" and illnessId="+illnessId);
			
			return convertJpaToModel(jpa);
		} else {
			MyRestPreconditionsException e = new MyRestPreconditionsException("Find herb-illness rating error!",
					"Some of the necessary fields are missing or invalid");
			
			if(!checkId(herbId)){
				e.getErrors().add("Herb id is missing or invalid");
			}
			if(!checkId(illnessId)){
				e.getErrors().add("Illness id is missing or invalid");
			}
			
			throw e;
		}
	}
	
	@Override
	public List<RatingDTO> getRatingsForHerbOrIllness(Long id, boolean ih) throws MyRestPreconditionsException {
		MyRestPreconditionsException e = new MyRestPreconditionsException("Find herb-illness rating error !",
				(ih ? "Herb" : "Illness")+" id invalid or no illness is linked to this herb");
		if(checkId(id)){
			if((ih && herbRepo.getOne(id)==null) || (!ih && illnessRepo.getOne(id)==null)) {
				throw e;
			}
			Set<LinkJPA> result = ih ? herbRepo.getOne(id).getLinks() : illnessRepo.getOne(id).getLinks();
			if(!(result!=null && !result.isEmpty())){
				throw e;
			}
			
			return result
					.stream()
					.map(i -> convertJpaToModel(i))
					.collect(Collectors.toList());
		} 
		
		throw e;
	}
	
	private boolean checkId(Long id){
		return id!=null && id>0;
	}
}