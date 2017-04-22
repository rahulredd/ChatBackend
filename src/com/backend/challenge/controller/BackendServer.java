package com.backend.challenge.controller;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.backend.challenge.dao.MessageDAOImpl;
import com.backend.challenge.dao.UserDAOImpl;
import com.backend.challenge.pojo.Message;
import com.backend.challenge.utils.StringList;
import com.backend.challenge.utils.Utils;
/**
 * A server that handles incoming requests
 * @author Rahul
 *
 */
@Path("/backendController")
public class BackendServer {

	public static final Logger LOG = LoggerFactory.getLogger(BackendServer.class);
	
	/**
	 * Accepts the header param that contains username and encrypted password
	 * Decodes the string and calls the UserDAOImpl class to save the user in db 
	 *   
	 * @param authString
	 * @return String
	 */
	
	@POST
	@Path("/createUser")
	@Produces(MediaType.TEXT_PLAIN)
	public String createUser(@HeaderParam("authorization") String authString) {
		String decodedAuth = "";
		String[] authParts = authString.split("\\s+");
		String authInfo = authParts[1];
		byte[] bytes = Base64.decodeBase64(authInfo);
		decodedAuth = new String(bytes);
		String credentials[] = decodedAuth.split(":");
		String username = credentials[0];
		String password = credentials[1];
		UserDAOImpl user = new UserDAOImpl();
		user.addUser(username, password);
		return decodedAuth;
	}
	
	/**
	 * Fetches messages from the db. Arguments are passed in the URL
	 * @param sender
	 * @param receiver
	 * @param messagesPerPage
	 * @param pageNum
	 * @return List of messages
	 */
	
	@GET
	@Path("/fetchMessages/{sender}/{receiver}/{messagesPerPage}/{pageNum}")
	@Consumes({MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_XML})
	public StringList fetchMessages(@PathParam("sender") String sender, @PathParam("receiver") String receiver,
																	@PathParam("messagesPerPage") int messagesPerPage, @PathParam("pageNum") int pageNum) {
		MessageDAOImpl messageDAO = new MessageDAOImpl();
		List<String> results = messageDAO.fetchMessages(sender, receiver, messagesPerPage, pageNum);
		return new StringList(results);
	}
	
	/**
	 * Accepts the Message object. Processes the content of message as video or text or image
	 * Calls the MessageDAO and stores the message in the db
	 * @param message
	 * @return Response
	 */
	
	@POST
	@Path("/sendMessage")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response sendMessage(Message message) {
		MessageDAOImpl messageDAO = new MessageDAOImpl();
		String content = message.getContent();
		try {
			content = Utils.processContent(content);
			message.setContent(content);
		} catch (IOException e) {
			LOG.error("Unable to retrieve content-type", e);
		}
		messageDAO.sendMessage(message);
		String result = message.getSender() + " " + message.getReceiver();
		return Response.status(200).entity(result).build();
	}
}
