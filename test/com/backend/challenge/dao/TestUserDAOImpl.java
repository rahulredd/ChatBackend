package com.backend.challenge.dao;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import com.backend.challenge.pojo.User;

public class TestUserDAOImpl {

	@Rule
	public final SessionFactoryRule sessionFactoryRule = new SessionFactoryRule();

	private static final String username = "testcases";
	private static final String password = "test123";
	
	//@Test
	public void testAddUser() {
		Session session = sessionFactoryRule.getSession();
		UserDAOImpl userDAOImpl = new UserDAOImpl();
		userDAOImpl.addUser(username, password, session);
		Assert.assertEquals(username, userDAOImpl.getUsers(session).get(0));
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddSameUserTwice() {
		Session session = sessionFactoryRule.getSession();
		UserDAOImpl userDAOImpl = new UserDAOImpl();
		userDAOImpl.addUser(username, password, session);
		
		session.beginTransaction();
		userDAOImpl.addUser(username, password, session);
		System.out.println(userDAOImpl.getUsers(session));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddUsersNullValues() {
		String username = null;
		String password = null;
		Session session = sessionFactoryRule.getSession();
		UserDAOImpl userDAOImpl = new UserDAOImpl();
		userDAOImpl.addUser(username, password, session);
	}

	@Test 
	public void testGetUsers() {
		Session session = sessionFactoryRule.getSession();
		UserDAOImpl userDAOImpl = new UserDAOImpl();
		userDAOImpl.addUser(username, password, session);
		
		session.beginTransaction();
		userDAOImpl.addUser("dummy", password, session);
		
		Assert.assertEquals(2, userDAOImpl.getUsers(session).size());
	}
	
	@Test
	public void deleteUser() {
		Session session = sessionFactoryRule.getSession();
		UserDAOImpl userDAOImpl = new UserDAOImpl();
	  userDAOImpl.addUser(username, password, session);
		
	  session.beginTransaction();
	  userDAOImpl.deleteUser(username, session);
	  System.out.println(userDAOImpl.getUsers(session));
		Assert.assertEquals(0, userDAOImpl.getUsers(session).size());
	}
}
