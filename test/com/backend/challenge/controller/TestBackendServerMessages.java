package com.backend.challenge.controller;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.backend.challenge.dao.MessageDAOImpl;
import com.backend.challenge.dao.UserDAOImpl;
import com.backend.challenge.pojo.Message;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import junit.framework.Assert;

public class TestBackendServerMessages {
	private static final String BASE_URL = "http://localhost:8080/ASAPP/rest/backendController";
	private static final String sender = "testSender";
	private static final String receiver = "testReceiver";
	private static final String password = "testUsers";
	MessageDAOImpl messageDAOImpl;
	Message message;
	UserDAOImpl userDAOImpl;

	@Before
	public void setup() {
		String content = "sending a test message";
		message = new Message(sender, receiver, content);
		messageDAOImpl = new MessageDAOImpl();
		userDAOImpl = new UserDAOImpl();
		addUsers();
	}

	@After
	public void shutdown() {
		userDAOImpl.deleteUser(sender);
		userDAOImpl.deleteUser(receiver);
		messageDAOImpl.deleteMessage(message);
	}

	@Test
	public void testSendMessage() {
		String url = BASE_URL + "/" + "sendMessage";
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(url);
		ClientResponse resp = webResource.accept("application/xml").entity(message).post(ClientResponse.class);
		if(resp.getStatus() != 200) {
			Assert.fail("Unable to send the message");
		} 
		Assert.assertEquals(1, messageDAOImpl.fetchMessages(sender, receiver, 0, 0).size());
	}
	
	@Test
	public void testFetchMessages() {
		messageDAOImpl.sendMessage(message);
		String url = BASE_URL + "/" + "fetchMessages" + "/" + sender + "/" + receiver + "/" + 0 + "/" + 0;
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(url);
		ClientResponse resp = webResource.accept("application/xml").get(ClientResponse.class);
		if(resp.getStatus() != 200) {
			Assert.fail("Unable to fetch messages");
		}
		Assert.assertEquals(1, messageDAOImpl.fetchMessages(sender, receiver, 0, 0).size());
	}
	
	private void addUsers() {
		userDAOImpl.addUser(sender, password);
		userDAOImpl.addUser(receiver, password);
	}
}
