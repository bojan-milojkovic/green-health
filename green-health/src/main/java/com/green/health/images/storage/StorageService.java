package com.green.health.images.storage;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface StorageService {

	void saveImage(final MultipartFile multipartfile, final Long id, final boolean isUser) throws MyRestPreconditionsException;
	
	void deleteImage(final Long id, final boolean isUser) throws MyRestPreconditionsException;
	
	ResponseEntity<Resource> getImage(final Long id, final String name) throws MyRestPreconditionsException;
}
