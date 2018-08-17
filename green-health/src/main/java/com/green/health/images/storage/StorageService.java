package com.green.health.images.storage;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import com.green.health.util.exceptions.MyRestPreconditionsException;

public interface StorageService {

	void saveImage(final MultipartFile multipartfile, Long id, boolean isUser) throws MyRestPreconditionsException;
	
	Resource readImage(final Long Id, final String name) throws MyRestPreconditionsException;
}
