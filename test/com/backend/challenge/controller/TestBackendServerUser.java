package com.backend.challenge.controller;

import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.backend.challenge.dao.UserDAOImpl;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import junit.framework.Assert;
	
public class TestBackendServerUser {
	
	private static final String username = "testBackendServerUser";
	private static final String password = "test123";
	UserDAOImpl userDAOImpl;
	
	@Before
	public void setup() {
		userDAOImpl = new UserDAOImpl();
	}

	@After
	public void shutdown() {
		userDAOImpl.deleteUser(username);
	}
	
	@Test
	public void testCreateUser() {
		String url = "http://localhost:8080/ASAPP/rest/backendController" + "/" + "createUser";
		String authString = username +  ":" + password;
		String authStringEnc = Base64.encodeBase64String(authString.getBytes());
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(url);
		ClientResponse resp = webResource.accept("text/plain").header("Authorization", "Basic " + authStringEnc).post(ClientResponse.class);
		Assert.assertTrue(userDAOImpl.getUsers().contains(username));
	}
}
