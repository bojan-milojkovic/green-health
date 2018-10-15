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

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {
	
	@Mock
	private ResourceLoader resourceLoader;
	
	@InjectMocks
	private StorageServiceImpl mockStorageServiceImpl;

	@Test
	public void deleteImageButDirPathInvalid(){
		try {
			mockStorageServiceImpl.deleteImage(-1L, true);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("Id used for directory structure traverse is invalid", e.getDetails());
		}
	}
	
	
}