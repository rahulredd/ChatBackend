package com.backend.challenge.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.backend.challenge.pojo.User;
import com.backend.challenge.utils.DbUtils;
import com.backend.challenge.utils.Utils;

public class UserDAOImpl implements UserDAO {
	public static final Logger LOG = LoggerFactory.getLogger(UserDAOImpl.class);
	private static Session session;

	private static void beginSession() {
		session = DbUtils.getSessionFactory().openSession();
		session.beginTransaction();
	}

	public UserDAOImpl() {

	}

	public UserDAOImpl(Session session) {
		this.session = session;
	}

	@Override
	public void addUser(String username, String password) {
		beginSession();
		addUser(username, password, session);
		session.close();
	}

	public void addUser(String username, String password, Session session) {
		if (username == null || password == null) {
			throw new IllegalArgumentException("Please check the credentials");
		}
		String encryptedPassword = Utils.encrypt(password);
		User user = new User(username, encryptedPassword);
		try {
			session.save(user);
			session.getTransaction().commit();
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			LOG.error("Cannot save user", e);
		}
	}

	@Override
	public List<String> getUsers() {
		List<String> results = new ArrayList<String>();
		beginSession();
		results = getUsers(session);
		session.close();
		return results;
	}

	public List<String> getUsers(Session session) {
		List<String> results = new ArrayList<String>();
		String hql = "select username from User";
		Query query = null;
		try {
			query = session.createQuery(hql);
			results = query.list();
		} catch (HibernateException e) {
			LOG.error("Cannot execute query", e);
		} 
		return results;
	}

	@Override
	public void deleteUser(String username) {
		beginSession();
		deleteUser(username, session);
		session.close();
	}

	public void deleteUser(String username, Session session) {
		String hql = "delete from User where username='" + username + "'";
		Query query = null;
		try {
			query = session.createQuery(hql);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			LOG.error("Cannot delete user", e);
		} 
	}
}
