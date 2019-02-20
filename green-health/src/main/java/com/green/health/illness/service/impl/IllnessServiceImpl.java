package com.green.health.illness.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import com.green.health.herb.dao.HerbRepository;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.illness.dao.IllnessLocaleRepository;
import com.green.health.illness.dao.IllnessRepository;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.illness.entities.IllnessInterface;
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
		return IllnessService.super.getAll();
	}

	// get one by id
	@Override
	public IllnessDTO getOneById(final Long id) throws MyRestPreconditionsException{
		return IllnessService.super.getOneById(id);
	}
	
	// get one by name :
	@Override
	public IllnessDTO getOneByLocalName(final String name) throws MyRestPreconditionsException{		
		return convertJpaToModel(RestPreconditions.checkNotNull(
				(RestPreconditions.checkLocaleIsEnglish() ? 
					illnessDao.findByEngName(name) :
					illnessLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), name).getIllness())
				, "Find illness error","Cannot find illness with name '"+name+"'."));
	}
	
	@Override
	public IllnessDTO getOneByLatinName(final String name) throws MyRestPreconditionsException{
		return convertJpaToModel(RestPreconditions.checkNotNull(illnessDao.findByLatinName(name),
				"Cannot find the illness with latin name '"+name+"'"));
	}
	
	// add new illness
	@Override
	public void addNew(final IllnessDTO model) throws MyRestPreconditionsException{
		// basic checks
		IllnessService.super.addNew(model);
		
		// check names :
		RestPreconditions.checkSuchEntityAlreadyExists(illnessDao.findByLatinName(model.getLatinName()),
				"Illness with Latin name "+model.getLatinName()+" is already in our database.");
		
		if(RestPreconditions.checkLocaleIsEnglish()) {
			RestPreconditions.checkSuchEntityAlreadyExists(illnessDao.findByEngName(model.getLocalName()),
					"We assert your locale as English. The herb with eng. name "+model.getLocalName()+" is already in our database.");
		}else {
			RestPreconditions.checkSuchEntityAlreadyExists(
					// 'if' ensures that LocaleContextHolder.getLocale() is not null
					illnessLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), model.getLocalName()),
					"The herb with local name "+model.getLocalName()+" is already in our database.");
		}
		
		illnessDao.save(convertModelToJPA(model));
	}
	
	// edit illness
	@Override
	public IllnessDTO edit(IllnessDTO model, final Long id) throws MyRestPreconditionsException{
		// basic checks
		model = IllnessService.super.edit(model, id);
		
		// check Latin name is not already taken :
		if(RestPreconditions.checkString(model.getLatinName())) {
			IllnessJPA tmp = illnessDao.findByLatinName(model.getLatinName());
			if(tmp!=null) {
				RestPreconditions.assertTrue(tmp.getId()==id, "Illness edit error", 
						"The Latin name '"+model.getLatinName()+"' has already been assigned to another illness.");
			}
		}
		
		// check local name is not already taken :
		if(RestPreconditions.checkString(model.getLocalName())) {
			IllnessJPA tmp = null;
			if(RestPreconditions.checkLocaleIsEnglish()) {
				tmp = illnessDao.findByEngName(model.getLocalName());
			} else {
				tmp = illnessLocaleDao.findWhereLocaleAndLocalName(LocaleContextHolder.getLocale().toString(), model.getLocalName()).getIllness();
			}
			if(tmp!=null) {
				RestPreconditions.assertTrue(tmp.getId()==id, "Illness edit error", 
						RestPreconditions.assertLocaleInString()+ "The name '"+model.getLocalName()+"' belongs to another illness in our database.");
			}
		}
		
		return convertJpaToModel(illnessDao.save(convertModelToJPA(model)));
	}
	
	private void useSettersInConvertToJPA(IllnessInterface model, IllnessInterface jpa){
		if(RestPreconditions.checkString(model.getDescription())){
			jpa.setDescription(model.getDescription());
		}
		if(RestPreconditions.checkString(model.getSymptoms())){
			jpa.setSymptoms(model.getSymptoms());
		}
		if(RestPreconditions.checkString(model.getLocalName())){
			jpa.setLocalName(model.getLocalName());
		}
		if(RestPreconditions.checkString(model.getCause())) {
			jpa.setCause(model.getCause());
		}
		if(RestPreconditions.checkString(model.getTreatment())) {
			jpa.setTreatment(model.getTreatment());
		}
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
			useSettersInConvertToJPA(model, jpa);
		} else {
			
			// 'if' above ensures that LocaleContextHolder.getLocale() is not null
			IllnessLocaleJPA ijpa = jpa.getForSpecificLocale(LocaleContextHolder.getLocale().toString());
			if(ijpa == null) {
				ijpa = new IllnessLocaleJPA();
				if(jpa.getId()==null){
					// illness needs to have id before we put IllnessLocale into its set
					jpa = illnessDao.save(jpa);
					//TODO: if it is a brand new herb, email admin to fill in english info
				}
				
				ijpa.setLocale(LocaleContextHolder.getLocale().toString());
				ijpa.setIllness(jpa);
				jpa.getIllnessLocales().add(ijpa);
			}
			
			useSettersInConvertToJPA(model, ijpa);
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
	
	private void useSettersInConvertToModel(IllnessInterface model, IllnessInterface jpa){
		model.setLocalName(jpa.getLocalName());
		model.setSymptoms(jpa.getSymptoms());
		model.setDescription(jpa.getDescription());
		model.setCause(jpa.getCause());
		model.setTreatment(jpa.getTreatment());
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
				useSettersInConvertToModel(model, ijpa);
			} else {
				isEnglish = true;
			}
		}
		if(isEnglish) {
			useSettersInConvertToModel(model, jpa);
		}
		
		// add herbs :
		if(jpa.getLinks()!=null && !jpa.getLinks().isEmpty()){
			model.setHerbs(new ArrayList<HerbDTO>());
			for(LinkJPA ljpa : jpa.getLinks()){
				// this is more memory efficient than autowiring herbservice
				HerbDTO hmodel = new HerbDTO();
				hmodel.setId(ljpa.getHerb().getId());
				hmodel.setLatinName(ljpa.getHerb().getLatinName());
				if(RestPreconditions.checkLocaleIsEnglish()){
					hmodel.setLocalName(ljpa.getHerb().getEngName());
				} else{
					hmodel.setLocalName(ljpa.getHerb().getNameForSpecificLocale(LocaleContextHolder.getLocale().toString()));
				}
				model.getHerbs().add(hmodel);
			}
		}
		
		return model;
	}

	@Override
	public void isPostDataPresent(final IllnessDTO model) throws MyRestPreconditionsException {
		MyRestPreconditionsException ex = new MyRestPreconditionsException("You cannot add this illness",
				"The following data is missing from the illness form");
		
		if(!RestPreconditions.checkString(model.getDescription())){
			ex.getErrors().add("illness description");
		}
		if(!RestPreconditions.checkString(model.getLatinName() )){
			ex.getErrors().add("illness Latin name");			
		}
		if(!RestPreconditions.checkString(model.getLocalName())){
			ex.getErrors().add("illness local name");
		}
		if(!RestPreconditions.checkString(model.getSymptoms())){
			ex.getErrors().add("symptoms of the illness");
		}
		if(!RestPreconditions.checkString(model.getCause())){
			ex.getErrors().add("cause of the illness");
		}
		if(!RestPreconditions.checkString(model.getTreatment())){
			ex.getErrors().add("treatment for the illness");
		}
		if(!ex.getErrors().isEmpty()) {
			throw ex;
		}
	}

	@Override
	public boolean isPatchDataPresent(final IllnessDTO model) {
		return RestPreconditions.checkString(model.getDescription()) ||
				RestPreconditions.checkString(model.getLatinName()) ||
				RestPreconditions.checkString(model.getLocalName()) ||
				RestPreconditions.checkString(model.getSymptoms()) ||
				RestPreconditions.checkString(model.getCause()) ||
				RestPreconditions.checkString(model.getTreatment()) ||
				(model.getHerbs()!=null && !model.getHerbs().isEmpty());
	}

	@Override
	public JpaRepository<IllnessJPA, Long> getRepository() {
		return illnessDao;
	}
	
	@Override
	public String getName(){
		return "illness";
	}
}