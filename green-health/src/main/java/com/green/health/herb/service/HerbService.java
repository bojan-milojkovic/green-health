package com.green.health.herb.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.parents.ServiceParent;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface HerbService extends ServiceParent<HerbJPA, HerbDTO>{
	
	HerbDTO getHerbByLocalName(String name) throws MyRestPreconditionsException;
	
	HerbDTO getHerbByLatinName(String latinName) throws MyRestPreconditionsException;
	
	ResponseEntity<Resource> getHerbImage(final Long id, final String name) 
			throws MyRestPreconditionsException;
	
	HerbDTO getMiniHerb(final Long id) throws MyRestPreconditionsException;
	
	List<HerbDTO> getListOfMiniHerbs(final String ids) throws MyRestPreconditionsException;
}
