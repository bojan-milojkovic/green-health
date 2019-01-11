package com.green.health.illness.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import com.green.health.herb.dao.HerbRepository;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.illness.dao.IllnessLocaleRepository;
import com.green.health.illness.dao.IllnessRepository;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.illness.entities.IllnessJPA;
import com.green.health.illness.entities.IllnessLocaleJPA;
import com.green.health.illness.service.IllnessService;
import com.green.health.ratings.entities.LinkJPA;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class IllnessServiceImpl implements IllnessService {
	
	private IllnessRepository illnessDao;
	private IllnessLocaleRepository illnessLocaleDao;
	private HerbRepository herbDao;
	
	@Autowired
	public IllnessServiceImpl(IllnessRepository illnessDao, HerbRepository herbDao, IllnessLocaleRepository illnessLocaleDao) {
		this.illnessDao = illnessDao;
		this.herbDao = herbDao;
		this.illnessLocaleDao = illnessLocaleDao;
	}

	// get all
	@Override
	public List<IllnessDTO> getAll(){
		return illnessDao.findAll().stream().map(jpa -> convertJpaToModel(jpa)).collect(Collectors.toList());
	}

	// get one by id
	@Override
	public IllnessDTO getOneById(final Long id) throws MyRestPreconditionsException{
		checkId(id);
		IllnessJPA jpa = RestPreconditions.checkNotNull(illnessDao.getOne(id), "Find illness by id error",
				"Cannot find illness with id = "+id);
		return convertJpaToModel(jpa);
	}
	
	// get one by name :
	@Override
	public IllnessDTO getOneByLocalName(final String name) throws MyRestPreconditionsException{
		IllnessJPA jpa = null;
		
		if(RestPreconditions.checkLocaleIsEnglish()) {
			jpa = illnessDao.findByEngName(name);
		} else {
			jpa = illnessLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), name).getIllness();
		}
		if(jpa==null) {
			throw new MyRestPreconditionsException("No such illness in database","Cannot find the illness with name '"+name+"'.");
		}
		
		return convertJpaToModel(jpa);
	}
	
	@Override
	public IllnessDTO getOneByLatinName(final String name) throws MyRestPreconditionsException{
		return convertJpaToModel(RestPreconditions.checkNotNull(illnessDao.findByLatinName(name),"Cannot find the illness with latin name '"+name+"'"));
	}
	
	// add new illness
	@Override
	public void addNew(final IllnessDTO model) throws MyRestPreconditionsException{
		
		if(isPostDataPresent(model)){
			// check names :
			RestPreconditions.checkSuchEntityAlreadyExists(illnessDao.findByLatinName(model.getLatinName()),
					"Illness with Latin name "+model.getLatinName()+" is already in our database.");
			
			if(RestPreconditions.checkLocaleIsEnglish()) {
				RestPreconditions.checkSuchEntityAlreadyExists(illnessDao.findByEngName(model.getLocalName()),
						"We assert your locale as English. The herb with name "+model.getLocalName()+" is already in our database.");
			}else {
				RestPreconditions.checkSuchEntityAlreadyExists(
						// 'if' ensures that LocaleContextHolder.getLocale() is not null
						illnessLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), model.getLocalName()),
						"The herb with local name "+model.getLocalName()+" is already in our database.");
			}
			
			illnessDao.save(convertModelToJPA(model));
		} else {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("You cannot add this illness",
					"The following data is missing from the illness form");
			
			if(RestPreconditions.checkString(model.getDescription())){
				ex.getErrors().add("illness description");
			}
			
			if(RestPreconditions.checkString(model.getLatinName() )){
				ex.getErrors().add("illness Latin name");			
			}
			
			if(RestPreconditions.checkString(model.getLocalName())){
				ex.getErrors().add("illness local name");
			}
			
			if(RestPreconditions.checkString(model.getSymptoms())){
				ex.getErrors().add("symptoms of the illness");
			}
			
			throw ex;
		}
	}
	
	// edit illness
	@Override
	public IllnessDTO edit(IllnessDTO model, final Long id) throws MyRestPreconditionsException{
		checkId(id);
		
		RestPreconditions.assertTrue(isPatchDataPresent(model), "Illness edit error", 
						"Your illness edit request is invalid - You must provide some editable data.");
		model.setId(id);
		
		// check Latin name is not already taken :
		if(RestPreconditions.checkString(model.getLatinName())) {
			IllnessJPA tmp = illnessDao.findByLatinName(model.getLatinName());
			if(tmp!=null) {
				RestPreconditions.assertTrue(tmp.getId()==id, "Illness edit error", 
						"The Latin name '"+model.getLatinName()+"' has already been assigned to another illness.");
			}
		}
		
		// check local name is not already taken :
		if(model.getLocalName()!=null) {
			IllnessJPA tmp = null;
			if(RestPreconditions.checkLocaleIsEnglish()) {
				tmp = illnessDao.findByEngName(model.getLocalName());
			} else {
				tmp = illnessLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), model.getLocalName()).getIllness();
			}
			if(tmp!=null) {
				RestPreconditions.assertTrue(tmp.getId()==id, "Herb edit error", 
						"The local name '"+model.getLocalName()+"' belongs to another herb in our database.");
			}
		}
		
		return convertJpaToModel(illnessDao.save(convertModelToJPA(model)));
	}
	
	// delete illness
	//TODO : put this in ServiceParent
	@Override
	public void delete(final Long id) throws MyRestPreconditionsException{
		checkId(id);
		
		RestPreconditions.checkNotNull(illnessDao.getOne(id),"Illness delete error",
				"Illness with id = "+id+" does not exist in our database.");
		illnessDao.deleteById(id);
	}
	
	@Override
	public IllnessJPA convertModelToJPA(final IllnessDTO model) throws MyRestPreconditionsException {
		IllnessJPA jpa = null;
		
		if(model.getId()==null){
			jpa = new IllnessJPA();
		} else {
			jpa = RestPreconditions.checkNotNull(illnessDao.getOne(model.getId()),
					"illness edit error", "Illness with id = "+model.getId()+" does not exist in our database.");
		}
		
		if(RestPreconditions.checkString(model.getLatinName())){
			jpa.setLatinName(model.getLatinName());
		}
		
		if(RestPreconditions.checkLocaleIsEnglish()) {
			if(RestPreconditions.checkString(model.getDescription())){
				jpa.setDescription(model.getDescription());
			}
			if(RestPreconditions.checkString(model.getSymptoms())){
				jpa.setSymptoms(model.getSymptoms());
			}
			if(RestPreconditions.checkString(model.getLocalName())){
				jpa.setEngName(model.getLocalName());
			}
		} else {
			
			if(model.getId()==null) {
				//TODO: if it is a brand new herb, email admin to fill in english info
			}
			
			// 'if' above ensures that LocaleContextHolder.getLocale() is not null
			IllnessLocaleJPA ijpa = jpa.getForSpecificLocale(LocaleContextHolder.getLocale().toString());
			if(ijpa == null) {
				ijpa = new IllnessLocaleJPA();
				ijpa.setLocale(LocaleContextHolder.getLocale().toString());
				ijpa.setIllness(jpa);
				jpa.getIllnessLocales().add(ijpa);
			}
			if(RestPreconditions.checkString(model.getDescription())){
				ijpa.setDescription(model.getDescription());
			}
			if(RestPreconditions.checkString(model.getSymptoms())){
				ijpa.setSymptoms(model.getSymptoms());
			}
			if(RestPreconditions.checkString(model.getLocalName())){
				jpa.setEngName(model.getLocalName());
			}
		}
		
		// link herbs :
		if(model.getHerbs()!=null && !model.getHerbs().isEmpty()){

			for(HerbDTO hmodel : model.getHerbs()){
				HerbJPA hjpa = null;
				
				if(RestPreconditions.checkString(hmodel.getLatinName())){
					hjpa = herbDao.getHerbByLatinName(hmodel.getLatinName());
					
				} else if(RestPreconditions.checkString(hmodel.getLocalName())) {
					hjpa = herbDao.getHerbByEngName(hmodel.getLocalName());
				}
				
				if(hjpa!=null){
					// since 'links' is a HashSet, there will be no duplicates
					jpa.getLinks().add(new LinkJPA(hjpa, jpa));
				} else {
					throw new MyRestPreconditionsException("Link herb to illness error","Cannot find herb with srb name = "
								+hmodel.getLocalName()+" and latin name = "+hmodel.getLatinName());
				}
			}
		}
		
		return jpa;
	}
	
	@Override
	public IllnessDTO convertJpaToModel(final IllnessJPA jpa) {
		IllnessDTO model = new IllnessDTO();
		
		model.setId(jpa.getId());
		model.setLatinName(jpa.getLatinName());
		
		boolean isEnglish = RestPreconditions.checkLocaleIsEnglish();
		if(!isEnglish) {
			IllnessLocaleJPA ijpa = jpa.getForSpecificLocale(LocaleContextHolder.getLocale().toString());
			if(ijpa!=null) {
				model.setLocalName(ijpa.getLocalName());
				model.setSymptoms(ijpa.getSymptoms());
				model.setDescription(ijpa.getDescription());
			} else {
				isEnglish = true;
			}
		}
		if(isEnglish) {
			model.setDescription(jpa.getDescription());
			model.setLocalName(jpa.getEngName());
			model.setSymptoms(jpa.getSymptoms());
		}
		
		// add herbs :
		if(jpa.getLinks()!=null && !jpa.getLinks().isEmpty()){
			model.setHerbs(new ArrayList<HerbDTO>());
			for(LinkJPA ljpa : jpa.getLinks()){
				// this is more memory efficient than autowiring herbservice
				HerbDTO hmodel = new HerbDTO();
				hmodel.setId(ljpa.getHerb().getId());
				hmodel.setLatinName(ljpa.getHerb().getLatinName());
				hmodel.setLocalName(ljpa.getHerb().getEngName());  //TODO local(e) name
				model.getHerbs().add(hmodel);
			}
		}
		
		return model;
	}

	@Override
	public boolean isPostDataPresent(final IllnessDTO model) {
		return RestPreconditions.checkString(model.getDescription()) &&
				RestPreconditions.checkString(model.getLatinName()) &&
				RestPreconditions.checkString(model.getLocalName()) &&
				RestPreconditions.checkString(model.getSymptoms());
	}

	@Override
	public boolean isPatchDataPresent(final IllnessDTO model) {
		return RestPreconditions.checkString(model.getDescription()) ||
				RestPreconditions.checkString(model.getLatinName()) ||
				RestPreconditions.checkString(model.getLocalName()) ||
				RestPreconditions.checkString(model.getSymptoms()) ||
				(model.getHerbs()!=null && !model.getHerbs().isEmpty());
	}
	
}