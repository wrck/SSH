package com.demo.ssh.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class HibernateRepository extends HibernateDaoSupport {

	/**
	 * 
	 * @param entity
	 * @param id
	 * @return 根据ID获取对象. 实际调用Hibernate的session.load()方法返回实体或其proxy对象.
	 *         如果对象不存在，抛出异常.
	 */
	public <T> T findOne(Class<T> entity, Serializable id) {
		return (T) this.getHibernateTemplate().get(entity, id);
	}

	/**
	 * 
	 * @param entity
	 * @return 全部对象
	 */
	public <T> List<T> findAll(Class<T> entity) {
		return this.getHibernateTemplate().loadAll(entity);
	}

	/**
	 * 
	 * @param entity
	 * @param orderBy
	 * @param isAsc
	 * @return 全部对象，带排序
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> entity, String orderBy, boolean isAsc) {
		Assert.hasText(orderBy);
		if (isAsc) {
			return (List<T>) this.getHibernateTemplate()
					.findByCriteria(DetachedCriteria.forClass(entity).addOrder(Order.asc(orderBy)));
		}
		return (List<T>) this.getHibernateTemplate()
				.findByCriteria(DetachedCriteria.forClass(entity).addOrder(Order.desc(orderBy)));
	}

	/**
	 * 
	 * @param entity
	 * @return id
	 */
	public <T> Serializable create(T entity) {
		return this.getHibernateTemplate().save(entity);
	}

	/**
	 * 
	 * @param entity
	 */
	public void update(Object entity) {
		this.getHibernateTemplate().update(entity);
	}

	/**
	 * 
	 * @param entity
	 */
	public void delete(Object entity) {
		this.getHibernateTemplate().delete(entity);
	}

	public void flush() {
		this.getHibernateTemplate().flush();
	}

	public void clear() {
		this.getHibernateTemplate().clear();
	}

	/**
	 * 创建Query对象.
	 * 对于要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设
	 * 留意可以连续设置,如下
	 * 
	 * <pre>
	 * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
	 * </pre>
	 * 
	 * 调用方式如下
	 * 
	 * <pre>
	 *        dao.createQuery(hql)
	 *        dao.createQuery(hql,arg0);
	 *        dao.createQuery(hql,arg0,arg1);
	 *        dao.createQuery(hql,new Object[arg0,arg1,arg2])
	 * </pre>
	 * 
	 * @param values
	 *            可变参数.
	 */
	public Query createQuery(String hql, Object... values) {
		Assert.hasText(hql);
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

	/**
	 * 创建Query对象.防止注入
	 * 
	 * @param hql
	 *            查询语句
	 * @param values
	 *            list集合
	 * @return
	 */
	public Query createQuery(String hql, List<Object> values) {
		Assert.hasText(hql);
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				query.setParameter(i, values.get(i));
			}
		}
		return query;
	}

	/**
	 * 创建Query对象.防止注入
	 * 
	 * @param hql
	 *            查询语句
	 * @param values
	 *            list集合
	 * @return
	 */
	public Query createSQLQuery(String hql, List<Object> values) {
		Assert.hasText(hql);
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				query.setParameter(i, values.get(i));
			}
		}
		return query;
	}

	/**
	 * 创建Criteria对象.
	 * 
	 * @param criterions
	 *            可变的Restrictions条件列表,见{@link #createQuery(String,Object...)}
	 */
	public <T> Criteria createCriteria(Class<T> entityClass, Criterion... criterions) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 创建Criteria对象，带排序字段与升降序字段.
	 * 
	 * @see #createCriteria(Class,Criterion[])
	 */
	public <T> Criteria createCriteria(Class<T> entityClass, String orderBy, boolean isAsc, Criterion... criterions) {
		Assert.hasText(orderBy);

		Criteria criteria = createCriteria(entityClass, criterions);

		if (isAsc)
			criteria.addOrder(Order.asc(orderBy));
		else
			criteria.addOrder(Order.desc(orderBy));

		return criteria;
	}

	/**
	 * 根据hql查询,直接使用HibernateTemplate的find函数.
	 * 
	 * @param values
	 *            可变参数,见{@link #createQuery(String,Object...)}
	 */
	public List find(String hql, Object... values) {
		Assert.hasText(hql);
		return this.getHibernateTemplate().find(hql, values);
	}

	/**
	 * 根据属名和属性查询对
	 * 
	 * @return 符合条件的对象列
	 */
	public <T> List<T> findBy(Class<T> entityClass, String propertyName, Object value) {
		Assert.hasText(propertyName);
		return createCriteria(entityClass, Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 根据属名和属性查询对,带排序参
	 */
	public <T> List<T> findBy(Class<T> entityClass, String propertyName, Object value, String orderBy, boolean isAsc) {
		Assert.hasText(propertyName);
		Assert.hasText(orderBy);
		return createCriteria(entityClass, orderBy, isAsc, Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 根据属名和属性查询唯对象.
	 * 
	 * @return 符合条件的唯对象 or null if not found.
	 */
	public <T> T findUniqueBy(Class<T> entityClass, String propertyName, Object value) {
		Assert.hasText(propertyName);
		return (T) createCriteria(entityClass, Restrictions.eq(propertyName, value)).uniqueResult();
	}

	/**
	 * 分页查询函数，使用hql.
	 * 
	 * @param <T>
	 * 
	 * @param pageNo
	 *            页号
	 */
	public Map<String, Object> pagedQuery(String hql, int pageNo, int pageSize, Object... values) {
		Assert.hasText(hql);
		Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
		// Count查询
		String countQueryString = " select count (*) " + removeSelect(removeOrders(hql));
		List countlist = this.getHibernateTemplate().find(countQueryString, values);
		long totalCount = (Long) countlist.get(0);

		// if (totalCount < 1)
		// return new Page();
		// 实际查询返回分页对象
		// int startIndex = Page.getStartOfPage(pageNo, pageSize);
		Query query = createQuery(hql, values);
		List list = query.setFirstResult((pageNo - 1) * pageSize + 1).setMaxResults(pageSize).list();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pageNo", pageNo);
		map.put("pageSize", pageSize);
		map.put("totalCount", totalCount);
		map.put("result", list);
		return map;
	}

	/**
	 * 分页查询函数，使用已设好查询条件与排序的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号
	 * @return 含记录数和当前页数据的Page对象.
	 */
	/*
	 * public Page pagedQuery(Criteria criteria, int pageNo, int pageSize) {
	 * Assert.notNull(criteria); Assert.isTrue(pageNo >= 1,
	 * "pageNo should start from 1"); CriteriaImpl impl = (CriteriaImpl)
	 * criteria;
	 * 
	 * Projection projection = impl.getProjection();
	 * List<CriteriaImpl.OrderEntry> orderEntries; try { orderEntries = (List)
	 * BeanUtils.forceGetProperty(impl, "orderEntries");
	 * BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList()); }
	 * catch (Exception e) { throw new InternalError(
	 * " Runtime Exception impossibility throw "); }
	 * 
	 * // 执行查询 long totalCount = (Long)
	 * criteria.setProjection(Projections.rowCount()).uniqueResult();
	 * 
	 * // 将之前的Projection和OrderBy条件重新设回 criteria.setProjection(projection); if
	 * (projection == null) {
	 * criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY); }
	 * 
	 * try { BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries); }
	 * catch (Exception e) { throw new InternalError(
	 * " Runtime Exception impossibility throw "); }
	 * 
	 * // 返回分页对象 if (totalCount < 1) return new Page();
	 * 
	 * int startIndex = Page.getStartOfPage(pageNo, pageSize); List list =
	 * criteria.setFirstResult(startIndex).setMaxResults(pageSize).list();
	 * return new Page(startIndex, totalCount, pageSize, list); }
	 * 
	 *//**
		 * 分页查询函数，根据entityClass和查询条件参数创建默认的<code>Criteria</code>.
		 * 
		 * @param pageNo
		 *            页号
		 * @return 含记录数和当前页数据的Page对象.
		 */
	/*
	 * public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
	 * Criterion... criterions) { Criteria criteria =
	 * createCriteria(entityClass, criterions); return pagedQuery(criteria,
	 * pageNo, pageSize); }
	 * 
	 *//**
		 * 分页查询函数，根据entityClass和查询条件参,排序参数创建默认<code>Criteria</code>.
		 * 
		 * @param pageNo
		 *            页号
		 * @return 含记录数和当前页数据的Page对象.
		 *//*
		 * public Page pagedQuery(Class entityClass, int pageNo, int pageSize,
		 * String orderBy, boolean isAsc, Criterion... criterions) { Criteria
		 * criteria = createCriteria(entityClass, orderBy, isAsc, criterions);
		 * return pagedQuery(criteria, pageNo, pageSize); }
		 */

	/**
	 * 判断对象某些属的值在数据库中是否唯一.
	 * 
	 * @param uniquePropertyNames
	 *            在POJO里不能重复的属列,以号分割"name,loginid,password"
	 */
	public <T> boolean isUnique(Class<T> entityClass, Object entity, String uniquePropertyNames) {
		Assert.hasText(uniquePropertyNames);
		Criteria criteria = createCriteria(entityClass).setProjection(Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// 循环加入唯一
			for (String name : nameList) {
				criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(entity, name)));
			}

			// 以下代码为了如果是update的情,排除entity自身.

			String idName = getIdName(entityClass);

			// 取得entity的主键
			Serializable id = getId(entityClass, entity);

			// 如果id!=null,说明对象已存,该操作为update,加入排除自身的判
			if (id != null)
				criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return (Integer) criteria.uniqueResult() == 0;
	}

	/**
	 * 取得对象的主键,辅助函数.
	 */
	public Serializable getId(Class entityClass, Object entity)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Assert.notNull(entity);
		Assert.notNull(entityClass);
		return (Serializable) PropertyUtils.getProperty(entity, getIdName(entityClass));
	}

	/**
	 * 取得对象的主键名,辅助函数.
	 */
	public String getIdName(Class clazz) {
		Assert.notNull(clazz);
		ClassMetadata meta = getSessionFactory().getClassMetadata(clazz);
		Assert.notNull(meta, "Class " + clazz + " not define in hibernate session factory.");
		String idName = meta.getIdentifierPropertyName();
		Assert.hasText(idName, clazz.getSimpleName() + " has no identifier property define.");
		return idName;
	}

	/**
	 * 去除hql的select 子句，未考虑union的情,用于pagedQuery.
	 * 
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	public static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除hql的orderby 子句，用于pagedQuery.
	 * 
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	public static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order//s*by[//w|//W|//s|//S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	// @Autowired
	// private SessionFactory sessionFactory;

	// public T save(T data) {
	// Session session = sessionFactory.openSession();
	// session.beginTransaction();
	// session.save(data);
	// session.getTransaction().commit();
	// return data;
	// }
	//
	// public T saveOrUpdate(T data) {
	// Session session = sessionFactory.openSession();
	// session.beginTransaction();
	// session.saveOrUpdate(data);
	// session.getTransaction().commit();
	// return data;
	// }
	//
	// public T update(T data) {
	// Session session = sessionFactory.openSession();
	// session.beginTransaction();
	// session.update(data);
	// session.getTransaction().commit();
	// return data;
	// }
	//
	// public void delete(T data) {
	// Session session = sessionFactory.openSession();
	// session.beginTransaction();
	// session.delete(data);
	// session.getTransaction().commit();
	// }
	//
	// public T findOne(long id) {
	// Session session = sessionFactory.openSession();
	// session.createCriteria();
	//
	// return null;
	// }

}
