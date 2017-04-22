package com.backend.challenge.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import com.backend.challenge.pojo.Message;
import com.backend.challenge.pojo.User;
/**
 * Helps to setup a test db in-memory
 * @author Rahul
 *
 */
public class SessionFactoryRule implements MethodRule {
	private SessionFactory sessionFactory;
	private Transaction transaction;
	private Session session;

	@Override
	public Statement apply(final Statement statement, FrameworkMethod method, Object test) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				sessionFactory = createSessionFactory();
				createSession();
				beginTransaction();
				try {
					statement.evaluate();
				} finally {
					shutdown();
				}
			}
		};
	}
	private void shutdown() {
		try {
			try {
				session.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			sessionFactory.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private SessionFactory createSessionFactory() {
		Configuration configuration = new Configuration().configure();
		configuration.addAnnotatedClass(User.class).addAnnotatedClass(Message.class);
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		configuration.setProperty("hibernate.connection.url", "jdbc:h2:./data/db");
		configuration.setProperty("hibernate.hbm2ddl.auto", "create");
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		return sessionFactory;
	}

	public Session createSession() {
		session = sessionFactory.openSession();
		return session;
	}

	public void commit() {
		transaction.commit();
	}

	public void beginTransaction() {
		transaction = session.beginTransaction();
	}

	public Session getSession() {
		return session;
	}
}