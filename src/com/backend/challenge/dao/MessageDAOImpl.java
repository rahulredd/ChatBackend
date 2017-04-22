package com.backend.challenge.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.backend.challenge.pojo.Message;
import com.backend.challenge.utils.DbUtils;

public class MessageDAOImpl implements MessageDAO {
	public static final Logger LOG = LoggerFactory.getLogger(MessageDAO.class);
	private static Session session;

	private static void beginSession() {
		session = DbUtils.getSessionFactory().openSession();
		session.beginTransaction();
	}

	@Override
	public void sendMessage(Message message) {
		beginSession();
		sendMessage(message, session);
		session.close();
	}

	public void sendMessage(Message message, Session session) {
		String sender = message.getSender();
		String receiver = message.getReceiver();
		if (!isUserExists(sender, session) || !isUserExists(receiver, session)) {
			throw new IllegalArgumentException("Sender or receiver not present in the system");
		}
		try {
			session.save(message);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			LOG.error("Cannot save the message", e);
		}
	}

	private boolean isUserExists(String username, Session session) {
		String hql = "select id from User where username='" + username + "'";
		Query query = null;
		try {
			query = session.createQuery(hql);
		} catch (HibernateException e) {
			LOG.error("Cannot execute query", e);
		} 
		if (query != null) {
			if (query.list().size() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> fetchMessages(String sender, String receiver, int messagesPerPage, int pageNum) {
		if (messagesPerPage < 0) {
			throw new IllegalArgumentException("messagePerPage should be positive integer");
		}
		if (pageNum < 0) {
			throw new IllegalArgumentException("pageNum should be positive integer");
		}
		List<String> results = new ArrayList<String>();
		beginSession();
		results = fetchMessages(sender, receiver, messagesPerPage, pageNum, session);
		session.close();
		return results;
	}

	public List<String> fetchMessages(String sender, String receiver, int messagesPerPage, int pageNum, Session session) {
		String hql = "select content from Message where sender='" + sender + "'" + "and receiver='" + receiver + "'";
		Query query = null;
		int lastPage = 1;
		int pageStart = 0;
		List<String> results = new ArrayList<String>();
		try {
			query = session.createQuery(hql);
			int totalSize =  query.list().size();
			if (messagesPerPage > 0) {
				lastPage = (totalSize / messagesPerPage) + 1;
			} else if (messagesPerPage == 0) {
				messagesPerPage = totalSize;
			}
			pageNum = pageNum > lastPage ? lastPage : pageNum;
			if (pageNum > 0) {
				pageStart = (pageNum - 1) * (messagesPerPage);
			}
			query.setFirstResult(pageStart);
			query.setMaxResults(messagesPerPage);
			results = query.list();
		} catch (HibernateException e) {
			LOG.error("Cannot execute query", e);
		}
		return results;
	}

	@Override
	public void deleteMessage(Message message) {
		beginSession();
		deleteMessage(message, session);
		session.close();
	}

	public void deleteMessage(Message message, Session session) {
		String sender = message.getSender();
		String receiver = message.getReceiver();
		String content = message.getContent();
		String hql = "delete from Message where sender='" + sender + "' and receiver='" + receiver + "' and content='" + content + "'";
		Query query = null;
		try {
			query = session.createQuery(hql);
			int deletedRows = query.executeUpdate();
			if (deletedRows == 0) {
				throw new IllegalArgumentException("Sender or Receiver not present");
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			LOG.error("Cannot delete message", e);
		} 
	} 
}
