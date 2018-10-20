package com.green.health.ratings.service;

import java.util.List;
import com.green.health.ratings.entities.RatingDTO;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface RatingsService {
	
	void addNew(RatingDTO model) throws MyRestPreconditionsException;
	
	RatingDTO getRatingForLink(Long herbId, Long illnessId) throws MyRestPreconditionsException;
	
	List<RatingDTO> getRatingsForHerbOrIllness(Long id, boolean ih) throws MyRestPreconditionsException;
}