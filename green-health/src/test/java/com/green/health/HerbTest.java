package com.green.health;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.green.health.herb.dao.HerbRepository;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.herb.service.impl.HerbServiceImpl;
import com.green.health.illness.dao.IllnessRepository;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.illness.entities.IllnessJPA;
import com.green.health.images.storage.StorageService;
import com.green.health.ratings.entities.LinkJPA;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@RunWith(MockitoJUnitRunner.class)
public class HerbTest {

	@Mock
	private HerbRepository mockHerbDao;
	
	@Mock
	private StorageService mockStorageServiceImpl;
	
	@Mock
	private IllnessRepository mockIllnessDao;
	
	@InjectMocks
	private HerbServiceImpl mockHerbServiceImpl;
	
	private static List<HerbJPA> list = new ArrayList<HerbJPA>();
	private static HerbDTO postModel, patchModel;
	private static IllnessDTO illnessModel;
	private static IllnessJPA illnessJpa;
	
	@BeforeClass
	public static void init(){
		for(long i=0 ; i<3 ; i++){
			HerbJPA jpa = new HerbJPA();
			jpa.setId(i);
			jpa.setLatinName("latinName-"+i);
			jpa.setEngName("srbName-"+i);
			jpa.setDescription("bilosta");
			list.add(jpa);
		}
		
		postModel = new HerbDTO();
		postModel.setDescription("static description");
		postModel.setLatinName("static latinName");
		postModel.setEngName("static srbName");
		postModel.setGrowsAt("static location");
		postModel.setProperties("static properties");
		postModel.setWarnings("static warnings");
		postModel.setWhenToPick("static instructions");
		postModel.setWhereToBuy("static location");
		
		patchModel = new HerbDTO();
		patchModel.setId(20L);
		patchModel.setLatinName("new latin name");
		
		patchModel.setIllnesses(new ArrayList<IllnessDTO>());
		illnessModel = new IllnessDTO();
		illnessModel.setId(1L);
		illnessModel.setLatinName("latin illness name");
		illnessModel.setSrbName("serbian illness name");
		patchModel.getIllnesses().add(illnessModel);
		
		illnessJpa = new IllnessJPA();
		illnessJpa.setId(1L);
		illnessJpa.setSrbName("serbian illness name");
		illnessJpa.setLatinName("latin illness name");
		illnessJpa.setDescription("illnessDescription");
		illnessJpa.setSymptoms("illnessSympthoms");
	}
	
	@Test
	public void getAllHerbs(){
		when(mockHerbDao.findAll()).thenReturn(list);
		
		List<HerbDTO> result = mockHerbServiceImpl.getAll();
		
		assertEquals(list.size(), result.size());
		
		for(int i=0; i<3 ; i++){
			assertEquals(list.get(i).getId(), result.get(i).getId());
			assertEquals(list.get(i).getLatinName() , result.get(i).getLatinName() );
			assertEquals(list.get(i).getEngName(), result.get(i).getEngName());
			assertEquals(list.get(i).getDescription(), result.get(i).getDescription());
		}
	}
	
