package com.green.health.illness.controller;

import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.illness.service.IllnessService;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Controller
@RequestMapping(value="/illness")
public class IllnessController {

	private IllnessService illnessServiceImpl;
	
	private static final Logger logger = LoggerFactory.getLogger(IllnessController.class);

	@Autowired
	public IllnessController(IllnessService illnessServiceImpl) {
		this.illnessServiceImpl = illnessServiceImpl;
	}
	
	// .../gh/illness
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<IllnessDTO> getAllIllnesses(Principal principal){
		logger.debug("User "+principal.getName()+" getting all illnesses.");
		return illnessServiceImpl.getAll();
	}
	
	// .../gh/illness/id
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody IllnessDTO getIllnessById(@PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" getting illness by id = "+id);
		return illnessServiceImpl.getOneById(id);
	}
	
	// .../gh/illness?name=asma
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody IllnessDTO getIllnessByName(@RequestParam(value="name", required=true) final String name, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" getting illness by name "+name);
		try {
			return illnessServiceImpl.getOneByLatinName(name);
		} catch(Exception e) {
			return illnessServiceImpl.getOneByLocalName(name);
		}
	}
	
	// .../gh/illness
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.CREATED)
	public void addIllness(@RequestBody @Valid IllnessDTO model, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" adding new illness to database - "+model.getLatinName());
		illnessServiceImpl.addNew(model);
		logger.debug("User "+principal.getName()+" successfully added new illness to database");
	}
	
	// .../gh/illness/3
	@RequestMapping(value="/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody IllnessDTO editIllness(@RequestBody @Valid IllnessDTO model, @PathVariable("id") Long id, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User "+principal.getName()+" editing illness = "+id);
		model = illnessServiceImpl.edit(model, id);
		logger.debug("User "+principal.getName()+" edit illness successful");
		return model;
	}
	
	// .../gh/illness/3
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteIllness(@PathVariable("id") final Long id, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User "+principal.getName()+" deleting illness = "+id);
		illnessServiceImpl.delete(id);
		logger.debug("User "+principal.getName()+" deleted illness successful.");
	}
}
