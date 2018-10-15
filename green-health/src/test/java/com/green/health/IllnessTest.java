package com.green.health;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
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
import com.green.health.illness.dao.IllnessRepository;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.illness.entities.IllnessJPA;
import com.green.health.illness.service.impl.IllnessServiceImpl;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@RunWith(MockitoJUnitRunner.class)
public class IllnessTest {

	@Mock
	private IllnessRepository mockIllnessRepo;
	
	@Mock
	private HerbRepository mockHerbRepo;
	
	@InjectMocks
	private IllnessServiceImpl mockIllnessServiceImpl;
	
	private static List<IllnessJPA> list = new ArrayList<IllnessJPA>();
	private static IllnessDTO postModel, patchModel;
	private static HerbDTO herbDto;
	private static HerbJPA herbJpa;
	
	@BeforeClass
	public static void init() {
		for(int i=0 ; i<4 ; i++){
			IllnessJPA jpa = new IllnessJPA();
			jpa.setId((long)i);
			jpa.setDescription("illness_"+i);
			jpa.setLatinName("Latin name "+i);
			jpa.setSrbName("Srb name "+i);
			jpa.setSymptoms("sympthom1, sympthom 2");
			list.add(jpa);
		}
		
		postModel = new IllnessDTO();
		postModel.setDescription("Some description");
		postModel.setLatinName("Some latin name");
		postModel.setSrbName("Some serb name");
		postModel.setSymptoms("vomiting, feaver, headache");
		
		patchModel = new IllnessDTO();
		patchModel.setId(1L);
		patchModel.setSymptoms("cramps, skin rash");
		
		herbDto = new HerbDTO();
		herbDto.setId(1L);
		herbDto.setLatinName("herb latin name");
		herbDto.setSrbName("herb srb name");
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
		herbJpa.setSrbName("herb srb name");
		herbJpa.setWarnings("tastes bad");
		herbJpa.setWhenToPick("summer");
	}
	
	@Test
	public void getAllIllnessesTest() {
		when(mockIllnessRepo.findAll()).thenReturn(list);
		
		List<IllnessDTO> result = mockIllnessServiceImpl.getAll();
		
		assertEquals(result.size(), list.size());
		
		for(int i=0 ; i<result.size() ; i++) {
			assertEquals(list.get(i).getDescription(), result.get(i).getDescription());
			assertEquals(list.get(i).getLatinName(), result.get(i).getLatinName());
			assertEquals(list.get(i).getSrbName(), result.get(i).getSrbName());
			assertEquals(list.get(i).getSymptoms(), result.get(i).getSymptoms());
		}
	}
	
