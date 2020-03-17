package com.michaloruba.obslugasesji.dao;

import com.michaloruba.obslugasesji.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private EntityManager entityManager;

	@Autowired
	public UserRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<User> findAll() {
		Session session = entityManager.unwrap(Session.class);
		Query<User> theQuery = session.createQuery("from User", User.class);

		return theQuery.getResultList();
	}

	@Override
	public User findByUserName(String theUserName) {
		Session currentSession = entityManager.unwrap(Session.class);

		Query<User> theQuery = currentSession.createQuery("from User where userName=:uName", User.class);
		theQuery.setParameter("uName", theUserName);
		User theUser;
		try {
			theUser = theQuery.getSingleResult();
		} catch (Exception e) {
			theUser = null;
		}

		return theUser;
	}

	@Override
	public void save(User theUser) {
		// get current hibernate session
		Session currentSession = entityManager.unwrap(Session.class);

		currentSession.saveOrUpdate(theUser);
	}

	@Override
	public void deleteByUserName(String userName) {
		Session session = entityManager.unwrap(Session.class);
		session.delete(findByUserName(userName));
	}

}
