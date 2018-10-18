package com.green.health.ratings.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.green.health.ratings.entities.RatingsDTO;
import com.green.health.ratings.service.RatingsService;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@RestController
public class RatingsController {

	private RatingsService ratingsServiceImpl;

	@Autowired
	public RatingsController(RatingsService ratingsServiceImpl) {
		this.ratingsServiceImpl = ratingsServiceImpl;
	}
	
	@RequestMapping(value = "/ratings", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.OK)
	public void addNewRatings(@RequestBody @Valid RatingsDTO model) throws MyRestPreconditionsException {
		ratingsServiceImpl.addNew(model);
	}
}