	@Test
	public void getIllnessInvalidIdTest() {
		try {
			mockIllnessServiceImpl.getOneById(-1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("Id is invalid", e.getDetails());
		}
	}
	
	@Test
	public void getIllnessNonExistantIdTest() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(null);
		
		try {
			mockIllnessServiceImpl.getOneById(1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("Cannot find illness with id = 1", e.getDetails());
		}
	}
	
	@Test
	public void getIllnessByInvalidNameTest() {
		when(mockIllnessRepo.findByLatinName(Mockito.anyString())).thenReturn(null);
		when(mockIllnessRepo.findBySrbName(Mockito.anyString())).thenReturn(null);
		
		try {
			mockIllnessServiceImpl.getOneByName("blatruc");
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("The the illness with the name blatruc is not in our database.", e.getDetails());
		}
	}
	
	@Test
	public void addIllnessPostDataMissing() {
		
		try {
			mockIllnessServiceImpl.addNew(patchModel);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("The following data is missing from the illness form", e.getDetails());
		}
	}
	
	@Test
	public void addIllnessLatinNameTakenTest() {
		
		when(mockIllnessRepo.findByLatinName(Mockito.anyString())).thenReturn(list.get(0));
		
		try {
			mockIllnessServiceImpl.addNew(postModel);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("Illness with Latin name Some latin name is already in our database.", e.getDetails());
		}
	}
	
	@Test
	public void addIllnessSrbNameTakenTest() {
		
		when(mockIllnessRepo.findByLatinName(Mockito.anyString())).thenReturn(null);
		when(mockIllnessRepo.findBySrbName(Mockito.anyString())).thenReturn(list.get(0));
		
		try {
			mockIllnessServiceImpl.addNew(postModel);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("Illness with Serbian name Some serb name is already in our database.", e.getDetails());
		}
	}
	
	@Test
	public void editIllnessInvalidId() {
		IllnessDTO badModel = new IllnessDTO();
		
		try {
			mockIllnessServiceImpl.edit(badModel, -1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals(e.getDetails(), "Id is invalid");
		}
	}
	
	@Test
	public void editIllnessNoPatchData() {
		IllnessDTO badModel = new IllnessDTO();
		
		try {
			mockIllnessServiceImpl.edit(badModel, 1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("To edit the illness you must provide some editable data.", e.getDetails());
		}
	}
	
	@Test
	public void tryToEditNonExistingIllness() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(null);
		IllnessDTO badModel = new IllnessDTO();
		badModel.setLatinName("newLatinName");
		
		try {
			mockIllnessServiceImpl.edit(badModel, 1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("Illness with id = 1 does not exist in our database.", e.getDetails());
		}
	}
	
	@Test
	public void tryToEditIllnessByGivingItLatinNameOfAnotherIllnessTest() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockIllnessRepo.findByLatinName(Mockito.anyString())).thenReturn(list.get(2));
		when(mockHerbRepo.getHerbByLatinName(Mockito.anyString())).thenReturn(herbJpa);
		
		patchModel.setLatinName("Latin name 2");
		
		try {
			mockIllnessServiceImpl.edit(patchModel, 1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			patchModel.setLatinName(null);
			assertEquals("The latin name Latin name 2 is already assigned to another illness", e.getDetails());
		}
	}
	
	@Test
	public void tryMakeDuplicateHerbLinkToOneIllnessTest() {
		// add a herb-illness link to test illness
		list.get(1).getHerbs().add(herbJpa);
		
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockHerbRepo.getHerbByLatinName(Mockito.anyString())).thenReturn(herbJpa);
		
		when(mockIllnessRepo.save(Mockito.any(IllnessJPA.class))).thenReturn(null);
		
		try {
			// patchModel already has the same herb-illness link
			IllnessDTO result = mockIllnessServiceImpl.edit(patchModel, 1L);
			list.get(1).setHerbs(new HashSet<HerbJPA>());
			assertEquals(1, result.getHerbs().size());
		} catch (MyRestPreconditionsException e) {
			fail();
		}
	}
	
	@Test
	public void tryLinkNonExistentHerbToIllnessTest() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockHerbRepo.getHerbByLatinName(Mockito.anyString())).thenReturn(null);
		when(mockHerbRepo.getHerbBySrbName(Mockito.anyString())).thenReturn(null);
		
		try {
			mockIllnessServiceImpl.edit(patchModel, 1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("Link herb to illness error", e.getDescription());
		}
	}
	
	@Test
	public void tryToDeleteInvalidIdTest() {
		try {
			mockIllnessServiceImpl.delete(-1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("Id is invalid", e.getDetails());
		}
	}
	
	@Test
	public void tryToDeleteNonExistingIllnessTest() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(null);
		
		try {
			mockIllnessServiceImpl.delete(1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals("Illness with id = 1 does not exist in our database.", e.getDetails());
		}
	}
	
	@Test
	public void assignHerbToIllnessTest() {
		when(mockHerbRepo.getHerbByLatinName(Mockito.anyString())).thenReturn(herbJpa);
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		
		when(mockIllnessRepo.save(isA(IllnessJPA.class))).thenReturn(null);
		
		IllnessDTO result;
		try {
			result = mockIllnessServiceImpl.edit(patchModel, 1L);
			
			assertEquals(result.getSymptoms(), patchModel.getSymptoms());
			assertTrue(!result.getHerbs().isEmpty());
			assertEquals("herb latin name", result.getHerbs().get(0).getLatinName());
		} catch (MyRestPreconditionsException e) {
			fail();
		}
	}
}