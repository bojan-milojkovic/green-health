package com.green.health;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.security.repositories.RoleRepository;
import com.green.health.security.repositories.UserSecurityRepository;
import com.green.health.user.dao.UserRepository;
import com.green.health.user.entities.UserDTO;
import com.green.health.user.entities.UserJPA;
import com.green.health.user.service.impl.UserServiceImpl;
import com.green.health.util.exceptions.MyRestPreconditionsException;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

	@Mock
	private UserRepository mockUserRepository;
	
	@Mock
	private RoleRepository mockRoleRepository;
	
	@Mock
	private UserSecurityRepository mockUserSecurityRepo;
	
	@InjectMocks
	private UserServiceImpl mockUserServiceimpl;
	
	private static List<UserJPA> list = new ArrayList<UserJPA>();
	private static UserDTO patchModel, postModel;

	@BeforeClass
	public static void init(){
		
		for(int i=0 ; i<4 ; i++){
			UserJPA jpa = new UserJPA();
			jpa.setEmail("bla_"+i+"@truc.com");
			jpa.setId((long)i);
			jpa.setFirstName("something_"+i);
			jpa.setRegistration(LocalDateTime.now());
			
			UserSecurityJPA usjpa = new UserSecurityJPA();
			usjpa.setUsername("username_"+i);
			usjpa.setUserJpa(jpa);
			jpa.setUserSecurityJpa(usjpa);
			
			list.add(jpa);
		}
		
		postModel = new UserDTO();
		postModel.setEmail("ja@gmail.com");
		postModel.setFirstName("Ja");
		postModel.setLastName("Jaja");
		postModel.setPassword("jajaja");
		postModel.setUsername("blanked");
		
		patchModel = new UserDTO();
		patchModel.setEmail("ti@gmail.com");
		patchModel.setUsername("blanked");
		patchModel.setId(20L);
	}
	
	@Test
	public void getAllUsersTest(){
		when(mockUserRepository.findAll()).thenReturn(list);
		
		List<UserDTO> result = mockUserServiceimpl.getAll();
		
		assertEquals(result.size(), list.size());
		
		for(int i=0 ; i<result.size() ; i++){
			assertEquals(result.get(i).getEmail(), list.get(i).getEmail());
			assertEquals(result.get(i).getId(), list.get(i).getId());
			assertEquals(result.get(i).getFirstName(), list.get(i).getFirstName());
		}
	}
	
	@Test
	public void findOneByUsernameTest(){
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(list.get(0).getUserSecurityJpa());
		
		try {
			UserDTO result = mockUserServiceimpl.getUserByUsernameOrEmail("username_0", null);
			
			assertEquals(result.getUsername(), list.get(0).getUserSecurityJpa().getUsername());
		} catch (MyRestPreconditionsException e) {
			fail();
		}
	}
	
	@Test
	public void findOneByEmailTest(){
		when(mockUserRepository.findByEmail(Mockito.anyString())).thenReturn(list.get(0));
		
		try {
			UserDTO result = mockUserServiceimpl.getUserByUsernameOrEmail(null, list.get(0).getEmail());
			
			assertEquals(result.getUsername(), list.get(0).getUserSecurityJpa().getUsername());
		} catch (MyRestPreconditionsException e) {
			fail();
		}
	}
	
	@Test
	public void findOneByIdTest(){
		when(mockUserRepository.getOne(Mockito.anyLong())).thenReturn(list.get(0));

		try {
			UserDTO result = mockUserServiceimpl.getOneById(1L);
			
			assertNotNull(result);
		
			assertEquals(result.getEmail(), list.get(0).getEmail());
		} catch (MyRestPreconditionsException e) {
			fail();
		}
	}
	
	@Test
	public void findUserByNegativeId(){
		try {
			mockUserServiceimpl.getOneById(-1L);
			fail();
		} catch (MyRestPreconditionsException e) {
			assertEquals(e.getDetails(), "Id is invalid");
		}
	}
	
	@Test
	public void findUserByNonExistingEmailTest(){
		when(mockUserRepository.findByEmail(Mockito.anyString())).thenReturn(null);
		
		try{
			mockUserServiceimpl.getUserByUsernameOrEmail(null, "ggg@ggg.com");
			fail();
		} catch(MyRestPreconditionsException e){
			assertEquals(e.getDetails(), "There is no user in our database with that email.");
		}
	}
	
	@Test
	public void findUserByInvalidEmailTest(){
		try{
			mockUserServiceimpl.getUserByUsernameOrEmail(null, "ggg@ggg");
			fail();
		} catch(MyRestPreconditionsException e){
			assertEquals(e.getDetails(), "You must provide a valid email address.");
		}
	}
	
	@Test
	public void findUserByNonExistingUsernameTest(){
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(null);
		
		try{
			mockUserServiceimpl.getUserByUsernameOrEmail("invalid", null);
			fail();
		} catch(MyRestPreconditionsException e){
			assertEquals(e.getDetails(), "There is no user in our database with that username.");
		}
	}
	
	@Test
	public void findUserWithoutEmailOrUsername(){
		try{
			mockUserServiceimpl.getUserByUsernameOrEmail(null,null);
			fail();
		} catch(MyRestPreconditionsException e){
			assertEquals(e.getDetails(), "When searching a user, you must provide at least one parameter - username or email.");
		}
	}
}