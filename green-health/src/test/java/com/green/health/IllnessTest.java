package com.green.health;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.green.health.illness.dao.IllnessRepository;
import com.green.health.illness.entities.IllnessDTO;
import com.green.health.illness.entities.IllnessJPA;
import com.green.health.illness.service.impl.IllnessServiceImpl;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@RunWith(MockitoJUnitRunner.class)
public class IllnessTest {

	@Mock
	private IllnessRepository mockIllnessRepo;
	
	@InjectMocks
	private IllnessServiceImpl mockIllnessServiceImpl;
	
	private static List<IllnessJPA> list = new ArrayList<IllnessJPA>();
	private static IllnessDTO postModel, patchModel;
	
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
	}
	
	@Test
	public void getAllIllnessesTest() {
		when(mockIllnessRepo.findAll()).thenReturn(list);
		
		List<IllnessDTO> result = mockIllnessServiceImpl.getAll();
		
		assertEquals(result.size(), list.size());
		
		for(int i=0 ; i<result.size() ; i++) {
			assertEquals(result.get(i).getDescription(), list.get(i).getDescription());
			assertEquals(result.get(i).getLatinName(), list.get(i).getLatinName());
			assertEquals(result.get(i).getSrbName(), list.get(i).getSrbName());
			assertEquals(result.get(i).getSymptoms(), list.get(i).getSymptoms());
		}
	}
	
	@Test
	public void getIllnessInvalidIdTest() {
		try {
			mockIllnessServiceImpl.getOneById(-1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals(e.getDetails(), "Id is invalid");
		}
	}
	
	@Test
	public void getIllnessNonExistantIdTest() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(null);
		
		try {
			mockIllnessServiceImpl.getOneById(1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals(e.getDetails(), "Cannot find illness with id = 1");
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
			assertEquals(e.getDetails(), "The the illness with the name blatruc is not in our database.");
		}
	}
	
	@Test
	public void addIllnessPostDataMissing() {
		
		try {
			mockIllnessServiceImpl.addNew(patchModel);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals(e.getDetails(), "The following data is missing from the illness form");
		}
	}
	
	@Test
	public void addIllnessLatinNameTakenTest() {
		
		when(mockIllnessRepo.findByLatinName(Mockito.anyString())).thenReturn(list.get(0));
		
		try {
			mockIllnessServiceImpl.addNew(postModel);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals(e.getDetails(), "Illness with Latin name Some latin name is already in our database.");
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
			assertEquals(e.getDetails(), "Illness with Serbian name Some serb name is already in our database.");
		}
	}
}