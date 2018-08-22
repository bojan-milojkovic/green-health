package com.green.health.images.storage.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.green.health.images.storage.StorageService;
import com.green.health.util.RestPreconditions;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@Service
public class StorageServiceImpl implements StorageService {

	private String fileStorageLocation;
	private ResourceLoader resourceLoader;
	
	@Autowired
	public StorageServiceImpl(ResourceLoader resourceLoader) throws MyRestPreconditionsException {
		this.resourceLoader = resourceLoader;
		this.fileStorageLocation = System.getProperty("user.dir")+File.separator+"images"+File.separator;
		
		try {
            Files.createDirectories(Paths.get(fileStorageLocation).toAbsolutePath().normalize());
        } catch (Exception ex) {
            throw new MyRestPreconditionsException("Image service initialization error", 
            		"Could not create the directory where the uploaded files will be stored.");
        }
	}

	@Override
	public void saveImage(final MultipartFile mpf, Long id, boolean isUser) throws MyRestPreconditionsException {	
		RestPreconditions.assertTrue(null != mpf, "You are attempting to upload a non-existing file.");
		RestPreconditions.assertTrue(!mpf.isEmpty(), "You are attempting to upload an empty file.");
		RestPreconditions.assertTrue(!mpf.getOriginalFilename().contains(".."), "Upload filename contains invalid path sequence");
		RestPreconditions.assertTrue(mpf.getSize() < (3 * 1024 * 1024), "Max upload file size is 3MB");
		RestPreconditions.assertTrue(id!=null && id>0, "Uploading file for invalid user/herb id ("+id+")");
		
		// compose dir structure :
		String dir = buildDirPath(id);
		
		// check resulting dir :
		checkResultingDir(dir);
		
		// put files in dir :
		saveFileInDir(dir, mpf, isUser);
	}
	
	@Override
	public Resource readImage(final Long id, final String name) throws MyRestPreconditionsException{
		
		String imgDir = buildDirPath(id);
		
		if(!(new File(imgDir)).isDirectory()){
			return resourceLoader.getResource("classpath:images/no_image_found.jpeg");
		}
		
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(imgDir))){
			Path filePath = null;
			
			for (Path path : directoryStream) {
            	if(path.toString().contains(name)){
            		filePath = path;
            		break;
            	}
            }
			
			if(filePath!=null){
				return new UrlResource(filePath.toUri());
			} else {
            	return resourceLoader.getResource("classpath:images/no_image_found.jpeg");
            }
		} catch (Exception e){
			throw new MyRestPreconditionsException("Resource retreave error", "Oops ! Something went wrong in retreaving the image.");
		}
	}
	
	// save file :
	private void saveFileInDir(String dir, MultipartFile mpf, boolean isUser) throws MyRestPreconditionsException{

		String fileName = isUser ? "profile" : "herb";
		String contentType = mpf.getContentType().split("/")[1];
		
		// delete previous :
		deletePreviousImage(dir, fileName);
		
		try {
			// save original :
			if(!isUser){ // herb has both regular size image and thumbnail ; user has only thumbnail :
				Path targetLocation = Paths.get(dir + fileName+"."+contentType).toAbsolutePath().normalize();
				Files.copy(mpf.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			}
			// save thumbnail :
			Path targetLocation = Paths.get(dir + fileName+"_THUMBNAIL."+contentType).toAbsolutePath().normalize();
			Files.copy(scaleImageInputstream(mpf, contentType, 100, 120), 
					targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
		}catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new MyRestPreconditionsException("Image save error", "Failed to save image in "+dir);
		}
	}
	
	private void deletePreviousImage(String dir, String name){
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : directoryStream) {
            	if(path.toString().contains(name)){
            		Files.delete(path);
            		break;
            	}
            }
        } catch (IOException ex) {
        	
        }
	}
	
	private InputStream scaleImageInputstream(final MultipartFile multipart,final String contentType,final int targetWidth,final int targetHeight) throws MyRestPreconditionsException {
		try{
	        BufferedImage originalImg = ImageIO.read(multipart.getInputStream());
	        
	        BufferedImage scaledImg = Scalr.resize(originalImg, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH,
	                targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
	        
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        ImageIO.write(scaledImg, contentType, os);
	        os.flush();
	        return new ByteArrayInputStream(os.toByteArray());
		}
		catch(Exception e){
			throw new MyRestPreconditionsException("Image scaling error","Ooops - something went wrong !");
		}
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
		
		String dir = fileStorageLocation;
		
		for(int i=folders.length-1 ; i>=0 ; i--){
			dir += (folders[i]+File.separator);
		}
		
		return dir;
	}
}
