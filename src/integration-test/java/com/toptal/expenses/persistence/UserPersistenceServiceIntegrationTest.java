package com.toptal.expenses.persistence;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.toptal.expenses.AbstractIntegrationTest;
import com.toptal.expenses.exception.NotFoundException;
import com.toptal.expenses.model.User;

public class UserPersistenceServiceIntegrationTest extends AbstractIntegrationTest {

	private UserPersistenceService userPersistenceService;

	@BeforeClass
	public void setup() {
		this.userPersistenceService = this.getInstance(UserPersistenceService.class);
	}

	@Test(expectedExceptions = NotFoundException.class)
	public void get_not_found() {
		this.userPersistenceService.getUser("jasantoro");
	}

	@Test
	public void create() {
		assertFalse(this.userPersistenceService.exists("jasantoro"));
		this.userPersistenceService.create(new User("jasantoro", "password", "Jorge", "Santoro"));
		assertTrue(this.userPersistenceService.exists("jasantoro"));
		this.userPersistenceService.getUser("jasantoro");
	}

	@Test
	public void delete() {
		assertFalse(this.userPersistenceService.exists("jasantoro"));
		this.userPersistenceService.create(new User("jasantoro", "password", "Jorge", "Santoro"));
		User user = this.userPersistenceService.getUser("jasantoro");
		this.userPersistenceService.delete(user);
		assertFalse(this.userPersistenceService.exists("jasantoro"));
	}

	@Test
	public void update() {
		assertFalse(this.userPersistenceService.exists("jasantoro"));
		this.userPersistenceService.create(new User("jasantoro", "password", "Jorge", "Santoro"));
		User user = this.userPersistenceService.getUser("jasantoro");
		user.setFirstName("Juan");
		this.userPersistenceService.update(user);
		user = this.userPersistenceService.getUser("jasantoro");
		assertEquals(user.getFirstName(), "Juan");
	}
}
