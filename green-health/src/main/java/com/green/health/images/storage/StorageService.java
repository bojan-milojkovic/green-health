package com.green.health.images.storage;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface StorageService {

	void saveImage(final MultipartFile multipartfile, Long id, boolean isUser) throws MyRestPreconditionsException;
	
	void deleteImage(final Long id, boolean isUser) throws MyRestPreconditionsException;
	
	ResponseEntity<Resource> getImage(Long id, String name, HttpServletRequest request) throws MyRestPreconditionsException;
}
