package com.green.health.illness.service;

import com.green.health.illness.entities.IllnessDTO;
import com.green.health.illness.entities.IllnessJPA;
import com.green.health.parents.ServiceParent;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface IllnessService  extends ServiceParent<IllnessJPA, IllnessDTO>{

	IllnessDTO getOneByLocalName(String name) throws MyRestPreconditionsException;
	
	IllnessDTO getOneByLatinName(String name) throws MyRestPreconditionsException;
}