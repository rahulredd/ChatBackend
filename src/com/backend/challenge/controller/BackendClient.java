package com.backend.challenge.controller;

import com.backend.challenge.pojo.Message;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.codec.binary.Base64;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * BackendClient which sends requests to the BackendServer.
 * Reads the inputs from cfg.properties
 */
public class BackendClient {
	public static final Logger LOG = LoggerFactory.getLogger(BackendClient.class);
	private static Properties props;
	private static final String PROPS_FILENAME = "cfg.properties";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String SENDER = "sender";
	private static final String RECEIVER = "receiver";
	private static final String CONTENT = "content";
	private static final String BASE_URL = "http://localhost:8080/ASAPP/rest/backendController";
	private static final String CREATEUSER_URL = "createUser";
	private static final String SENDMESSAGE_URL = "sendMessage";
	private static final String FETCHMESSAGE_URL = "fetchMessages";
	private static final int MESSAGES_PER_PAGE = 0;
	private static final int PAGE_NUM = 0;

	static Properties getProperties() {
		props = new Properties();
		try {
			props.load(new FileInputStream(PROPS_FILENAME));
		} catch (FileNotFoundException e ) {
			LOG.error("Unable to load Props", e);
		} catch (IOException e) {
			LOG.error("Unable to load Props", e);
		}
		return props;
	}
  
	/**
	 * Sends a request to the server to create a user. 
	 * The password is encrypted and the parameters are sent in the header
	 * @param username
	 * @param password
	 * 
	 */
	public void createUser(String username, String password) {
		String url = BASE_URL + "/" + CREATEUSER_URL;
		String authString = username + ":" + password;
		String authStringEnc = Base64.encodeBase64String(authString.getBytes());
		LOG.info("Base64 encoded auth string: ", authStringEnc);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(url);
		ClientResponse resp = webResource.accept("text/plain").header("Authorization", "Basic " + authStringEnc).post(ClientResponse.class);
		if(resp.getStatus() != 200) {
			LOG.error("User already present ");
		} else {
			String output = resp.getEntity(String.class);
			LOG.info("Added user " + output);
		}
	}

	public void fetchMessages(String sender, String receiver) {
		fetchMessages(sender, receiver, MESSAGES_PER_PAGE, PAGE_NUM);
	}
	
	/**
	 * Fetches the messages between the sender and receiver with pagination
	 * @param sender
	 * @param receiver
	 * @param messagesPerPage
	 * @param pageNum
	 */

	public void fetchMessages(String sender, String receiver, int messagesPerPage, int pageNum) {
		if (messagesPerPage < 0) {
			throw new IllegalArgumentException("messagePerPage should be a positive integer");
		}
		if (pageNum < 0) {
			throw new IllegalArgumentException("pageNum should be a positive integer");
		}
		String url = BASE_URL + "/" + FETCHMESSAGE_URL +  "/" + sender + "/" + receiver + "/" + messagesPerPage + "/" + pageNum;
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(url);
		ClientResponse resp = webResource.accept("application/xml").get(ClientResponse.class);
		if(resp.getStatus() != 200) {
			LOG.error("Unable to fetch messages");
		} else {
			String output = resp.getEntity(String.class);
			List<String> messages = convertToXml(output);
			System.out.println(messages);
		}
	}
	
  /**
   * Takes a sender, recipient, and message and saves that to the data store
   * @param sender
   * @param receiver
   * @param content
   */
	public void sendMessage(String sender, String receiver, String content) {
		String url = BASE_URL + "/" + SENDMESSAGE_URL;
		Message message = new Message(sender, receiver, content);
		Client restClient = Client.create();
		WebResource webResource = restClient.resource(url);
		ClientResponse resp = webResource.accept("application/xml").entity(message).post(ClientResponse.class);
		if(resp.getStatus() != 200) {
			LOG.error("Unable to send the message");
		} else {
			String output = resp.getEntity(String.class);
			LOG.info("Message sent " + output);
		}
	}

	private static List<String> convertToXml(String output) {
		List<String> results = new ArrayList<String>();
		SAXBuilder saxBuilder = new SAXBuilder();
		try {
			Document doc = saxBuilder.build(new StringReader(output));
			Element elements = doc.getRootElement();
			List<Element> list = elements.getChildren("data");
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {
				Element node = list.get(i);
				results.add(node.getText());
			}
		} catch (JDOMException e) {
			LOG.error("Cannot read the xml",e);
		} catch (IOException e) {
			LOG.error("Cannot read the xml", e);
		}
		return results;
	}

	public static void main(String[] args) throws IOException {
		String username = getProperties().getProperty(USERNAME);
		String password = getProperties().getProperty(PASSWORD);
		String sender = getProperties().getProperty(SENDER);
		String receiver = getProperties().getProperty(RECEIVER);
		String content = getProperties().getProperty(CONTENT);

		BackendClient client = new BackendClient();
		LOG.info("Creating user - " + username);

		client.createUser(username, password);

		LOG.info("Sending message from " + sender + " " + receiver);
		client.sendMessage(sender, receiver, content);

		LOG.info("Fectching messages between " + sender + " " + receiver);
		client.fetchMessages(sender, receiver);
	}
}
