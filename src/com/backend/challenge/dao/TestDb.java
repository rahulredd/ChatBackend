package com.backend.challenge.dao;

import java.io.IOException;
import java.util.*;

import com.backend.challenge.pojo.Message;
import com.backend.challenge.utils.Utils;

public class TestDb {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		MessageDAOImpl messageDAO = new MessageDAOImpl();
//		UserDAOImpl user = new UserDAOImpl();
//		user.addUser("rahulisthenewdude:cooldude");
		//Message message = new Message("mirza khan", "rahul", "whastsup");
		//db.addUser("mirza khan : salim ");
		//messageDAO.sendMessage(message);
//		
		String sender = "santa";
		String receiver = "rahul";
		List<String> results = messageDAO.fetchMessages(sender, receiver, 0, 0);
		for (String result : results) {
			System.out.println(result);
		}
		
		
	}

}
