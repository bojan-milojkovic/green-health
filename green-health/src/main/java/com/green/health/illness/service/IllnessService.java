package com.green.health.illness.service;

import com.green.health.illness.entities.IllnessDTO;
import com.green.health.illness.entities.IllnessJPA;
import com.green.health.parents.ServiceParent;

public interface IllnessService  extends ServiceParent<IllnessJPA, IllnessDTO>{

	IllnessDTO getOneByName(final String name);
	
}