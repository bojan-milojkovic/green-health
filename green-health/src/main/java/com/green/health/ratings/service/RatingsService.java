package com.green.health.ratings.service;

import java.util.List;
import com.green.health.ratings.entities.RatingDTO;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface RatingsService {
	
	void addNew(RatingDTO model) throws MyRestPreconditionsException;
	
	RatingDTO getRatingForLink(final Long herbId, final Long illnessId) throws MyRestPreconditionsException;
	
	List<RatingDTO> getRatingsForHerbOrIllness(final Long id, final boolean ih) throws MyRestPreconditionsException;
}