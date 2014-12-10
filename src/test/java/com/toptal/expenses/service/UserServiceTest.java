package com.toptal.expenses.service;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;

import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.toptal.expenses.exception.EntityAlreadyExistException;
import com.toptal.expenses.model.LoggedUser;
import com.toptal.expenses.model.User;
import com.toptal.expenses.persistence.UserPersistenceService;

public class UserServiceTest {

	private UserService userService;
	@Mock
	private UserPersistenceService userPersistentService;

	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.userService = new UserService(userPersistentService);
	}

	@Test
	public void create() {
		InOrder inOrder = inOrder(userPersistentService);
		User user = this.userService.create("username", "password", "password", "firstName", "lastName");
		assertNotNull(user);
		inOrder.verify(userPersistentService).exists(eq("username"));
		inOrder.verify(userPersistentService).create(eq(user));
		inOrder.verifyNoMoreInteractions();
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void create_differentPasswords() {
		this.userService.create("username", "password", "verifyPassword", "firstName", "lastName");
	}

	@Test(expectedExceptions = EntityAlreadyExistException.class)
	public void create_alreadyExist() {
		when(this.userPersistentService.exists(eq("username"))).thenReturn(true);
		this.userService.create("username", "password", "password", "firstName", "lastName");
	}

	@Test
	public void authenticate() {
		InOrder inOrder = inOrder(userPersistentService);
		User user = mock(User.class);
		when(userPersistentService.getUser(eq("username"))).thenReturn(user);
		LoggedUser loggedUser = this.userService.authenticate("username", "password");
		assertNotNull(loggedUser);
		verify(user).authenticate(eq("password"));
		inOrder.verify(userPersistentService).getUser(eq("username"));
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void getUser() {
		InOrder inOrder = inOrder(userPersistentService);
		this.userService.getUser("username");
		inOrder.verify(userPersistentService).getUser(eq("username"));
		inOrder.verifyNoMoreInteractions();
	}
}
