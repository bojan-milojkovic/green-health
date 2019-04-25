package com.green.health.herb.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.parents.ServiceParent;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface HerbService extends ServiceParent<HerbJPA, HerbDTO>{
	
	HerbDTO getHerbByLocalName(String name) throws MyRestPreconditionsException;
	
	HerbDTO getHerbByLatinName(String latinName) throws MyRestPreconditionsException;
	
	ResponseEntity<Resource> getHerbImage(final Long id, final String name, final HttpServletRequest request) 
			throws MyRestPreconditionsException;
}
