package com.green.health.store.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.health.store.entities.StoreDTO;
import com.green.health.store.service.StoreService;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Controller
@RequestMapping(value="/store")
public class StoreController {
	
	@Autowired
	private StoreService storeServiceImpl;

	private static final Logger logger = LoggerFactory.getLogger(StoreController.class);
	
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<StoreDTO> getAllStores(){
		return storeServiceImpl.getAll();
	}
	
	@RequestMapping(value = "/my", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<StoreDTO> getMyStores(Principal principal) throws MyRestPreconditionsException {
		logger.debug("User " + principal.getName() + " getting his store");
		return storeServiceImpl.getMyStores(principal.getName());
	}
	
	@RequestMapping(value = "/for", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<StoreDTO> getStoreForUser(@RequestParam(value="username") final String username, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User " + principal.getName() + " getting the store of user " + username);
		return storeServiceImpl.getMyStores(username);
	}
	
	@RequestMapping(value = "/bypar", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<StoreDTO> getStoresByParams(@RequestBody @Valid final StoreDTO model, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User " + principal.getName() + " getting stores by parameters");
		return storeServiceImpl.getStoresByProperties(model);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers="Content-Type!=multipart/form-data")
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.CREATED)
	public void addNewStore(@RequestBody @Valid StoreDTO model, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User " + principal.getName() + " adding new store");
		model.setUsername(principal.getName());
		storeServiceImpl.addNew(model);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, headers="Content-Type=multipart/form-data")
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.CREATED)
	public void addStoreWithImage (@RequestParam(value="json", required=true) final String json,
								   @RequestParam(value="file", required=true) final MultipartFile file, 
								   Principal principal) throws MyRestPreconditionsException {
		try {
			logger.debug("User " + principal.getName() + " adding new store with image");
			StoreDTO model = (new ObjectMapper()).readValue(json, StoreDTO.class);
			model.setImage(file);
			storeServiceImpl.addNew(model);
		} catch (IOException e) {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("Add store error","error transforming a json string into an object");
			ex.getErrors().add(e.getMessage());
			ex.getErrors().add(e.getLocalizedMessage());
			e.printStackTrace();
			throw ex;
		}
	}
	
	@RequestMapping(value = "/{sid}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody StoreDTO editStore(
			@RequestBody @Valid StoreDTO model, 
			@PathVariable("sid") final Long id, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User " + principal.getName() + " editing the store with id = "+id);
		model.setUsername(principal.getName());
		return storeServiceImpl.edit(model, id);
	}
	
	@RequestMapping(value = "/{sid}", method = RequestMethod.PATCH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, headers="Content-Type=multipart/form-data")
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody StoreDTO editStoreWithImage(
			   @PathVariable("sid") final Long id,
			   @RequestParam(value="json", required=true) final String json,
			   @RequestParam(value="file", required=true) final MultipartFile file, 
			   Principal principal) throws MyRestPreconditionsException {
		try {
			logger.debug("User " + principal.getName() + " editing store with image");
			StoreDTO model = (new ObjectMapper()).readValue(json, StoreDTO.class);
			model.setImage(file);
			return storeServiceImpl.edit(model, id);
		} catch (IOException e) {
			MyRestPreconditionsException ex = new MyRestPreconditionsException("Edit store with image error","error transforming a json string into an object");
			ex.getErrors().add(e.getMessage());
			ex.getErrors().add(e.getLocalizedMessage());
			e.printStackTrace();
			throw ex;
		}
	}
	
	@RequestMapping(value = "/{sid}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.OK)
	public void deleteStore(@PathVariable("sid") final Long id, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User " + principal.getName() + " deleting their store");
		storeServiceImpl.deleteStore(id, principal.getName());
	}
}