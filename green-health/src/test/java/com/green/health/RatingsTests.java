package com.green.health;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

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
import com.green.health.herb.entities.HerbJPA;
import com.green.health.herb.service.HerbService;
import com.green.health.illness.dao.IllnessRepository;
import com.green.health.illness.entities.IllnessJPA;
import com.green.health.ratings.entities.LinkJPA;
import com.green.health.ratings.entities.RatingDTO;
import com.green.health.ratings.repository.RatingsRepository;
import com.green.health.ratings.service.impl.RatingsServiceImpl;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.security.repositories.UserSecurityRepository;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@RunWith(MockitoJUnitRunner.class)
public class RatingsTests {

	@Mock
	private UserSecurityRepository mockUserSecurityRepo;
	@Mock
	private HerbRepository mockHerbRepo;
	@Mock
	private IllnessRepository mockIllnessRepo;
	@Mock
	private HerbService mockHerbServiceImpl;
	@Mock
	private RatingsRepository mockRatingsRepo;
	
	@InjectMocks
	private RatingsServiceImpl mockRatingsServiceImpl;
	
	private static RatingDTO model;
	private static UserSecurityJPA userSecurity;
	private static HerbJPA herb;
	private static IllnessJPA illness;
	private static LinkJPA link;
	
	@BeforeClass
	public static void init() {
		model = new RatingDTO();
		model.setHerbId(2L);
		model.setIllnessId(2L);
		model.setUsername("username1");
		
		herb = new HerbJPA();
		herb.setId(2L);
		illness = new IllnessJPA();
		illness.setId(2L);
		herb.getLinks().add(new LinkJPA(herb,illness));
		illness.getLinks().add(new LinkJPA(herb,illness));
		
		userSecurity = new UserSecurityJPA();
		
		link = new LinkJPA(herb,illness);
		userSecurity.getLinks().add(link);
	}
	
	@Test
	public void addNewRatingUsernameInvalidTest() {
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(null);
		
		try {
			mockRatingsServiceImpl.addNewRatingLink(model);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Cannot find the user with username = "+model.getUsername(), e.getDetails());
		}
	}
	
	@Test
	public void addNewRatingNoHerbForThatLinkTest() {
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(userSecurity);
		when(mockHerbRepo.getOne(Mockito.anyLong())).thenReturn(null);
		
		try {
			mockRatingsServiceImpl.addNewRatingLink(model);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Cannot find link with herbId="+model.getHerbId()+" and illnessId="+ model.getIllnessId(), e.getDetails());
		}
	}
	
	@Test
	public void addNewRatingNoIllnessForThatLinkTest() {
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(userSecurity);
		when(mockHerbRepo.getOne(Mockito.anyLong())).thenReturn(herb);
		
		herb.setLinks(new HashSet<LinkJPA>());
		
		try {
			mockRatingsServiceImpl.addNewRatingLink(model);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Cannot find link with herbId="+model.getHerbId()+" and illnessId="+ model.getIllnessId(), e.getDetails());
		}
		
		herb.getLinks().add(new LinkJPA(herb,illness));
	}
	
	@Test
	public void addNewRatingUserAlreadyRatedTest() {
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(userSecurity);
		when(mockRatingsRepo.findByHerbAndIllness(Mockito.anyLong(), Mockito.anyLong())).thenReturn(link);
		
		try {
			mockRatingsServiceImpl.addNewRatingLink(model);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("The user "+model.getUsername()+" has already rated this herb-illness link", e.getDetails());
		}
	}
	
