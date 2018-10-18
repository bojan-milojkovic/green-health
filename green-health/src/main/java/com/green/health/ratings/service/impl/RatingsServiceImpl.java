package com.green.health.ratings.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.green.health.ratings.dao.LinkRepository;
import com.green.health.ratings.dao.RatingsRepository;
import com.green.health.ratings.entities.LinkJPA;
import com.green.health.ratings.entities.RatingsDTO;
import com.green.health.ratings.entities.RatingsJPA;
import com.green.health.ratings.service.RatingsService;
import com.green.health.user.dao.UserRepository;
import com.green.health.user.entities.UserJPA;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class RatingsServiceImpl implements RatingsService {
	
	private RatingsRepository ratingsDao;
	private LinkRepository linkDao;
	private UserRepository userDao;
	
	@Autowired
	public RatingsServiceImpl(RatingsRepository ratingsDto, LinkRepository linkDao, UserRepository userDao) {
		this.ratingsDao = ratingsDto;
		this.linkDao = linkDao;
		this.userDao = userDao;
	}
	
	@Override
	public RatingsJPA convertModelToJPA(RatingsDTO model) throws MyRestPreconditionsException {
		RatingsJPA jpa = null;
		
		LinkJPA link = RestPreconditions.checkNotNull(linkDao.findOneByHerbAndIllness(model.getHerb().getId(), model.getIllness().getId()),
					"No herb-illness link found for herbId="+model.getHerb().getId()+" and illnessId="+model.getIllness().getId());
		
		if(model.getId() == null) {
			jpa = new RatingsJPA();
			
			UserJPA user = RestPreconditions.checkNotNull(userDao.getOne(model.getUserId()),
					"No user found with id="+model.getUserId());
			
			// check that this user did not rate this link yet :
			RestPreconditions.checkSuchEntityAlreadyExists(ratingsDao.findRatingsByUserAndLink(user.getId(), link.getId()), 
					"Every herbalist can rate each herb-illness link only once.");
			
			jpa.setLink(link);
			jpa.setRaters(user);
		} else {
			jpa = RestPreconditions.checkNotNull(ratingsDao.getOne(model.getId()), 
					"Edit ratings error", 
					"Cannot find ratings with id="+model.getId());
			// delete old ratings :
			jpa.setRatingOnes(0);
			jpa.setRatingTwos(0);
			jpa.setRatingThrees(0);
			jpa.setRatingFours(0);
			jpa.setRatingFives(0);
		}
		
		// add new ratings :
		switch(model.getNewRatings()) {
			case 1 :
				jpa.setRatingOnes(1);
				break;
			case 2 :
				jpa.setRatingTwos(1);
				break;
			case 3 :
				jpa.setRatingThrees(1);
				break;
			case 4 :
				jpa.setRatingFours(1);
				break;
			default :
				jpa.setRatingFives(1);
				break;
		};
		
		return jpa;
	}

	@Override
	public RatingsDTO convertJpaToModel(RatingsJPA jpa) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPostDataPresent(RatingsDTO model) {
		// all these fields must be present for edit too + userId :
		return isPatchDataPresent(model) && model.getUserId()!=null;
	}

	@Override
	public boolean isPatchDataPresent(RatingsDTO model) {
		return model.getHerb().getId()!=null && model.getHerb().getId()>0 &&
				model.getIllness().getId()!=null && model.getIllness().getId()>0 &&
				model.getNewRatings()!=null;
	}

	@Override
	public List<RatingsDTO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RatingsDTO getOneById(Long id) throws MyRestPreconditionsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addNew(RatingsDTO model) throws MyRestPreconditionsException {
		if(isPostDataPresent(model)) {
			ratingsDao.save(convertModelToJPA(model));
		} else {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("Add ratings error !",
					"One or more properties is missing from the request body.");
			
			if(model.getHerb().getId()==null) {
				ex.getErrors().add("Herb id cannot be null");
			}
			if(model.getIllness().getId()==null) {
				ex.getErrors().add("Illness id cannot be null");
			}
			if(model.getNewRatings()==null) {
				ex.getErrors().add("You must enter a valid ratings [1-5]");
			}
			if(model.getUserId()==null) {
				ex.getErrors().add("User id cannot be null");
			}
			
			throw ex;
		}
	}

	@Override
	public RatingsDTO edit(RatingsDTO model, Long id) throws MyRestPreconditionsException {
		checkId(id);
		
		if(isPatchDataPresent(model)) {
			model.setId(id);
			
			return convertJpaToModel(convertModelToJPA(model));
		} else {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("Add ratings error !",
					"One or more properties is missing from the request body.");
			
			if(model.getHerb().getId()==null) {
				ex.getErrors().add("Herb id cannot be null");
			}
			if(model.getIllness().getId()==null) {
				ex.getErrors().add("Illness id cannot be null");
			}
			if(model.getNewRatings()==null) {
				ex.getErrors().add("You must enter a valid ratings [1-5]");
			}
			
			throw ex;
		}
	}

	@Override
	public void delete(Long id) throws MyRestPreconditionsException {
		// TODO Auto-generated method stub
		
	}

}
