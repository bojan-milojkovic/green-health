package com.green.health.images.storage.controller;

import java.security.Principal;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;
import com.green.health.images.storage.StorageService;
import com.green.health.user.entities.UserDTO;
import com.green.health.user.service.UserService;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Controller
@MultipartConfig(maxFileSize = 3*1024*1024, maxRequestSize = 4*1024*1024)
public class ImageTestController implements ServletContextAware {

	private StorageService storageServiceImpl;
	
	private UserService userServiceImpl;

	private ServletContext servletContext;

	@Autowired
	public ImageTestController(StorageService storageServiceImpl, UserService userServiceImpl,
			ServletContext servletContext) {
		this.storageServiceImpl = storageServiceImpl;
		this.userServiceImpl = userServiceImpl;
		this.servletContext = servletContext;
	}

	
	@RequestMapping(value = "/image/test", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public void saveImage(@RequestParam(value="file", required=true) MultipartFile file, 
						   Principal principal) throws MyRestPreconditionsException{
		UserDTO model = userServiceImpl.getUserByUsernameOrEmail(principal.getName(), null);
		
		storageServiceImpl.saveImage(file, model.getId(), true);
	}
	
	@RequestMapping(value = "/image/test/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody ResponseEntity<Resource> getImageAsResource(@PathVariable("id") final Long id) throws MyRestPreconditionsException {
	    HttpHeaders headers = new HttpHeaders();
	    Resource resource = new ServletContextResource(servletContext, storageServiceImpl.readImage(id, "profile_THUMBNAIL.png"));
	    return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
