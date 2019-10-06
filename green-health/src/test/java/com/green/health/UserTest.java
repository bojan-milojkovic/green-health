package com.green.health;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.green.health.email.EmailUtil;
import com.green.health.security.entities.UserSecurityJPA;
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
	private UserSecurityRepository mockUserSecurityRepo;
	
	@Mock
	private EmailUtil mockEmailUtil;
	
	@InjectMocks
	private UserServiceImpl mockUserServiceimpl;
	
	private static List<UserJPA> list = new ArrayList<UserJPA>();
	private static UserDTO patchModel, postModel, passwordModel;

	@BeforeClass
	public static void init(){
		
		for(int i=0 ; i<4 ; i++){
			UserJPA jpa = new UserJPA();
			jpa.setEmail("bla_"+i+"@truc.com");
			jpa.setId((long)i);
			jpa.setFirstName("something_"+i);
			jpa.setRegistration(LocalDateTime.now());
			jpa.setPhone1("+38164377124"+i);
			jpa.setPhone2("+38164377125"+i);
			
			UserSecurityJPA usjpa = new UserSecurityJPA();
			usjpa.setId((long)i);
			usjpa.setUsername("username_"+i);
			usjpa.setUserHasRoles("ROLE_USER");
			usjpa.setUserJpa(jpa);
			usjpa.setPassword(BCrypt.hashpw("password_"+i, BCrypt.gensalt()));
			jpa.setUserSecurityJpa(usjpa);
			
			list.add(jpa);
		}
		
		postModel = new UserDTO();
		postModel.setEmail("ja@gmail.com");
		postModel.setFirstName("Ja");
		postModel.setLastName("Jaja");
		postModel.setPassword("jajaja");
		postModel.setUsername("blanked");
		postModel.setAddress1("add1");
		postModel.setCity("city");
		postModel.setCountry("country");
		postModel.setPostalCode("11000");
		postModel.setPhone1("+381643771247");
		
		patchModel = new UserDTO();
		patchModel.setEmail("ti@gmail.com");
		patchModel.setUsername("blanked");
		patchModel.setId(20L);
		
		passwordModel = new UserDTO();
		passwordModel.setId(2L);
		passwordModel.setUsername("blanked");
		passwordModel.setPassword("password");
		passwordModel.setNewPassword("password");
	}
	
	@Test
	public void getAllUsersTest(){
		when(mockUserRepository.findAll()).thenReturn(list);
		
		List<UserDTO> result = mockUserServiceimpl.getAll();
		
		assertEquals(result.size(), list.size());
		
		for(int i=0 ; i<result.size() ; i++){
			assertEquals(list.get(i).getUserSecurityJpa().getUsername(), result.get(i).getUsername());
			assertEquals(list.get(i).getId(), result.get(i).getId());
			assertEquals(list.get(i).getFirstName(), result.get(i).getFirstName());
		}
	}
	
	@Test
	public void findOneByUsernameTest(){
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(list.get(0).getUserSecurityJpa());
		
		try {
			UserDTO result = mockUserServiceimpl.getUserByUsernameOrEmail("username_0", null, null);
			
			assertEquals(list.get(0).getUserSecurityJpa().getUsername(), result.getUsername());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void findOneByEmailTest(){
		when(mockUserRepository.findByEmail(Mockito.anyString())).thenReturn(list.get(0));
		
		try {
			UserDTO result = mockUserServiceimpl.getUserByUsernameOrEmail(null, list.get(0).getEmail(), null);
			
			assertEquals(list.get(0).getUserSecurityJpa().getUsername(), result.getUsername());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void findOneByIdTest(){
		when(mockUserRepository.getOne(Mockito.anyLong())).thenReturn(list.get(0));

		try {
			mockUserServiceimpl.setCurrentUsername("username_0");
			UserDTO result = mockUserServiceimpl.getOneById(1L);
			
			assertNotNull(result);
		
			assertEquals(list.get(0).getEmail(), result.getEmail());
		} catch (MyRestPreconditionsException e) {
			fail(e.getDescription());
		}
	}
	
	@Test
	public void findUserByNegativeId(){
		try {
			mockUserServiceimpl.getOneById(-1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Id you entered is missing or invalid", e.getDetails());
		}
	}
	
	@Test
	public void findUserByNonExistingEmailTest(){
		when(mockUserRepository.findByEmail(Mockito.anyString())).thenReturn(null);
		
		try{
			mockUserServiceimpl.getUserByUsernameOrEmail(null, "ggg@ggg.com", null);
			fail("Exception expected");
		} catch(MyRestPreconditionsException e){
			assertEquals("There is no user in our database with that email.", e.getDetails());
		}
	}
	
	@Test
	public void findUserByInvalidEmailTest(){
		try{
			mockUserServiceimpl.getUserByUsernameOrEmail(null, "ggg@ggg", null);
			fail("Exception expected");
		} catch(MyRestPreconditionsException e){
			assertEquals("You must provide a valid email address.", e.getDetails());
		}
	}
	
	@Test
	public void findUserByNonExistingUsernameTest(){
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(null);
		
		try{
			mockUserServiceimpl.getUserByUsernameOrEmail("invalid", null, null);
			fail("Exception expected");
		} catch(MyRestPreconditionsException e){
			assertEquals("There is no user in our database with that username.", e.getDetails());
		}
	}
	
	@Test
	public void findUserWithoutEmailOrUsername(){
		try{
			mockUserServiceimpl.getUserByUsernameOrEmail(null,null, null);
			fail("Exception expected");
		} catch(MyRestPreconditionsException e){
			assertEquals("When searching a user, you must provide at least one parameter - username, phone or email.", e.getDetails());
		}
	}
	
	@Test
	public void addNewUserToDbNoPostDataPresentTest(){
		try{
			mockUserServiceimpl.addNew(patchModel);
			fail("Exception expected");
		} catch(MyRestPreconditionsException e){
			assertEquals("The following request form data is missing or invalid :", e.getDetails());
		}
	}
	
	@Test
	public void newUserUsernameAlreadyExistsTest(){
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(list.get(0).getUserSecurityJpa());
		
		try {
			mockUserServiceimpl.addNew(postModel);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Create user : Username blanked belongs to another user.", e.getDetails());
		}
	}
	
	@Test
	public void newUserEmailAlreadyExists(){
		when(mockUserRepository.findByEmail(Mockito.anyString())).thenReturn(list.get(0));
		doNothing().when(mockEmailUtil).confirmRegistration(Mockito.anyString(),Mockito.anyString(),Mockito.anyString());
		
		try {
			mockUserServiceimpl.addNew(postModel);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("User email ja@gmail.com belongs to another user.", e.getDetails());
		}
	}
	
	@Test
	public void newUsetPhone1AlreadyExists(){
		when(mockUserRepository.findByPhone(Mockito.anyString())).thenReturn(list.get(0));
		doNothing().when(mockEmailUtil).confirmRegistration(Mockito.anyString(),Mockito.anyString(),Mockito.anyString());
		
		try {
			mockUserServiceimpl.addNew(postModel);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("User phone number "+postModel.getPhone1()+" belongs to another user.", e.getDetails());
		}
	}
	
	@Test
	public void editUserTestIdsDontMatch(){
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(list.get(0).getUserSecurityJpa());
		
		try {
			mockUserServiceimpl.edit(patchModel, 20L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("You cannot edit someone else's user account.", e.getDetails());
		}
	}
	
	@Test
	public void editUserPatchDataNotPresent(){
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(list.get(1).getUserSecurityJpa());
		
		UserDTO badPatchModel = new UserDTO();
		badPatchModel.setUsername("username_1");
		badPatchModel.setId(1L);
		try {
			mockUserServiceimpl.edit(badPatchModel, 1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Your edit request is invalid - You must provide some editable data", e.getDetails());
		}
	}
	
	@Test
	public void editUserWithAnotherUsersEmail(){
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(list.get(1).getUserSecurityJpa());
		when(mockUserRepository.getOne(Mockito.anyLong())).thenReturn(list.get(1));
		when(mockUserRepository.findByEmail(Mockito.anyString())).thenReturn(list.get(2));
		
		UserDTO badPatchModel = new UserDTO();
		badPatchModel.setUsername("username_1");
		badPatchModel.setId(1L);
		badPatchModel.setEmail("bla_2@truc.com");
		
		try {
			mockUserServiceimpl.edit(badPatchModel, 1L);
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("User email bla_2@truc.com belongs to another user.", e.getDetails());
		}
	}
	
	
	@Test
	public void changePasswordWithNoData(){
		UserDTO badPatchModel = new UserDTO();
		badPatchModel.setId(1L);
		
		try {
			mockUserServiceimpl.changePassword(badPatchModel, "blanked");
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("request json is missing some elements.", e.getDetails());
		}
	}
	
	@Test
	public void changingPasswordForNonExistingUser() {
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(null);
		
		UserDTO badPatchModel = new UserDTO();
		badPatchModel.setId(1L);
		badPatchModel.setPassword("old");
		badPatchModel.setNewPassword("new");
		
		try {
			mockUserServiceimpl.changePassword(badPatchModel, "blanked");
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("User you are changing the password for does not exist.", e.getDetails());
		}
	}
	
	@Test
	public void tryingToChangeSomeoneElsesPasswordTest() {
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(list.get(3).getUserSecurityJpa());
		
		UserDTO badPatchModel = new UserDTO();
		badPatchModel.setId(1L);
		badPatchModel.setPassword("old");
		badPatchModel.setNewPassword("new");
		
		try {
			mockUserServiceimpl.changePassword(badPatchModel, "blanked");
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("You are trying to change someone elses's password", e.getDetails());
		}
	}
	
	@Test
	public void tryToChangePasswordOriginalPasswordDoesNotMatch(){
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(list.get(3).getUserSecurityJpa());
		
		UserDTO badPatchModel = new UserDTO();
		badPatchModel.setId(3L);
		badPatchModel.setPassword("old");
		badPatchModel.setNewPassword("new");
		
		try {
			mockUserServiceimpl.changePassword(badPatchModel, "blanked");
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Your entry for original password does not match with the DB value", e.getDetails());
		}
	}
	
	@Test
	public void tryToChangePasswordButNewValueIsSameAsOriginal() {
		when(mockUserSecurityRepo.findByUsername(Mockito.anyString())).thenReturn(list.get(3).getUserSecurityJpa());
		
		UserDTO badPatchModel = new UserDTO();
		badPatchModel.setId(3L);
		badPatchModel.setPassword("password_3");
		badPatchModel.setNewPassword("password_3");
		
		try {
			mockUserServiceimpl.changePassword(badPatchModel, "blanked");
			fail("Exception expected");
		} catch (MyRestPreconditionsException e) {
			assertEquals("Old password and new password should be different.", e.getDetails());
		}
	}
}