	@Test
	public void addNewRatingValidTest() {
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(userSecurity);
		when(mockRatingsRepo.findByHerbAndIllness(Mockito.anyLong(), Mockito.anyLong())).thenReturn(link);
		
		userSecurity.setLinks(new HashSet<LinkJPA>());
		
		try {
			mockRatingsServiceImpl.addNewRatingLink(model);
			
			assertEquals(1, userSecurity.getLinks().size());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void getOneRatingInvalidHerbIdTest() {
		try {
			mockRatingsServiceImpl.getRatingForLink(-1L, 2L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Cannot find link with herbId=-1 and illnessId=2", e.getDetails());
		}
	}
	
	@Test
	public void getOneRatingInvalidIllnessIdTest() {
		try {
			mockRatingsServiceImpl.getRatingForLink(2L, -1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Cannot find link with herbId=2 and illnessId=-1", e.getDetails());
		}
	}
	
	@Test
	public void getOneRatingNoHerbForIdTest() {
		when(mockHerbRepo.getOne(Mockito.anyLong())).thenReturn(null);
		
		try {
			mockRatingsServiceImpl.getRatingForLink(2L, 2L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Cannot find link with herbId=2 and illnessId=2", e.getDetails());
		}
	}
	
	@Test
	public void getOneRatingNoIllnessForIdTest() {
		when(mockHerbRepo.getOne(Mockito.anyLong())).thenReturn(herb);
		
		herb.setLinks(new HashSet<LinkJPA>());
		
		try {
			mockRatingsServiceImpl.getRatingForLink(2L, 2L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Cannot find link with herbId=2 and illnessId=2", e.getDetails());
		}
		
		herb.getLinks().add(new LinkJPA(herb, illness));
	}
	
	@Test
	public void getOneRatingValidTest() {
		when(mockRatingsRepo.findByHerbAndIllness(Mockito.anyLong(), Mockito.anyLong())).thenReturn(link);
		
		try {
			RatingDTO result = mockRatingsServiceImpl.getRatingForLink(2L, 2L);
			
			assertTrue(result!=null);
			assertTrue(2L==result.getHerbId());
			assertTrue(2L==result.getIllnessId());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void getRatingsForHerbOrIllnessInvailidId1Test() {
		
		try {
			mockRatingsServiceImpl.getRatingsForHerbOrIllness(-1L, true);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("No herb found for that id", e.getDetails());
		}
	}
	
	@Test
	public void getRatingsForHerbOrIllnessInvailidId2Test() {
		
		try {
			mockRatingsServiceImpl.getRatingsForHerbOrIllness(-1L, false);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("No illness found for that id", e.getDetails());
		}
	}
	
	@Test
	public void getRatingsForHerbOrIllnessNoHerbFoundTest() {
		when(mockHerbRepo.getOne(Mockito.anyLong())).thenReturn(null);
		
		try {
			mockRatingsServiceImpl.getRatingsForHerbOrIllness(2L, true);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("No herb found for that id", e.getDetails());
		}
	}
	
	@Test
	public void getRatingsForHerbOrIllnessNoIllnessFoundTest() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(null);
		
		try {
			mockRatingsServiceImpl.getRatingsForHerbOrIllness(2L, false);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("No illness found for that id", e.getDetails());
		}
	}
	
	@Test
	public void getRatingsForHerbOrIllnessNoLinksForThisIllnessTest() {
		when(mockHerbRepo.getOne(Mockito.anyLong())).thenReturn(herb);
		
		herb.setLinks(new HashSet<LinkJPA>());
		
		try {
			mockRatingsServiceImpl.getRatingsForHerbOrIllness(2L, true);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("No herb-illness link exists", e.getDetails());
		}
		
		herb.getLinks().add(new LinkJPA(herb,illness));
	}
	
	@Test
	public void getRatingsForHerbOrIllnessNoLinksForThisHerbTest() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(illness);
		
		illness.setLinks(new HashSet<LinkJPA>());
		
		try {
			mockRatingsServiceImpl.getRatingsForHerbOrIllness(2L, false);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("No herb-illness link exists", e.getDetails());
		}
		
		illness.getLinks().add(new LinkJPA(herb,illness));
	}
	
	@Test
	public void getRatingsForHerbOrIllnessValidHerbTest() {
		when(mockHerbRepo.getOne(Mockito.anyLong())).thenReturn(herb);
		
		try {
			List<RatingDTO> result = mockRatingsServiceImpl.getRatingsForHerbOrIllness(2L, true);
			
			assertTrue(result!=null);
			assertTrue(result.size()==1);
			assertTrue(2L==result.get(0).getHerbId());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void getRatingsForHerbOrIllnessValidIllnessTest() {
		when(mockIllnessRepo.getOne(Mockito.anyLong())).thenReturn(illness);
		
		try {
			List<RatingDTO> result = mockRatingsServiceImpl.getRatingsForHerbOrIllness(2L, false);
			
			assertTrue(result!=null);
			assertTrue(result.size()==1);
			assertTrue(2L==result.get(0).getIllnessId());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
}