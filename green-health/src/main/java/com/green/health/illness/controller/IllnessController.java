package com.green.health.illness.controller;

import java.util.List;
import javax.validation.Valid;
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

	@Autowired
	public IllnessController(IllnessService illnessServiceImpl) {
		this.illnessServiceImpl = illnessServiceImpl;
	}
	
	// .../gh/illness
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<IllnessDTO> getAllIllnesses(){
		return illnessServiceImpl.getAll();
	}
	
	// .../gh/illness/id
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody IllnessDTO getIllnessById(@PathVariable("id") final Long id){
		return illnessServiceImpl.getOneById(id);
	}
	
	// .../gh/illness?name=asma
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody IllnessDTO getIllnessByName(@RequestParam(value="name", required=true) final String name){
		return illnessServiceImpl.getOneByName(name);
	}
	
	// .../gh/illness
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.CREATED)
	public void addIllness(@RequestBody @Valid IllnessDTO model) throws MyRestPreconditionsException {
		if(model!=null){
			illnessServiceImpl.addNew(model);
		} else {
			throw new MyRestPreconditionsException("Illness creation error",
					"You are sending a request without the object");
		}
	}
	
	// .../gh/illness/3
	@RequestMapping(value="/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody IllnessDTO editIllness(@RequestBody @Valid IllnessDTO model, @PathVariable("id") Long id) throws MyRestPreconditionsException{
		if(model!=null){
			return illnessServiceImpl.edit(model, id);
		} else {
			throw new MyRestPreconditionsException("Illness edit error",
					"You are sending a request without the object");
		}
	}
	
	// .../gh/illness/3
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteIllness(@PathVariable("id") final Long id) throws MyRestPreconditionsException {
		illnessServiceImpl.delete(id);
	}
}
