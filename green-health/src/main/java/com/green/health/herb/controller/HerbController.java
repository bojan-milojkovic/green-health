package com.green.health.herb.controller;

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
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.service.HerbService;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Controller
@RequestMapping(value="/herb")
public class HerbController {
	
	private HerbService herbServiceImpl;
	
	@Autowired
	public HerbController(HerbService herbServiceImpl) {
		this.herbServiceImpl = herbServiceImpl;
	}

	// .../gh/herb
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<HerbDTO> getAllHerbs(){
		return herbServiceImpl.getAllHerbs();
	}
	
	// .../gh/herb/id
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody HerbDTO getHerbById(@PathVariable("id") Long id){
		return herbServiceImpl.getHerbById(id);
	}
	
	// .../gh/herb?name=maticnjak
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody HerbDTO getHerbByName(@RequestParam(value="name", required=true) String name){
		HerbDTO model = herbServiceImpl.getHerbByLatinName(name);
		return model==null ? herbServiceImpl.getHerbBySrbName(name) : model;
	}
	
	// .../gh/herb
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.CREATED)
	public void addHerb(@RequestBody @Valid HerbDTO model) throws MyRestPreconditionsException {
		herbServiceImpl.addHerb(model);
	}
	
	// .../gh/herb/3
	@RequestMapping(value="/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody HerbDTO editHerb(@RequestBody @Valid HerbDTO model, @PathVariable("id") Long id) throws MyRestPreconditionsException {
		return herbServiceImpl.editHerb(id, model);
	}
	
	// .../gh/herb/3
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteHerb(@PathVariable("id") final Long id) throws MyRestPreconditionsException {
		herbServiceImpl.deleteHerb(id);
	}
}