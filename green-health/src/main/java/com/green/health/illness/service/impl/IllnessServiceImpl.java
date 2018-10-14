package com.green.health.illness.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.green.health.herb.dao.HerbRepository;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.illness.dao.IllnessRepository;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.illness.entities.IllnessJPA;
import com.green.health.illness.service.IllnessService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class IllnessServiceImpl implements IllnessService {
	
	private IllnessRepository illnessDao;
	private HerbRepository herbDao;
	
	@Autowired
	public IllnessServiceImpl(IllnessRepository illnessDao, HerbRepository herbDao) {
		this.illnessDao = illnessDao;
		this.herbDao = herbDao;
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
	public IllnessDTO getOneByName(final String name) throws MyRestPreconditionsException{
		IllnessJPA jpa = illnessDao.findByLatinName(name);
		
		if(jpa == null){
			jpa = RestPreconditions.checkNotNull(illnessDao.findBySrbName(name), "Find illness by name error",
					"The the illness with the name "+name+" is not in our database.");
		}
		
		return convertJpaToModel(jpa);
	}
	
	// add new illness
	@Override
	public void addNew(final IllnessDTO model) throws MyRestPreconditionsException{
		
		if(isPostDataPresent(model)){
			// check names :
			RestPreconditions.checkSuchEntityAlreadyExists(illnessDao.findByLatinName(model.getLatinName()),
					"Illness with Latin name "+model.getLatinName()+" is already in our database.");
			RestPreconditions.checkSuchEntityAlreadyExists(illnessDao.findBySrbName(model.getSrbName()),
					"Illness with Serbian name "+model.getSrbName()+" is already in our database.");
			
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
			
			if(RestPreconditions.checkString(model.getSrbName())){
				ex.getErrors().add("illness Serbian name");
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
						"To edit the illness you must provide some editable data.");
		model.setId(id);
		
		IllnessJPA jpa = RestPreconditions.checkNotNull(convertModelToJPA(model), "Illness edit error", 
														"Illness with id = "+id+" does not exist in our database.");
		illnessDao.save(jpa);
		return convertJpaToModel(jpa);
	}
	
	// delete illness
	@Override
	public void delete(final Long id) throws MyRestPreconditionsException{
		checkId(id);
		
		IllnessJPA jpa = RestPreconditions.checkNotNull(illnessDao.getOne(id),"Illness delete error","Illness with id = "+id+" does not exist in our database.");

		illnessDao.delete(jpa);
	}
	
	@Override
	public IllnessJPA convertModelToJPA(final IllnessDTO model) {
		IllnessJPA jpa = null;
		
		if(model.getId()==null){
			jpa = new IllnessJPA();
			
			jpa.setDescription(model.getDescription());
			jpa.setLatinName(model.getLatinName());
			jpa.setSrbName(model.getSrbName());
			jpa.setSymptoms(model.getSymptoms());
		} else {
			jpa = illnessDao.getOne(model.getId());
			if(jpa == null){
				return null;
			}
			
			if(RestPreconditions.checkString(model.getDescription())){
				jpa.setDescription(model.getDescription());
			}
			if(RestPreconditions.checkString(model.getLatinName())){
				jpa.setLatinName(model.getLatinName());
			}
			if(RestPreconditions.checkString(model.getSrbName())){
				jpa.setSrbName(model.getSrbName());
			}
			if(RestPreconditions.checkString(model.getSymptoms())){
				jpa.setSymptoms(model.getSymptoms());
			}
		}
		
		// add herbs :
		if(model.getHerbs()!=null && !model.getHerbs().isEmpty()){
			/*if(jpa.getHerbs()==null){
				jpa.setHerbs(new ArrayList<HerbJPA>());
			}*/
			for(HerbDTO hmodel : model.getHerbs()){
				HerbJPA hjpa = null;
				
				if(RestPreconditions.checkString(hmodel.getLatinName())){
					hjpa = herbDao.getHerbByLatinName(hmodel.getLatinName());
					
				} else if(RestPreconditions.checkString(hmodel.getSrbName())) {
					hjpa = herbDao.getHerbBySrbName(hmodel.getSrbName());
				}
				
				if(hjpa!=null){
					jpa.getHerbs().add(hjpa);
				}
			}
		}
		
		return jpa;
	}

	@Override
	public IllnessDTO convertJpaToModel(final IllnessJPA jpa) {
		IllnessDTO model = new IllnessDTO();
		
		model.setId(jpa.getId());
		model.setDescription(jpa.getDescription());
		model.setLatinName(jpa.getLatinName());
		model.setSrbName(jpa.getSrbName());
		model.setSymptoms(jpa.getSymptoms());
		
		// add herbs :
		if(jpa.getHerbs()!=null && !jpa.getHerbs().isEmpty()){
			model.setHerbs(new ArrayList<HerbDTO>());
			for(HerbJPA hjpa : jpa.getHerbs()){
				HerbDTO hmodel = new HerbDTO();
				hmodel.setId(hjpa.getId());
				hmodel.setLatinName(hjpa.getLatinName());
				hmodel.setSrbName(hjpa.getSrbName());
				model.getHerbs().add(hmodel);
			}
		}
		
		return model;
	}

	@Override
	public boolean isPostDataPresent(final IllnessDTO model) {
		return RestPreconditions.checkString(model.getDescription()) &&
				RestPreconditions.checkString(model.getLatinName()) &&
				RestPreconditions.checkString(model.getSrbName()) &&
				RestPreconditions.checkString(model.getSymptoms());
	}

	@Override
	public boolean isPatchDataPresent(final IllnessDTO model) {
		return RestPreconditions.checkString(model.getDescription()) ||
				RestPreconditions.checkString(model.getLatinName()) ||
				RestPreconditions.checkString(model.getSrbName()) ||
				RestPreconditions.checkString(model.getSymptoms()) ||
				(model.getHerbs()!=null && !model.getHerbs().isEmpty());
	}
	
}