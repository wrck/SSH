package com.demo.ssh.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.demo.ssh.entity.Users;

@SuppressWarnings("deprecation")
@Repository
public class UsersRepository<T> {

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Users findOne(Long id) {
		Session session = null;
		Users user = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			user = (Users) session.get(Users.class, id);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return user;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Users> findAll() {
		String hsql = "from users";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(hsql);
		return query.list();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Users> getAllUser() {
		String hsql = "from users";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(hsql);
		return query.list();
	}

	public Serializable create(Users user) {
		Session session = null;
		Serializable serializable = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			serializable = session.save(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return serializable;
	}

	public <T> void delete(long id) {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			@SuppressWarnings("unchecked")
			T t = (T) session.get(Users.class, id);
			session.delete(t);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public Serializable delete(Users user) {
		Session session = null;
		Serializable serializable = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.delete(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return serializable;
	}

	public Serializable update(Users user) {

		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.update(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return user.getId();

	}

	public Serializable saveOrUpdate(Users user) {

		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.saveOrUpdate(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return user.getId();

	}

	public List<T> find(String sqlParams) {
		Session session = null;
		List<T> result = new ArrayList<T>();
		try {
			String hsql = "from users" + (sqlParams.isEmpty() ? "" : " where " + sqlParams);
			session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hsql);
			result = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) {
				session.getTransaction().rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return result;
	}
}
