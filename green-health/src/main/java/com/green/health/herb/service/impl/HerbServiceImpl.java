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
		HerbJPA jpa = null;
		if(RestPreconditions.checkLocaleIsEnglish()) {
			jpa = herbDao.getHerbByEngName(name);
		} else {
			// 'if' ensures that LocaleContextHolder.getLocale() is not null
			jpa = herbLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), name).getHerb();
		}
		if(jpa!=null) {
			return convertJpaToModel(jpa);
		}
		throw new MyRestPreconditionsException("No such herb in database","Cannot find the herb with name '"+name+"'.");
	}

	@Override
	public HerbDTO getHerbByLatinName(String latinName) throws MyRestPreconditionsException {
		return convertJpaToModel(RestPreconditions.checkNotNull(herbDao.getHerbByLatinName(latinName),"Cannot find the herb with latin name '"+latinName+"'"));
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
			
			if(RestPreconditions.checkLocaleIsEnglish()) {
				RestPreconditions.checkSuchEntityAlreadyExists(herbDao.getHerbByEngName(model.getLocalName()),
					"We assert your locale as English. The herb with name "+model.getLocalName()+" is already in our database.");
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
			if(!RestPreconditions.checkStringMatches(model.getLocalName(),"[A-Za-z ]{3,}")){
				ex.getErrors().add("herb's local name");
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
		
		RestPreconditions.assertTrue(isPatchDataPresent(model), "Herb edit error", 
				"Your herb edit request is invalid - You must provide some editable data");
		model.setId(id);

		// check that latin name is not taken :
		if(RestPreconditions.checkString(model.getLatinName())) {
			HerbJPA jpa = herbDao.getHerbByLatinName(model.getLatinName());
			if(jpa!=null) {
				RestPreconditions.assertTrue(jpa.getId()==id, "Herb edit error !", 
						"The latin name "+model.getLatinName()+" has already been assigned to another herb");
			}
		}
		// check that new locale name is not taken:
		if(model.getLocalName()!=null) {
			HerbJPA jpa = null;
			if(RestPreconditions.checkLocaleIsEnglish()) {
				jpa = herbDao.getHerbByEngName(model.getLocalName());
			} else {
				// 'if' ensures that LocaleContextHolder.getLocale() is not null
				jpa = herbLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), model.getLocalName()).getHerb();
			}
			if(jpa!=null) {
				RestPreconditions.assertTrue(jpa.getId()==id, "Herb edit error", 
						"The local name '"+model.getLocalName()+"' belongs to another herb in our database.");
			}
		}

		if(model.getImage()!=null){
			// save image :
			storageServiceImpl.saveImage(model.getImage(), model.getId(), false);
		}
		
		return convertJpaToModel(herbDao.save(convertModelToJPA(model)));
	}

	@Override
	public void delete(final Long id) throws MyRestPreconditionsException {
		checkId(id);
		
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
		} else {
			jpa = RestPreconditions.checkNotNull(herbDao.getOne(model.getId()), 
					"Herb edit error", "Herb with id = "+model.getId()+" does not exist in our database.");
		}
		if(RestPreconditions.checkString(model.getLatinName())){
			jpa.setLatinName(model.getLatinName());
		}
		// english
		if(RestPreconditions.checkLocaleIsEnglish()) {
			if(RestPreconditions.checkString(model.getDescription())){
				jpa.setDescription(model.getDescription());
			}
			if(RestPreconditions.checkString(model.getGrowsAt())){
				jpa.setGrowsAt(model.getGrowsAt());
			}
			if(RestPreconditions.checkString(model.getLocalName())){
				jpa.setEngName(model.getLocalName());
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
		} else {
			
			if(model.getId()==null) {
				//TODO: if it is a brand new herb, email admin to fill in english info
			}
			
			// 'if' above ensures that LocaleContextHolder.getLocale() is not null
			HerbLocaleJPA hjpa = jpa.getForSpecificLocale(LocaleContextHolder.getLocale().toString());
			if(hjpa==null) {
				hjpa = new HerbLocaleJPA();
				hjpa.setLocale(LocaleContextHolder.getLocale().toString());
				//TODO : local name must be in model for hashCode in herbLocales
				hjpa.setLocalName(model.getLocalName());
				hjpa.setHerb(jpa);
				jpa.getHerbLocales().put(LocaleContextHolder.getLocale().toString(), hjpa);
			}
			if(RestPreconditions.checkString(model.getDescription())){
				hjpa.setDescription(model.getDescription());
			}
			if(RestPreconditions.checkString(model.getGrowsAt())){
				hjpa.setGrowsAt(model.getGrowsAt());
			}
			if(RestPreconditions.checkString(model.getProperties())){
				hjpa.setProperties(model.getProperties());
			}
			if(RestPreconditions.checkString(model.getWarnings())){
				hjpa.setWarnings(model.getWarnings());
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
				model.setLocalName(hjpa.getLocalName());
				model.setDescription(hjpa.getDescription());
				model.setGrowsAt(hjpa.getGrowsAt());
				model.setProperties(hjpa.getProperties());
				model.setWhenToPick(hjpa.getWhenToPick());
				model.setWhereToBuy(hjpa.getWhereToBuy());
				model.setWarnings(hjpa.getWarnings());
			} else {
				isEnglish = true;
			}
		}
		
		if(isEnglish) {
			model.setLocalName(jpa.getEngName());
			model.setDescription(jpa.getDescription());
			model.setGrowsAt(jpa.getGrowsAt());
			model.setProperties(jpa.getProperties());
			model.setWhenToPick(jpa.getWhenToPick());
			model.setWhereToBuy(jpa.getWhereToBuy());
			model.setWarnings(jpa.getWarnings());
		}
		
		if(jpa.getLinks()!=null && !jpa.getLinks().isEmpty()){
			model.setIllnesses(new ArrayList<IllnessDTO>());
			for(LinkJPA ijpa : jpa.getLinks()){
				// this is more memory-efficient than autowiring IllnessService
				IllnessDTO imodel = new IllnessDTO();
				imodel.setId(ijpa.getIllness().getId());
				imodel.setLocalName(ijpa.getIllness().getEngName()); //TODO local(e) name
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
				RestPreconditions.checkString(model.getLocalName()) && 
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
				RestPreconditions.checkString(model.getLocalName()) ||
				RestPreconditions.checkString(model.getProperties()) ||
				RestPreconditions.checkString(model.getWarnings()) ||
				RestPreconditions.checkString(model.getWhenToPick()) ||
				RestPreconditions.checkString(model.getWhereToBuy()) ||
				(model.getIllnesses()!=null && !model.getIllnesses().isEmpty())
				;
	}
}