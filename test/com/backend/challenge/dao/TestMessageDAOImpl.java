package com.backend.challenge.dao;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import com.backend.challenge.pojo.Message;
/**
 * Uses the SessionFactoryRule to create a in-memory test db
 * Tests the methods in MessageDAOImpl class
 * @author Rahul
 *
 */
public class TestMessageDAOImpl {
	
	@Rule
	public final SessionFactoryRule sessionFactoryRule = new SessionFactoryRule();

	private static final String sender = "testMessageDAOImplSender";
	private static final String receiver = "testMessageDAOImplReceiver";
	private static final String password = "test123";

	private void addUsers(Session session) {
		UserDAOImpl userDAOImpl = new UserDAOImpl();
		userDAOImpl.addUser(sender, password, session);
		
		session.beginTransaction();
		userDAOImpl.addUser(receiver, password, session);
	}

	@Test
	public void testSendMessage() {
		Session session = sessionFactoryRule.getSession();
		addUsers(session);
		
		String content = "Hey This is an awesome challenge";
		session.beginTransaction();
		Message message = new Message(sender, receiver, content);
		MessageDAOImpl messageDAOImpl = new MessageDAOImpl();
		messageDAOImpl.sendMessage(message, session);
		
		Assert.assertTrue(content.equals(messageDAOImpl.fetchMessages(sender, receiver, 0, 0, session).get(0)));
	}
	
	@Test
	public void testFetchMessages() {
		Session session = sessionFactoryRule.getSession();
		addUsers(session);
		
		session.beginTransaction();
		MessageDAOImpl messageDAOImpl = new MessageDAOImpl();

		String content = "Hey This is an awesome challenge1";
		Message message = new Message(sender, receiver, content);
		messageDAOImpl.sendMessage(message, session);

		session.beginTransaction();
		content = "Hey This is an awesome challenge2";
		message = new Message(sender, receiver, content);
		messageDAOImpl.sendMessage(message, session);

		Assert.assertEquals(2, messageDAOImpl.fetchMessages(sender, receiver, 0, 0, session).size());
	}
	
	private void sendMessages(Session session) {
		MessageDAOImpl messageDAOImpl = new MessageDAOImpl();
		String content = "Hey This is an awesome challenge";
		for (int i = 0; i < 25; i++) {
			content += i;
			session.beginTransaction();
			Message message = new Message(sender, receiver, content);
			messageDAOImpl.sendMessage(message, session);
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFetchMessagesNegativeArguments() {
		Session session = sessionFactoryRule.getSession();
		addUsers(session);
		
		sendMessages(session);
		MessageDAOImpl messageDAOImpl = new MessageDAOImpl();
		messageDAOImpl.fetchMessages(sender, receiver, -10, -10);
	}
	
	@Test
	public void testFetchMessagesWithPagination() {
		Session session = sessionFactoryRule.getSession();
		addUsers(session);
		
		sendMessages(session);
		MessageDAOImpl messageDAOImpl = new MessageDAOImpl();
		
		//25 messages 
		Assert.assertEquals(25, messageDAOImpl.fetchMessages(sender, receiver, 0, 0, session).size());

		//5 entries per page - page 1
		Assert.assertEquals(5, messageDAOImpl.fetchMessages(sender, receiver, 5, 1, session).size());
		
		//10 entries per page - page 2
		Assert.assertEquals(10, messageDAOImpl.fetchMessages(sender, receiver, 10, 2, session).size());

		//10 entries per page - page 3 => returns only 5. Messages per page - 10, 10, 5
		Assert.assertEquals(5, messageDAOImpl.fetchMessages(sender, receiver, 10, 3, session).size());

		//10 entries per page - page 4 => returns 5 as only 3 pages with messages per page 10. If pageNum > pages returns messages of last page
		Assert.assertEquals(5, messageDAOImpl.fetchMessages(sender, receiver, 10, 3, session).size());
	}
	
	@Test
	public void testDeleteMessages() {
		Session session = sessionFactoryRule.getSession();
		addUsers(session);
		
		session.beginTransaction();
		MessageDAOImpl messageDAOImpl = new MessageDAOImpl();

		String content = "Hey This is an awesome challenge";
		Message message = new Message(sender, receiver, content);
		messageDAOImpl.sendMessage(message, session);

		session.beginTransaction();
		message = new Message(sender, receiver, content);
		messageDAOImpl.sendMessage(message, session);
		
		Assert.assertEquals(2, messageDAOImpl.fetchMessages(sender, receiver, 0, 0, session).size());
		
		session.beginTransaction();
		messageDAOImpl.deleteMessage(message, session);
		
		Assert.assertEquals(0, messageDAOImpl.fetchMessages(sender, receiver, 0, 0, session).size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteMessagesWithoutUsers() {
		Session session = sessionFactoryRule.getSession();
		MessageDAOImpl messageDAOImpl = new MessageDAOImpl();

		String content = "Hey This is an awesome challenge";
		Message message = new Message(sender, receiver, content);
		messageDAOImpl.sendMessage(message, session);
		
		Assert.assertEquals(1, messageDAOImpl.fetchMessages(sender, receiver, 0, 0, session).size());
		
		session.beginTransaction();
		messageDAOImpl.deleteMessage(message, session);
	}
}
