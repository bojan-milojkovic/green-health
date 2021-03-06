package com.green.health.user.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.green.health.parents.ServiceParent;
import com.green.health.user.entities.UserDTO;
import com.green.health.user.entities.UserJPA;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface UserService extends ServiceParent<UserJPA, UserDTO>{
	
	UserDTO getUserByUsernameOrEmail(final String username, final String email, final String phone) throws MyRestPreconditionsException;
	
	void changePassword(UserDTO model, String username) throws MyRestPreconditionsException;
	
	void saveProfilePicture(MultipartFile file, final String username) throws MyRestPreconditionsException;
	
	ResponseEntity<Resource> getProfilePictureThumb(final Long id, final String name) 
			throws MyRestPreconditionsException;
	
	void setCurrentUsername(final String cun);
	
	String activateUser(final String key) throws MyRestPreconditionsException;
}