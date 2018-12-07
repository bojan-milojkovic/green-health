package com.green.health.herb.service;

import org.springframework.core.io.Resource;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.parents.ServiceParent;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface HerbService extends ServiceParent<HerbJPA, HerbDTO>{
	
	HerbDTO getHerbByEngName(String name);
	
	Resource getImage(Long id, boolean isThumbnail) throws MyRestPreconditionsException;
	
	HerbDTO getHerbByLatinName(String latinName);
}
