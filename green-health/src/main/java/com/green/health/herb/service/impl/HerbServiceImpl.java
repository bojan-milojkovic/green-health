package com.green.health.herb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.green.health.herb.dao.HerbLocaleRepository;
import com.green.health.herb.dao.HerbRepository;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.herb.service.HerbService;
import com.green.health.illness.dao.IllnessRepository;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.illness.entities.IllnessJPA;
import com.green.health.images.storage.StorageService;
import com.green.health.ratings.entities.LinkJPA;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class HerbServiceImpl implements HerbService {

	private HerbRepository herbDao;
	private HerbLocaleRepository herbLocaleDao;
	private StorageService storageServiceImpl;
	private IllnessRepository illnessDao;
	
	@Autowired
	public HerbServiceImpl(HerbRepository herbDao, HerbLocaleRepository herbLocaleDao, StorageService storageServiceImpl, IllnessRepository illnessDao){
		this.herbDao = herbDao;
		this.herbLocaleDao = herbLocaleDao;
		this.storageServiceImpl = storageServiceImpl;
		this.illnessDao = illnessDao;
	}
	
	@Override
	public List<HerbDTO> getAll() {
		return herbDao.findAll().stream().map(jpa -> convertJpaToModel(jpa)).collect(Collectors.toList());
	}
	
	@Override
	public HerbDTO getOneById(Long id) throws MyRestPreconditionsException {
		checkId(id);
		HerbJPA jpa = RestPreconditions.checkNotNull(herbDao.getOne(id), "Cannot find the herb with id = "+id);
		return convertJpaToModel(jpa);
	}

	@Override
	public HerbDTO getHerbByLocalName(String EngName) {
		if(RestPreconditions.checkLocaleIsEnglish()) {
			return convertJpaToModel(herbDao.getHerbByEngName(EngName));
		} else {
			return convertJpaToModel(herbLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), EngName).getHerb());
		}
	}

	@Override
	public HerbDTO getHerbByLatinName(String latinName) {
		return convertJpaToModel(herbDao.getHerbByLatinName(latinName));
	}
	
	@Override
	public Resource getImage(Long id, boolean isThumbnail) throws MyRestPreconditionsException{
		RestPreconditions.assertTrue(id!=null && id>0, "Retreaving image error","Invalid herb id ("+id+")");
		if(isThumbnail){
			return storageServiceImpl.readImage(id, "herb_THUMBNAIL");
		} else {
			return storageServiceImpl.readImage(id, "herb.");
		}
	}

	@Override
	public void addNew(HerbDTO model) throws MyRestPreconditionsException {
		if(isPostDataPresent(model)) {
			// check that herb name is unique :
			RestPreconditions.checkSuchEntityAlreadyExists(herbDao.getHerbByLatinName(model.getLatinName()),
					"The herb with Latin name "+model.getLatinName()+" is already in our database.");
			RestPreconditions.checkSuchEntityAlreadyExists(herbDao.getHerbByEngName(model.getEngName()),
					"The herb with Serbian name "+model.getEngName()+" is already in our database.");

			HerbJPA jpa = herbDao.save(convertModelToJPA(model));
			
			if(model.getImage()!=null){
				// save image :
				storageServiceImpl.saveImage(model.getImage(), jpa.getId(), false);
			}
		} else {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("You cannot add this herb",
							"The following data is missing from the herb form");
			if(!RestPreconditions.checkStringMatches(model.getDescription(),"[A-Za-z0-9 .,:'()-]{10,}")){
				ex.getErrors().add("herb description");
			}
			if(!RestPreconditions.checkStringMatches(model.getGrowsAt(),"[A-Za-z0-9 .,:'()-]{10,}")){
				ex.getErrors().add("where the herb grows");
			}
			if(!RestPreconditions.checkStringMatches(model.getLatinName(),"[A-Za-z ]{3,}")){
				ex.getErrors().add("herb's latin name");
			}
			if(!RestPreconditions.checkStringMatches(model.getProperties(),"[A-Za-z0-9 .,:'()-]{10,}")){
				ex.getErrors().add("herb's use properties");
			}
			if(!RestPreconditions.checkStringMatches(model.getEngName(),"[A-Za-z ]{3,}")){
				ex.getErrors().add("herb's Serbian name");
			}
			if(!RestPreconditions.checkStringMatches(model.getWarnings(),"[A-Za-z0-9 .,:'()-]{10,}")){
				ex.getErrors().add("herb's use warnings");
			}
			if(!RestPreconditions.checkStringMatches(model.getWhenToPick(),"[A-Za-z0-9 .,:'()-]{10,}")){
				ex.getErrors().add("when to pick the herb");
			}
			if(model.getWhereToBuy()!=null && !model.getWhereToBuy().matches("([A-Za-z0-9 .,:'()-]{10,})|(^$)")){
				ex.getErrors().add("where to buy the herb");
			}
			throw ex;
		}
	}

	@Override
	public HerbDTO edit(HerbDTO model, Long id) throws MyRestPreconditionsException {
		
		RestPreconditions.assertTrue(isPatchDataPresent(model), "Herb edit error", "Your herb edit request is invalid - You must provide some editable data");
		model.setId(id);
		
		HerbJPA jpa = RestPreconditions.checkNotNull(convertModelToJPA(model), "Herb edit error", "Herb with id = "+id+" does not exist in our database.");

		// check that latin name is not taken :
		if(RestPreconditions.checkString(model.getLatinName())) {
			HerbJPA tmp = herbDao.getHerbByLatinName(model.getLatinName());
			if(tmp!=null) {
				RestPreconditions.assertTrue(tmp.getId()==jpa.getId(), "Herb edit error !", 
						"The latin name "+model.getLatinName()+" has already been assigned to another herb");
			}
		}
			
		herbDao.save(jpa);

		if(model.getImage()!=null){
			// save image :
			storageServiceImpl.saveImage(model.getImage(), jpa.getId(), false);
		}
		
		return convertJpaToModel(jpa);
	}

	@Override
	public void delete(final Long id) throws MyRestPreconditionsException {
		RestPreconditions.assertTrue(herbDao.getOne(id)!=null, "Herb delete error",
					"Herb with id = "+ id + " does not exist in our database.");
		herbDao.deleteById(id);
		
		storageServiceImpl.deleteImage(id, false);
	}

	@Override
	public HerbJPA convertModelToJPA(HerbDTO model) throws MyRestPreconditionsException {
		HerbJPA jpa = null;
		
		if(model.getId()==null){
			jpa = new HerbJPA();
			
			jpa.setDescription(model.getDescription());
			jpa.setGrowsAt(model.getGrowsAt());
			jpa.setLatinName(model.getLatinName());
			jpa.setEngName(model.getEngName());
			jpa.setProperties(model.getProperties());
			jpa.setWarnings(model.getWarnings());
			jpa.setWhenToPick(model.getWhenToPick());
			jpa.setWhereToBuy(model.getWhereToBuy());
		} else {
			jpa = herbDao.getOne(model.getId());
			if(jpa==null){ // in case of trying to edit a non existing herb 
				return null;
			}
			
			if(RestPreconditions.checkString(model.getDescription())){
				jpa.setDescription(model.getDescription());
			}
			if(RestPreconditions.checkString(model.getGrowsAt())){
				jpa.setGrowsAt(model.getGrowsAt());
			}
			if(RestPreconditions.checkString(model.getLatinName())){
				jpa.setLatinName(model.getLatinName());
			}
			if(RestPreconditions.checkString(model.getEngName())){
				jpa.setEngName(model.getEngName());
			}
			if(RestPreconditions.checkString(model.getProperties())){
				jpa.setProperties(model.getProperties());
			}
			if(RestPreconditions.checkString(model.getWarnings())){
				jpa.setWarnings(model.getWarnings());
			}
			if(RestPreconditions.checkString(model.getWhenToPick())){
				jpa.setWhenToPick(model.getWhenToPick());
			}
			if(RestPreconditions.checkString(model.getWhereToBuy())){
				jpa.setWhereToBuy(model.getWhereToBuy());
			}
		}
		
		// link illnesses :
		if(model.getIllnesses()!=null && !model.getIllnesses().isEmpty()){
			
			for(IllnessDTO illness : model.getIllnesses()){
				IllnessJPA ijpa = null;
				
				if(RestPreconditions.checkString(illness.getLatinName())){
					ijpa = illnessDao.findByLatinName(illness.getLatinName());
				
				} else if(RestPreconditions.checkString(illness.getSrbName())){
					ijpa = illnessDao.findBySrbName(illness.getSrbName());
				}
				
				if(ijpa!=null){
					// since 'links' is a HashSet there will be no duplicates :
					jpa.getLinks().add(new LinkJPA(jpa, ijpa));
				} else {
					throw new MyRestPreconditionsException("Link illness to herb error","Cannot find illness with latin name = "
							+illness.getLatinName()+" and serbian name = "+illness.getSrbName());
				}
			}
		}
		
		return jpa;
	}

	@Override
	public HerbDTO convertJpaToModel(HerbJPA jpa) {
		HerbDTO model = new HerbDTO();
		
		model.setDescription(jpa.getDescription());
		model.setGrowsAt(jpa.getGrowsAt());
		model.setId(jpa.getId());
		model.setLatinName(jpa.getLatinName());
		model.setEngName(jpa.getEngName());
		model.setProperties(jpa.getProperties());
		model.setWhenToPick(jpa.getWhenToPick());
		model.setWhereToBuy(jpa.getWhereToBuy());
		model.setWarnings(jpa.getWarnings());
		
		if(jpa.getLinks()!=null && !jpa.getLinks().isEmpty()){
			model.setIllnesses(new ArrayList<IllnessDTO>());
			for(LinkJPA ijpa : jpa.getLinks()){
				// this is more memory-efficient than autowiring IllnessService
				IllnessDTO imodel = new IllnessDTO();
				imodel.setId(ijpa.getIllness().getId());
				imodel.setSrbName(ijpa.getIllness().getSrbName());
				imodel.setLatinName(ijpa.getIllness().getLatinName());
				model.getIllnesses().add(imodel);
			}
		}
		
		return model;
	}

	@Override
	public boolean isPostDataPresent(HerbDTO model) {
		return RestPreconditions.checkString(model.getDescription()) && 
				RestPreconditions.checkString(model.getGrowsAt()) && 
				RestPreconditions.checkString(model.getLatinName()) &&
				RestPreconditions.checkString(model.getEngName()) && 
				RestPreconditions.checkString(model.getProperties()) && 
				RestPreconditions.checkString(model.getWarnings()) && 
				RestPreconditions.checkString(model.getWhenToPick())
				;
	}

	@Override
	public boolean isPatchDataPresent(HerbDTO model) {
		return RestPreconditions.checkString(model.getDescription()) ||
				RestPreconditions.checkString(model.getGrowsAt()) ||
				RestPreconditions.checkString(model.getLatinName()) ||
				RestPreconditions.checkString(model.getEngName()) ||
				RestPreconditions.checkString(model.getProperties()) ||
				RestPreconditions.checkString(model.getWarnings()) ||
				RestPreconditions.checkString(model.getWhenToPick()) ||
				RestPreconditions.checkString(model.getWhereToBuy()) ||
				(model.getIllnesses()!=null && !model.getIllnesses().isEmpty())
				;
	}
}