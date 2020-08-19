package com.green.health;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import com.green.health.images.storage.StorageService.ImgType;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;
import com.green.health.herb.dao.HerbLocaleRepository;
import com.green.health.herb.dao.HerbRepository;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.herb.entities.HerbLocaleJPA;
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
	private HerbLocaleRepository mockHerbLocaleDao;
	
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
	private static HerbLocaleJPA hjpa;
	
	@BeforeClass
	public static void init(){
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		for(long i=0 ; i<3 ; i++){
			HerbJPA jpa = new HerbJPA();
			jpa.setId(i);
			jpa.setLatinName("latinName-"+i);
			jpa.setEngName("engName-"+i);
			jpa.setDescription("bilosta");
			list.add(jpa);
		}
		
		postModel = new HerbDTO();
		postModel.setDescription("static description");
		postModel.setLatinName("static latinName");
		postModel.setLocalName("static localName");

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
		illnessModel.setLocalName("serbian illness name");
		patchModel.getIllnesses().add(illnessModel);
		
		illnessJpa = new IllnessJPA();
		illnessJpa.setId(1L);
		illnessJpa.setEngName("serbian illness name");
		illnessJpa.setLatinName("latin illness name");
		illnessJpa.setDescription("illnessDescription");
		illnessJpa.setSymptoms("illnessSympthoms");
		
		hjpa = new HerbLocaleJPA();
		hjpa.setLocale(Locale.FRANCE.toString());
		hjpa.setDescription("francuski opis");
		hjpa.setLocalName("francusko ime");
		hjpa.setHerb(list.get(1));
		list.get(1).getHerbLocales().add(hjpa);
	}
	
	@After // after each and every test
	public void setLocale() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
	}
	
	@Test
	public void getAllHerbs(){
		when(mockHerbDao.findAll()).thenReturn(list);
		
		List<HerbDTO> result = mockHerbServiceImpl.getAll();
		
		assertEquals(list.size(), result.size());
		
		for(int i=0; i<3 ; i++){
			assertEquals(list.get(i).getId(), result.get(i).getId());
			assertEquals(list.get(i).getLatinName() , result.get(i).getLatinName() );
			assertEquals(list.get(i).getEngName(), result.get(i).getLocalName());
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
			assertEquals(list.get(1).getEngName(), model.getLocalName());
			assertEquals(list.get(1).getDescription(), model.getDescription());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void getHerbByNameTest1(){
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(list.get(0));
		
		try {
			HerbDTO model = mockHerbServiceImpl.getHerbByLatinName("bilosta");
			assertEquals(list.get(0).getId(), model.getId());
			assertEquals(list.get(0).getLatinName(), model.getLatinName());
			assertEquals(list.get(0).getEngName(), model.getLocalName());
			assertEquals(list.get(0).getDescription(), model.getDescription());
		} catch (MyRestPreconditionsException e) {
			fail("cannot find herb by latin name 'bilosta'");
		}
	}
	
	@Test
	public void getHerbByNameTest2() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		when(mockHerbDao.getHerbByEngName(Mockito.anyString())).thenReturn(list.get(0));
		
		try {
			HerbDTO model = mockHerbServiceImpl.getHerbByLocalName("bilosta");
			assertEquals(list.get(0).getId(), model.getId());
			assertEquals(list.get(0).getLatinName(), model.getLatinName());
			assertEquals(list.get(0).getEngName(), model.getLocalName());
			assertEquals(list.get(0).getDescription(), model.getDescription());
		} catch (MyRestPreconditionsException e) {
			fail("cannot find herb by given name 'bilosta'");
		}
	}
	
	@Test
	public void getHerbByNotEnglishName() {
		LocaleContextHolder.setLocale(Locale.FRANCE);
		
		when(mockHerbLocaleDao.findWhereLocaleAndLocalName(Mockito.anyString(), Mockito.anyString())).thenReturn(hjpa);
		
		try {
			HerbDTO model = mockHerbServiceImpl.getHerbByLocalName("francusko ime");
			assertEquals(hjpa.getLocalName(), model.getLocalName());
			assertEquals(hjpa.getDescription(), model.getDescription());
		} catch (MyRestPreconditionsException e) {
			fail("Herb by not-english name test failed");
		}
	}
	
	
	
	
	
	
	@Test
	public void createNewHerbTest(){
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
			assertEquals("The following request form data is missing or invalid :",e.getDetails());
		}
	}
	
	@Test
	public void createHerbEnglishNameAlreadyExists(){
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(null);
		when(mockHerbDao.getHerbByEngName(Mockito.anyString())).thenReturn(list.get(0));
		
		try {
			mockHerbServiceImpl.addNew(postModel);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("We assert your locale as English. The herb with eng. name "+postModel.getLocalName()+" is already in our database.",e.getDetails());
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
			assertEquals("The latin name "+postModel.getLatinName()+" has already been assigned to another herb",e.getDetails());
		}
	}
	
	@Test
	public void createHerbWithFranchData() {
		LocaleContextHolder.setLocale(Locale.FRANCE);
		
		when(mockHerbDao.save(Mockito.any(HerbJPA.class))).thenReturn(list.get(0));
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(null);
		when(mockHerbLocaleDao.save(Mockito.any(HerbLocaleJPA.class))).thenReturn(null);
		when(mockHerbLocaleDao.findWhereLocaleAndLocalName(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		
		try {
			mockHerbServiceImpl.addNew(postModel);
		} catch (MyRestPreconditionsException e) {
			fail("failed to create Franch herb.");
		}
	}
	
	@Test
	public void createHerbWithFranchDataAndFrenchNameAlreadyExists() {
		LocaleContextHolder.setLocale(Locale.FRANCE);
		
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(null);
		when(mockHerbLocaleDao.findWhereLocaleAndLocalName(Mockito.anyString(), Mockito.anyString())).thenReturn(hjpa);
		
		postModel.setLocalName(hjpa.getLocalName());
		
		try {
			mockHerbServiceImpl.addNew(postModel);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("The herb with local name "+postModel.getLocalName()+" is already in our database.",e.getDetails());
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
			assertEquals("Your edit request is invalid - You must provide some editable data",e.getDetails());
		}
	}
	
	@Test
	public void tryingToEditANonExistingHerbTest(){
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
	public void editHerbLocalNameBelongsToAnotherHerbTest(){
		LocaleContextHolder.setLocale(Locale.FRANCE);
		
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(list.get(2));
		when(mockHerbDao.getHerbByLatinName(Mockito.anyString())).thenReturn(list.get(2));
		when(mockHerbLocaleDao.findWhereLocaleAndLocalName(Mockito.anyString(), Mockito.anyString())).thenReturn(hjpa);

		patchModel.setLocalName(hjpa.getLocalName());
		
		try {
			mockHerbServiceImpl.edit(patchModel, 2L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("The herb with local name "+patchModel.getLocalName()+" is already in our database.",e.getDetails());
		}
	}
	
	@Test
	public void successfulEdit(){
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockHerbDao.save(Mockito.any(HerbJPA.class))).thenReturn(list.get(1));
		
		when(mockIllnessDao.findByLatinName(Mockito.anyString())).thenReturn(illnessJpa);
		
		try {
			HerbDTO result = mockHerbServiceImpl.edit(patchModel, 1L);
			assertEquals("new latin name",result.getLatinName());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
		
		list.get(1).setLatinName("latinName-1");
	}
	
	@Test
	public void tryMakeDuplicateHerbIllnessLink() {
		// illnessJpa is already in a list of links for that herb :
		LinkJPA link = new LinkJPA(list.get(1),illnessJpa);
		list.get(1).getLinks().add(link);
		
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockIllnessDao.findByLatinName(Mockito.anyString())).thenReturn(illnessJpa);
		
		when(mockHerbDao.save(Mockito.any(HerbJPA.class))).thenReturn(list.get(1));
		
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
		when(mockIllnessDao.findByEngName(Mockito.anyString())).thenReturn(null);
		
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
			assertEquals("herb with id = 1 does not exist in our database.", e.getDetails());
		}
	}
	
	@Test
	public void successfullDeleteHerb() {
		when(mockHerbDao.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		doNothing().when(mockHerbDao).deleteById(isA(Long.class));

		try {
			doNothing().when(mockStorageServiceImpl).deleteImage(isA(Long.class), isA(ImgType.class));
			mockHerbServiceImpl.delete(1L);
			assertTrue(true);
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
}