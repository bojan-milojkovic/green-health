package com.green.health.herb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import com.green.health.herb.dao.HerbLocaleRepository;
import com.green.health.herb.dao.HerbRepository;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbInterface;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.herb.entities.HerbLocaleJPA;
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
		return convertJpaToModel(RestPreconditions.checkNotNull(herbDao.getOne(id), "Cannot find the herb with id = "+id));
	}

	@Override
	public HerbDTO getHerbByLocalName(String name) throws MyRestPreconditionsException {
		return convertJpaToModel(RestPreconditions.checkNotNull(
				(RestPreconditions.checkLocaleIsEnglish() ?
						herbDao.getHerbByEngName(name) :
						herbLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), name).getHerb())
				,"No such herb in database","Cannot find the herb with name '"+name+"'."));
	}

	@Override
	public HerbDTO getHerbByLatinName(String latinName) throws MyRestPreconditionsException {
		return convertJpaToModel(RestPreconditions.checkNotNull(herbDao.getHerbByLatinName(latinName),"Cannot find the herb with latin name '"+latinName+"'"));
	}
	
	@Override
	public Resource getImage(Long id, boolean isThumbnail) throws MyRestPreconditionsException{
		if(isThumbnail){
			return storageServiceImpl.readImage(id, "herb_THUMBNAIL");
		} else {
			return storageServiceImpl.readImage(id, "herb.");
		}
	}

	@Override
	public void addNew(HerbDTO model) throws MyRestPreconditionsException {
		// POST new is only with image, so model is never null
		model.setId(null);
		isPostDataPresent(model);
		
		// check that herb name is unique :
		RestPreconditions.checkSuchEntityAlreadyExists(herbDao.getHerbByLatinName(model.getLatinName()),
				"The herb with Latin name "+model.getLatinName()+" is already in our database.");
		
		if(RestPreconditions.checkLocaleIsEnglish()) {
			RestPreconditions.checkSuchEntityAlreadyExists(herbDao.getHerbByEngName(model.getLocalName()),
				"We assert your locale as English. The herb with eng. name "+model.getLocalName()+" is already in our database.");
		} else {
			RestPreconditions.checkSuchEntityAlreadyExists(
				// 'if' ensures that LocaleContextHolder.getLocale() is not null
				herbLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), model.getLocalName()),
					"The herb with local name "+model.getLocalName()+" is already in our database.");
		}

		if(model.getImage()!=null){
			// save image :
			storageServiceImpl.saveImage(model.getImage(), model.getId(), false);
		}
		
		herbDao.save(convertModelToJPA(model));
	}

	@Override
	public HerbDTO edit(HerbDTO model, Long id) throws MyRestPreconditionsException {
		RestPreconditions.checkNotNull(model, "Herb edit error",
				"You are sending a request without body");
		checkId(id);
		model.setId(id);
		RestPreconditions.assertTrue(isPatchDataPresent(model), "Herb edit error", 
				"Your edit request is invalid - You must provide some editable data");

		// check that latin name is not taken :
		if(RestPreconditions.checkString(model.getLatinName())) {
			HerbJPA jpa = herbDao.getHerbByLatinName(model.getLatinName());
			if(jpa!=null) {
				RestPreconditions.assertTrue(jpa.getId()==id, "Herb edit error", 
						"The latin name "+model.getLatinName()+" has already been assigned to another herb");
			}
		}
		// check that new locale name is not taken:
		if(RestPreconditions.checkString(model.getLocalName())) {
			if(RestPreconditions.checkLocaleIsEnglish()) {
				RestPreconditions.checkSuchEntityAlreadyExists(herbDao.getHerbByEngName(model.getLocalName()), 
						"We assert your locale as English. The herb with eng. name "+model.getLocalName()+" is already in our database.");
			} else {
				// 'if' ensures that LocaleContextHolder.getLocale() is not null
				RestPreconditions.checkSuchEntityAlreadyExists(
						herbLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), model.getLocalName()),
						"The herb with local name "+model.getLocalName()+" is already in our database.");
			}
		}

		if(model.getImage()!=null){
			// save image :
			storageServiceImpl.saveImage(model.getImage(), model.getId(), false);
		}
		
		return convertJpaToModel(herbDao.save(convertModelToJPA(model)));
	}
	
	private void useSettersInConvertToJPA(HerbInterface model, HerbInterface jpa){
		if(RestPreconditions.checkString(model.getDescription())){
			jpa.setDescription(model.getDescription());
		}
		if(RestPreconditions.checkString(model.getGrowsAt())){
			jpa.setGrowsAt(model.getGrowsAt());
		}
		if(RestPreconditions.checkString(model.getLocalName())){
			jpa.setLocalName(model.getLocalName());
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

	@Override
	public HerbJPA convertModelToJPA(HerbDTO model) throws MyRestPreconditionsException {
		HerbJPA jpa = null;
		
		if(model.getId()==null){
			jpa = new HerbJPA();
		} else {
			jpa = RestPreconditions.checkNotNull(herbDao.getOne(model.getId()), 
					"Herb edit error", "Herb with id = "+model.getId()+" does not exist in our database.");
		}
		if(RestPreconditions.checkString(model.getLatinName())){
			jpa.setLatinName(model.getLatinName());
		}
		// english
		if(RestPreconditions.checkLocaleIsEnglish()) {
			useSettersInConvertToJPA(model, jpa);
		} else {
			
			// 'if' above ensures that LocaleContextHolder.getLocale() is not null
			HerbLocaleJPA hjpa = jpa.getForSpecificLocale(LocaleContextHolder.getLocale().toString());
			if(hjpa==null) {
				hjpa = new HerbLocaleJPA();
				if(jpa.getId()==null){
					// herb needs to have id before we put HerbLocale into its set
					jpa = herbDao.save(jpa);
					//TODO: if it is a brand new herb, email admin to fill in english info
				}
				
				hjpa.setLocale(LocaleContextHolder.getLocale().toString());
				hjpa.setHerb(jpa);
				jpa.getHerbLocales().add(hjpa);
			}

			useSettersInConvertToJPA(model, hjpa);
		}
		
		// link illnesses :
		if(model.getIllnesses()!=null && !model.getIllnesses().isEmpty()){
			
			for(IllnessDTO illness : model.getIllnesses()){
				IllnessJPA ijpa = null;
				
				if(RestPreconditions.checkString(illness.getLatinName())){
					ijpa = illnessDao.findByLatinName(illness.getLatinName());
				
				} else if(RestPreconditions.checkString(illness.getLocalName())){
					ijpa = illnessDao.findByEngName(illness.getLocalName());
				}
				
				if(ijpa!=null){
					// since 'links' is a HashSet there will be no duplicates :
					jpa.getLinks().add(new LinkJPA(jpa, ijpa));
				} else {
					throw new MyRestPreconditionsException("Link illness to herb error","Cannot find illness with latin name = "
							+illness.getLatinName()+" and local name = "+illness.getLocalName());
				}
			}
		}
		
		return jpa;
	}
	
	private void useSettersInConvertToModel(HerbInterface model, HerbInterface jpa){
		model.setLocalName(jpa.getLocalName());
		model.setDescription(jpa.getDescription());
		model.setGrowsAt(jpa.getGrowsAt());
		model.setProperties(jpa.getProperties());
		model.setWhenToPick(jpa.getWhenToPick());
		model.setWhereToBuy(jpa.getWhereToBuy());
		model.setWarnings(jpa.getWarnings());
	}

	@Override
	public HerbDTO convertJpaToModel(HerbJPA jpa) {		
		HerbDTO model = new HerbDTO();
		
		model.setId(jpa.getId());
		model.setLatinName(jpa.getLatinName());
		
		boolean isEnglish = RestPreconditions.checkLocaleIsEnglish();
		if(!isEnglish) {
			// 'if' ensures that LocaleContextHolder.getLocale() is not null
			HerbLocaleJPA hjpa = jpa.getForSpecificLocale(LocaleContextHolder.getLocale().toString());
			if(hjpa!=null) {
				useSettersInConvertToModel(model, hjpa);
			} else {
				isEnglish = true;
			}
		}
		
		if(isEnglish) {
			useSettersInConvertToModel(model, jpa);
		}
		
		if(jpa.getLinks()!=null && !jpa.getLinks().isEmpty()){
			model.setIllnesses(new ArrayList<IllnessDTO>());
			for(LinkJPA ijpa : jpa.getLinks()){
				// this is more memory-efficient than autowiring IllnessService
				IllnessDTO imodel = new IllnessDTO();
				imodel.setId(ijpa.getIllness().getId());
				
				if(RestPreconditions.checkLocaleIsEnglish()){
					imodel.setLocalName(ijpa.getIllness().getEngName());
				} else {
					imodel.setLocalName(ijpa.getIllness().getNameForSpecificLocale(LocaleContextHolder.getLocale().toString()));
				}
				imodel.setLatinName(ijpa.getIllness().getLatinName());
				model.getIllnesses().add(imodel);
			}
		}
		
		return model;
	}

	@Override
	public void isPostDataPresent(HerbDTO model) throws MyRestPreconditionsException {
		// use .checkStringMatches() here because model object is created from json in controller
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
		if(!RestPreconditions.checkStringMatches(model.getLocalName(),"[A-Za-z ]{3,}")){
			ex.getErrors().add("herb's local name");
		}
		if(!RestPreconditions.checkStringMatches(model.getWarnings(),"[A-Za-z0-9 .,:'()-]{10,}")){
			ex.getErrors().add("herb's use warnings");
		}
		if(!RestPreconditions.checkStringMatches(model.getWhenToPick(),"[A-Za-z0-9 .,:'()-]{10,}")){
			ex.getErrors().add("when to pick the herb");
		}
		if(model.getWhereToBuy()!=null && !model.getWhereToBuy().matches("([A-Za-z0-9 .,:'()-]{10,})|(^$)")){ // regexp allows for ""
			ex.getErrors().add("where to buy the herb");
		}
		
		if(!ex.getErrors().isEmpty()) {
			throw ex;
		}
	}

	@Override
	public boolean isPatchDataPresent(HerbDTO model) {
		return RestPreconditions.checkString(model.getDescription()) ||
				RestPreconditions.checkString(model.getGrowsAt()) ||
				RestPreconditions.checkString(model.getLatinName()) ||
				RestPreconditions.checkString(model.getLocalName()) ||
				RestPreconditions.checkString(model.getProperties()) ||
				RestPreconditions.checkString(model.getWarnings()) ||
				RestPreconditions.checkString(model.getWhenToPick()) ||
				RestPreconditions.checkString(model.getWhereToBuy()) ||
				(model.getIllnesses()!=null && !model.getIllnesses().isEmpty())
				;
	}

	@Override
	public JpaRepository<HerbJPA, Long> getRepository() {
		return herbDao;
	}
}