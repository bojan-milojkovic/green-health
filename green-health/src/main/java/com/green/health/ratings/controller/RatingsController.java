package com.green.health.ratings.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;
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

	@RequestMapping(value = "/rating", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.OK)
	public void addNewRatings(@RequestBody @Valid RatingDTO model, Principal principal) throws MyRestPreconditionsException {
		model.setUsername(principal.getName());
		ratingsServiceImpl.addNew(model);
	}
	
	@RequestMapping(value = "/rating", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody RatingDTO getRatingForHerbAndIllness(@RequestParam("herbId") Long herbId,
												@RequestParam("illnessId") Long illnessId) throws MyRestPreconditionsException{
		
		return ratingsServiceImpl.getRatingForLink(herbId, illnessId);
	}
	
	@RequestMapping(value = "/rating/herb", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<RatingDTO> getRatingsForHerb(@RequestParam("id") Long id) throws MyRestPreconditionsException{
		return ratingsServiceImpl.getRatingsForHerbOrIllness(id, true);
	}
	
	@RequestMapping(value = "/rating/illness", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<RatingDTO> getRatingsForIllness(@RequestParam("id") Long id) throws MyRestPreconditionsException{
		return ratingsServiceImpl.getRatingsForHerbOrIllness(id, false);
	}
}