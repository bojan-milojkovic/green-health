package com.green.health.herb.service;

import java.util.List;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.parents.ServiceParent;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface HerbService extends ServiceParent<HerbJPA, HerbDTO>{
	
	List<HerbDTO> getAllHerbs();
	
	HerbDTO getHerbById(Long id);
	
	HerbDTO getHerbBySrbName(String name);
	
	HerbDTO getHerbByLatinName(String latinName);

	void addHerb(HerbDTO model) throws MyRestPreconditionsException;
	
	HerbDTO editHerb(Long id, HerbDTO model) throws MyRestPreconditionsException;
	
	void deleteHerb(Long id) throws MyRestPreconditionsException ;
}