	@Test
	public void getHerbByIdTest(){
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		
		HerbDTO model;
		try {
			model = mockHerbServiceImpl.getOneById(1L);
			
			assertEquals(list.get(1).getId(), model.getId());
			assertEquals(list.get(1).getLatinName(), model.getLatinName());
			assertEquals(list.get(1).getEngName(), model.getEngName());
			assertEquals(list.get(1).getDescription(), model.getDescription());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void getHerbByNameTest1(){
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(list.get(0));
		
		HerbDTO model = mockHerbServiceImpl.getHerbByLatinName("bilosta");
		
		assertEquals(list.get(0).getId(), model.getId());
		assertEquals(list.get(0).getLatinName(), model.getLatinName());
		assertEquals(list.get(0).getEngName(), model.getEngName());
		assertEquals(list.get(0).getDescription(), model.getDescription());
	}
	
	@Test
	public void getHerbByNameTest2(){
		when(mockHerbDao.getHerbByEngName(Mockito.anyString())).thenReturn(list.get(0));
		
		HerbDTO model = mockHerbServiceImpl.getHerbByEngName("bilosta");
		
		assertEquals(list.get(0).getId(), model.getId());
		assertEquals(list.get(0).getLatinName(), model.getLatinName());
		assertEquals(list.get(0).getEngName(), model.getEngName());
		assertEquals(list.get(0).getDescription(), model.getDescription());
	}
	
	
	
	
	
	
	@Test
	public void createNewHerbTest(){
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(null);
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(null);
		
		when(mockHerbDao.save(Mockito.any(HerbJPA.class))).thenReturn(list.get(0));
		when(mockIllnessDao.findByLatinName(Mockito.anyString())).thenReturn(illnessJpa);
		
		try {
			mockHerbServiceImpl.addNew(postModel);
			assertTrue(true);
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void createHerbMissingPostData(){
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(null);
		when(mockHerbDao.getHerbByEngName(Mockito.anyString())).thenReturn(null);

		try {
			mockHerbServiceImpl.addNew(patchModel);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("The following data is missing from the herb form",e.getDetails());
		}
	}
	
	@Test
	public void createHerbSrbNameAlreadyExists(){
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(null);
		when(mockHerbDao.getHerbByEngName(Mockito.anyString())).thenReturn(list.get(0));
		
		try {
			mockHerbServiceImpl.addNew(postModel);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("The herb with Serbian name "+postModel.getEngName()+" is already in our database.",e.getDetails());
		}
	}
	
	@Test
	public void createHerbLatinNameAlreadyExists(){
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(list.get(0));
		when(mockHerbDao.getHerbByEngName(Mockito.anyString())).thenReturn(null);
		
		try {
			mockHerbServiceImpl.addNew(postModel);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("The herb with Latin name "+postModel.getLatinName()+" is already in our database.",e.getDetails());
		}
	}
	
	@Test
	public void editHerbNoPatchDataPresent(){
		
		HerbDTO invalidPatchModel = new HerbDTO();
		invalidPatchModel.setId(1L);
		
		try{
			mockHerbServiceImpl.edit(invalidPatchModel, invalidPatchModel.getId());
			fail("Exception expected");
		} catch(MyRestPreconditionsException e){
			assertEquals("Your herb edit request is invalid - You must provide some editable data",e.getDetails());
		}
	}
	
	@Test
	public void tryingToEditANonExistingHerb(){
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(null);
		
		try{
			mockHerbServiceImpl.edit(patchModel, 1L);
			fail("Exception expected");
		} catch(MyRestPreconditionsException e){
			assertEquals("Herb with id = 1 does not exist in our database.",e.getDetails());
		}
	}
	
	@Test
	public void editHerbLatinNameBelongsToAnotherHerbTest(){
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(list.get(2));
		when(mockIllnessDao.findByLatinName(Mockito.anyString())).thenReturn(illnessJpa);
		
		patchModel.setLatinName("latinName-2");
		
		try {
			mockHerbServiceImpl.edit(patchModel, 1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			patchModel.setLatinName(null);
			assertEquals("The latin name latinName-2 has already been assigned to another herb",e.getDetails());
		}
	}
	
	@Test
	public void successfulEdit(){
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockHerbDao.save(Mockito.any(HerbJPA.class))).thenReturn(null);
		
		when(mockIllnessDao.findByLatinName(Mockito.anyString())).thenReturn(illnessJpa);
		
		try {
			HerbDTO result = mockHerbServiceImpl.edit(patchModel, 1L);
			assertEquals("new latin name",result.getLatinName());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void tryMakeDuplicateHerbIllnessLink() {
		
		// illnessJpa is already in a list of links for that herb :
		LinkJPA link = new LinkJPA(list.get(1),illnessJpa);
		list.get(1).getLinks().add(link);
		
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockIllnessDao.findByLatinName(Mockito.anyString())).thenReturn(illnessJpa);
		
		when(mockHerbDao.save(Mockito.any(HerbJPA.class))).thenReturn(null);
		
		try {
			// illnessDto is already in the patch model :
			HerbDTO result = mockHerbServiceImpl.edit(patchModel, 1L);
			
			list.get(1).setLinks(new HashSet<LinkJPA>());
			
			assertEquals(1, result.getIllnesses().size());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void linkNonExitingIllnessWithHerbTest() {
		patchModel.setLatinName(null);
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockIllnessDao.findByLatinName(Mockito.anyString())).thenReturn(null);
		when(mockIllnessDao.findBySrbName(Mockito.anyString())).thenReturn(null);
		
		try {
			mockHerbServiceImpl.edit(patchModel, 2L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Link illness to herb error", e.getDescription());
		}
	}
	
	@Test
	public void tryToDeleteNonExistingHerb(){
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(null);
		
		try{
			mockHerbServiceImpl.delete(1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e){
			assertEquals("Herb with id = 1 does not exist in our database.", e.getDetails());
		}
	}
	
	@Test
	public void successfullDeleteHerb() {
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		doNothing().when(mockHerbDao).deleteById(isA(Long.class));

		try {
			doNothing().when(mockStorageServiceImpl).deleteImage(isA(Long.class), isA(boolean.class));
			mockHerbServiceImpl.delete(1L);
			assertTrue(true);
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
}