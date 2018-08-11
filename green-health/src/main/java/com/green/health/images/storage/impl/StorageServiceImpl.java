package com.green.health.images.storage.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.green.health.images.storage.StorageService;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class StorageServiceImpl implements StorageService {

	@Override
	public void saveImage(final MultipartFile mpf, Long id, boolean isUser) throws MyRestPreconditionsException {
		// compose dir structure :
		String dir = buildDirPath(id);
		
		// check resulting dir :
		checkResultingDir(dir);
		
		// put files in dir :
		saveFileInDir(dir, mpf, isUser);
	}
	
	@Override
	public String readImage(final Long id, final String name) throws MyRestPreconditionsException{
		
		String imgDir = buildDirPath(id);
		
		if(!(new File(imgDir)).isDirectory()){
			throw new MyRestPreconditionsException("Image retreave error", "image directory is invalid");
		}
		
		return imgDir + name;
		
	}
	
	// save file :
	private void saveFileInDir(String dir, MultipartFile mpf, boolean isUser) throws MyRestPreconditionsException{
		if (null != mpf && !mpf.isEmpty()) {
			if(mpf.getSize() > 3 * 1024 * 1024){
				throw new MyRestPreconditionsException("Image save error", "Max upload file size is 3MB");
			}
			
			String fileName = isUser ? "profile" : "herb";
			String contentType = mpf.getContentType().split("/")[1];
			
			try {
				// save original :
				if(!isUser){ // only if it's herb. users only have thumbnails :
					try (FileOutputStream fos = new FileOutputStream(dir + fileName + "." + contentType, false)) {
					   fos.write(scaleImageInputstream(mpf, contentType, 800, 800));
					}
				}
				
				// save thumbnail :
				try (FileOutputStream fos = new FileOutputStream(dir + fileName + "_THUMBNAIL" + "." + contentType, false)) {
				   fos.write(scaleImageInputstream(mpf, contentType, 100, 120));
				}
			}catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				throw new MyRestPreconditionsException("Image save error", "Failed to save image in "+dir);
			}
			
		} else {
			throw new MyRestPreconditionsException("Image save error", "File you are inputing is empty or null");
		}
	}
	
	private byte[] scaleImageInputstream(final MultipartFile multipart,final String contentType,final int targetWidth,final int targetHeight) throws MyRestPreconditionsException {
		String imageFormat = (contentType.contains("jpeg")) ? "jpg" : "png";
        byte[] scaledImageInByte = null;
		try{
			
	        BufferedImage imBuff = ImageIO.read(multipart.getInputStream());
	        
	        BufferedImage scaledImg = Scalr.resize(imBuff, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH,
	                targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(scaledImg,imageFormat, baos);
	        baos.flush();
	        
	        scaledImageInByte = baos.toByteArray();
	        baos.close();

		}
		catch(Exception e){
			
		}
        return scaledImageInByte;
    }
	
	// directory verification
	private void checkResultingDir(String dir) throws MyRestPreconditionsException {
		
		if(!(new File(dir)).isDirectory()){
			// it does not exist yet ; make it :
			File newDir = new File(dir);
			newDir.mkdirs();
			if(!newDir.isDirectory()){
				throw new MyRestPreconditionsException("Directory creation error", "Invalid path to directory");
			}
		}
		
		if(!java.nio.file.Files.isWritable((new File(dir)).toPath())){
			throw new MyRestPreconditionsException("Directory creation exception", "File location is not writable");
		}
	}

	// create dir path in classpath: from userId / herbId
	private String buildDirPath(Long id) throws MyRestPreconditionsException{
		if (id==null || (id!=null && id<=0)){
			throw new MyRestPreconditionsException("Save Image File !","Id used for directory structure traverse is invalid");
		}
		
		char[] folders = (""+id).toCharArray();
		
		String dir = System.getProperty("user.dir")+File.separator+"images"+File.separator;
		
		for(int i=folders.length-1 ; i>=0 ; i--){
			dir += (folders[i]+File.separator);
		}
		
		return dir;
	}
}
