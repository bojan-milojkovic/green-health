package com.green.health;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ResourceLoader;
import com.green.health.images.storage.impl.StorageServiceImpl;
import com.green.health.util.exceptions.MyRestPreconditionsException;
import com.green.health.images.storage.StorageService.ImgType;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {
	
	@Mock
	private ResourceLoader resourceLoader;
	
	@InjectMocks
	private StorageServiceImpl mockStorageServiceImpl;

	@Test
	public void deleteImageButDirPathInvalid(){
		try {
			mockStorageServiceImpl.deleteImage(-1L, ImgType.profile);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("No directory exists for that id.", e.getDetails());
		}
	}
	
	
}