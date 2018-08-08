package com.green.health.herb.service;

import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.parents.ServiceParent;

public interface HerbService extends ServiceParent<HerbJPA, HerbDTO>{
	
	HerbDTO getHerbBySrbName(String name);
	
	HerbDTO getHerbByLatinName(String latinName);
}
