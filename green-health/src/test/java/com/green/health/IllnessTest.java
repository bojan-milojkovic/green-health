package com.green.health;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;

import com.green.health.herb.dao.HerbRepository;
import com.green.health.herb.entities.HerbDTO;
import com.green.health.herb.entities.HerbJPA;
import com.green.health.illness.dao.IllnessLocaleRepository;
import com.green.health.illness.dao.IllnessRepository;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.illness.entities.IllnessJPA;
import com.green.health.illness.entities.IllnessLocaleJPA;
import com.green.health.illness.service.impl.IllnessServiceImpl;
import com.green.health.ratings.entities.LinkJPA;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@RunWith(MockitoJUnitRunner.class)
public class IllnessTest {

	@Mock
	private IllnessRepository mockIllnessRepo;
	
	@Mock
	private HerbRepository mockHerbRepo;
	
	@Mock
	private IllnessLocaleRepository mockIllnessLocaleRepo;
	
	@InjectMocks
	private IllnessServiceImpl mockIllnessServiceImpl;
	
	private static List<IllnessJPA> list = new ArrayList<IllnessJPA>();
	private static IllnessDTO postModel, patchModel;
	private static HerbDTO herbDto;
	private static HerbJPA herbJpa;
	private static IllnessLocaleJPA ijpa;
	
	@BeforeClass
	public static void init() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		for(int i=0 ; i<4 ; i++){
			IllnessJPA jpa = new IllnessJPA();
			jpa.setId((long)i);
			jpa.setDescription("illness_"+i);
			jpa.setLatinName("Latin name "+i);
			jpa.setEngName("Eng name "+i);
			jpa.setSymptoms("sympthom1, sympthom 2");
			jpa.setCause("Eng cause");
			jpa.setTreatment("Eng treatment");
			list.add(jpa);
		}
		
		postModel = new IllnessDTO();
		postModel.setDescription("Some description");
		postModel.setLatinName("Some latin name");
		postModel.setLocalName("Some illness name");
		postModel.setSymptoms("vomiting, feaver, headache");
		postModel.setCause("Some illness cause");
		postModel.setTreatment("Some treatment");
		
		patchModel = new IllnessDTO();
		patchModel.setId(1L);
		patchModel.setSymptoms("cramps, skin rash");
		
		herbDto = new HerbDTO();
		herbDto.setId(1L);
		herbDto.setLatinName("herb latin name");
		herbDto.setLocalName("herb srb name");
		patchModel.setHerbs(new ArrayList<HerbDTO>());
		patchModel.getHerbs().add(herbDto);
		
		patchModel.setHerbs(new ArrayList<HerbDTO>());
		patchModel.getHerbs().add(herbDto);
		
		herbJpa = new HerbJPA();
		herbJpa.setId(1L);
		herbJpa.setDescription("some herb description");
		herbJpa.setGrowsAt("sunny fields");
		herbJpa.setLatinName("herb latin name");
		herbJpa.setProperties("some properties");
		herbJpa.setEngName("herb eng name");
		herbJpa.setWarnings("tastes bad");
		herbJpa.setWhenToPick("summer");
		
