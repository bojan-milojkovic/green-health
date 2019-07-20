package com.green.health.ratings.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.green.health.ratings.entities.RatingDTO;
import com.green.health.ratings.service.RatingsService;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@RestController
public class RatingsController {
	
	@Autowired
	private RatingsService ratingsServiceImpl;
	
	private static final Logger logger = LoggerFactory.getLogger(RatingsController.class);

	@RequestMapping(value = "/rating", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.OK)
	public void addNewRatings(@RequestBody @Valid RatingDTO model, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" adding a new rating "+model.getNewRatings()+" for (herb,illness)=("+model.getHerbId()+","+model.getIllnessId()+")");
		model.setUsername(principal.getName());
		ratingsServiceImpl.addNew(model);
		logger.debug("User "+principal.getName()+" ratings added successfully");
	}
	
	@RequestMapping(value = "/rating", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RatingDTO getRatingForHerbAndIllness(@RequestParam("herbId") Long herbId,
												@RequestParam("illnessId") Long illnessId, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" getting ratings for link (herb,illness)=("+herbId+","+illnessId+")");
		return ratingsServiceImpl.getRatingForLink(herbId, illnessId);
	}
	
	@RequestMapping(value = "/rating/herb", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<RatingDTO> getRatingsForHerb(@RequestParam("id") Long id, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" getting ratings for link where herbId = "+id);
		return ratingsServiceImpl.getRatingsForHerbOrIllness(id, true);
	}
	
	@RequestMapping(value = "/rating/illness", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<RatingDTO> getRatingsForIllness(@RequestParam("id") Long id, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" getting ratings for link where herbId = "+id);
		return ratingsServiceImpl.getRatingsForHerbOrIllness(id, false);
	}
}