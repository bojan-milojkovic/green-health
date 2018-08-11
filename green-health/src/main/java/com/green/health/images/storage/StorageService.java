package com.green.health.images.storage;

import org.springframework.web.multipart.MultipartFile;

import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface StorageService {

	void saveImage(final MultipartFile multipartfile, Long id, boolean isUser) throws MyRestPreconditionsException;
	
	String readImage(final Long Id, final String name) throws MyRestPreconditionsException;
}