		ijpa = new IllnessLocaleJPA();
		ijpa.setId(1L);
		ijpa.setLocale(Locale.FRANCE.toString());
		ijpa.setDescription("French description");
		ijpa.setLocalName("French name");
		ijpa.setIllness(list.get(1));
		list.get(1).getIllnessLocales().add(ijpa);
	}
	
	@After // after each and every test
	public void setLocale() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
	}
	
	@Test
	public void getAllIllnessesTest() {
		when(mockIllnessRepo.findAll()).thenReturn(list);
		
		List<IllnessDTO> result = mockIllnessServiceImpl.getAll();
		
		assertEquals(result.size(), list.size());
		
		for(int i=0 ; i<result.size() ; i++) {
			assertEquals(list.get(i).getDescription(), result.get(i).getDescription());
			assertEquals(list.get(i).getLatinName(), result.get(i).getLatinName());
			assertEquals(list.get(i).getLocalName(), result.get(i).getLocalName());
			assertEquals(list.get(i).getSymptoms(), result.get(i).getSymptoms());
		}
	}
	
	@Test
	public void getIllnessInvalidIdTest() {
		try {
			mockIllnessServiceImpl.getOneById(-1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Id you entered is missing or invalid", e.getDetails());
		}
	}
	
	@Test
	public void getIllnessNonExistantIdTest() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(null);
		
		try {
			mockIllnessServiceImpl.getOneById(1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Cannot find the illness with id = 1", e.getDetails());
		}
	}
	
	@Test
	public void getIllnessByInvalidLatinNameTest() {
		when(mockIllnessRepo.findByLatinName(Mockito.anyString())).thenReturn(null);
		
		try {
			mockIllnessServiceImpl.getOneByLatinName("blatruc");
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Cannot find the illness with latin name 'blatruc'", e.getDetails());
		}
	}
	
	@Test
	public void getIllnessByNotEnglishName() {
		LocaleContextHolder.setLocale(Locale.FRANCE);
		when(mockIllnessRepo.findByEngName(Mockito.anyString())).thenReturn(null);
		when(mockIllnessLocaleRepo.findWhereLocaleAndLocalName(Mockito.anyString(), Mockito.anyString())).thenReturn(ijpa);
		
		try {
			IllnessDTO result = mockIllnessServiceImpl.getOneByLocalName("French name");
			assertEquals("Latin name 1", result.getLatinName());
			assertEquals("French name", result.getLocalName());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void addIllnessPostDataMissing() {
		
		try {
			mockIllnessServiceImpl.addNew(patchModel);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("The following request form data is missing or invalid :", e.getDetails());
		}
	}
	
	@Test
	public void editIllnessInvalidId() {
		IllnessDTO badModel = new IllnessDTO();
		
		try {
			mockIllnessServiceImpl.edit(badModel, -1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Id you entered is missing or invalid", e.getDetails());
		}
	}
	
	@Test
	public void editIllnessNoPatchData() {
		IllnessDTO badModel = new IllnessDTO();
		
		try {
			mockIllnessServiceImpl.edit(badModel, 1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Your edit request is invalid - You must provide some editable data", e.getDetails());
		}
	}
	
	@Test
	public void tryToEditNonExistingIllness() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(null);
		IllnessDTO badModel = new IllnessDTO();
		badModel.setLatinName("newLatinName");
		
		try {
			mockIllnessServiceImpl.edit(badModel, 1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Illness with id = 1 does not exist in our database.", e.getDetails());
		}
	}
	
	@Test
	public void tryToEditIllnessByGivingItEnglishNameOfAnotherIllness() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockIllnessRepo.findByEngName(Mockito.anyString())).thenReturn(list.get(2));
		
		patchModel.setLocalName("Eng name 2");
		
		try {
			mockIllnessServiceImpl.edit(patchModel, 1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {	
			assertEquals("We assert your language as English ; "+
					"The name '"+patchModel.getLocalName()+"' belongs to another illness in our database.", e.getDetails());
		}
		patchModel.setLatinName(null);
	}
	
	@Test
	public void tryToEditIllnessByGivingItLocalNameOfAnotherIllness() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(list.get(2));
		when(mockIllnessRepo.findByEngName(Mockito.anyString())).thenReturn(null);
		when(mockIllnessLocaleRepo.findWhereLocaleAndLocalName(Mockito.anyString(), Mockito.anyString())).thenReturn(ijpa);
		
		LocaleContextHolder.setLocale(Locale.FRENCH);
		postModel.setLocalName("French name");
		
		try {
			mockIllnessServiceImpl.edit(patchModel, 2L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			
			assertEquals("We assert your language as French ; "+
					"The name '"+patchModel.getLocalName()+"' belongs to another illness in our database.", e.getDetails());
		}
		patchModel.setLatinName(null);
	}
	
	@Test
	public void tryMakeDuplicateHerbLinkToOneIllnessTest() {
		// add a herb-illness link to test illness
		LinkJPA link = new LinkJPA(herbJpa, list.get(1));
		list.get(1).getLinks().add(link);
		
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockHerbRepo.getHerbByLatinName(Mockito.anyString())).thenReturn(herbJpa);
		
		when(mockIllnessRepo.save(Mockito.any(IllnessJPA.class))).thenReturn(list.get(1));
		
		try {
			// patchModel already has the same herb-illness link
			IllnessDTO result = mockIllnessServiceImpl.edit(patchModel, 1L);
			
			list.get(1).setLinks(new HashSet<LinkJPA>());
			
			assertEquals(1, result.getHerbs().size());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void tryLinkNonExistentHerbToIllnessTest() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockHerbRepo.getHerbByLatinName(Mockito.anyString())).thenReturn(null);
		when(mockHerbRepo.getHerbByEngName(Mockito.anyString())).thenReturn(null);
		
		try {
			mockIllnessServiceImpl.edit(patchModel, 1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Link herb to illness error", e.getDescription());
		}
	}
	
	@Test
	public void tryToDeleteInvalidIdTest() {
		try {
			mockIllnessServiceImpl.delete(-1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Id you entered is missing or invalid", e.getDetails());
		}
	}
	
	@Test
	public void tryToDeleteNonExistingIllnessTest() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(null);
		
		try {
			mockIllnessServiceImpl.delete(1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("illness with id = 1 does not exist in our database.", e.getDetails());
		}
	}
	
	@Test
	public void assignHerbToIllnessTest() {
		when(mockHerbRepo.getHerbByLatinName(Mockito.anyString())).thenReturn(herbJpa);
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		
		when(mockIllnessRepo.save(isA(IllnessJPA.class))).thenReturn(list.get(1));
		
		IllnessDTO result;
		try {
			result = mockIllnessServiceImpl.edit(patchModel, 1L);
			
			assertEquals(result.getSymptoms(), patchModel.getSymptoms());
			assertTrue(!result.getHerbs().isEmpty());
			assertEquals("herb latin name", result.getHerbs().get(0).getLatinName());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
}