package com.green.health.store.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.green.health.store.entities.StoreDTO;
import com.green.health.store.service.StoreService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Controller
@RequestMapping(value="/store")
public class StoreController {
	
	@Autowired
	private StoreService storeServiceImpl;

	private static final Logger logger = LoggerFactory.getLogger(StoreController.class);
	
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<StoreDTO> getAllStores(){
		return storeServiceImpl.getAll();
	}
	
	@RequestMapping(value = "/my", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<StoreDTO> getMyStore(Principal principal) throws MyRestPreconditionsException {
		logger.debug("User " + principal.getName() + " getting his store");
		return storeServiceImpl.getMyStore(principal.getName());
	}
	
	@RequestMapping(value = "/for", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<StoreDTO> getStoreForUser(@RequestParam(value="username") final String username, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User " + principal.getName() + " getting the store of user " + username);
		return storeServiceImpl.getMyStore(username);
	}
	
	@RequestMapping(value = "/bypar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<StoreDTO> getStoresByParams(@RequestBody @Valid final StoreDTO model, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User " + principal.getName() + " getting stores by parameters");
		return storeServiceImpl.getStoresByProperties(model);
	}
	
	@RequestMapping(value = "/ad", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.CREATED)
	public void addNewStore(@RequestBody @Valid final StoreDTO model, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User " + principal.getName() + " adding new store");
		model.setUsername(principal.getName());
		storeServiceImpl.addNew(model);
	}
	
	@RequestMapping(value = "/ed", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public @ResponseBody StoreDTO editStore(@RequestBody @Valid final StoreDTO model, final Long id, Principal principal) throws MyRestPreconditionsException{
		logger.debug("User " + principal.getName() + " editing their store");
		model.setUsername(principal.getName());
		return storeServiceImpl.edit(model, id);
	}
	
	@RequestMapping(value = "/del", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_HERBALIST')")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteStore(final Long id, Principal principal) throws MyRestPreconditionsException {
		logger.debug("User " + principal.getName() + " deleting their store");
		RestPreconditions.assertTrue(storeServiceImpl.checkUserOwnsTheStore(principal.getName(), id),
				"Store delete error","You cannot delete someone else's store.");
		storeServiceImpl.delete(id);
	}
}