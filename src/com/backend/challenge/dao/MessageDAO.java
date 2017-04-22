package com.backend.challenge.dao;

import java.util.List;
import com.backend.challenge.pojo.Message;

public interface MessageDAO {

	public void sendMessage(Message message);
	public List<String> fetchMessages(String sender, String receiver, int messagesPerPage, int pageNum);
	public void deleteMessage(Message message);
}
