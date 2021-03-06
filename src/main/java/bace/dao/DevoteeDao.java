package bace.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.NullPrecedence;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bace.pojo.Devotee;
import static bace.utils.Constants.*;

@Transactional
@Repository("devoteeDao")
public class DevoteeDao {

	private static final String ASCENDING = "ascending";

	@Autowired
	private SessionFactory sessionFactory;

	private Session session;
	
	private SimpleExpression se0, se1, se2, se3, se4, se5, se6, se7;
	
	public int save(Devotee devotee) {
		session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(devotee);
		return devotee.getId();
	}
	
	public void delete(int id) {
		session = sessionFactory.getCurrentSession();
		session.delete(new Devotee(id));
	}
	
	public Devotee get(int id) {
		session = sessionFactory.getCurrentSession();
		return (Devotee) session.get(Devotee.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Devotee> list(String searchQuery, String pageNumber
			, String maximumResults, String sortBy, String order) {
		session = sessionFactory.getCurrentSession();
		Criteria cr = session.createCriteria(Devotee.class);
		if (!isNullOrEmpty(pageNumber) && !isNullOrEmpty(maximumResults)) {
			int max = Integer.parseInt(maximumResults);
			cr.setMaxResults(max);
			cr.setFirstResult((Integer.parseInt(pageNumber) - 1) * max);
		}
		if (!isNullOrEmpty(searchQuery)) {
			if (StringUtils.isNumeric(searchQuery)) {
				se0 = Restrictions.eq(ID2, searchQuery);
				cr.add(Restrictions.or(se0));
			}
			se1 = Restrictions.like(NAME, PERCENT + searchQuery + PERCENT);
			se2 = Restrictions.like(PERMANENT_ADDRESS, PERCENT + searchQuery + PERCENT);
			se3 = Restrictions.like(CURRENT_ADDRESS, PERCENT + searchQuery + PERCENT);
			se4 = Restrictions.like(MOBILE_NUMBER, PERCENT + searchQuery + PERCENT);
			se5 = Restrictions.like(EMAIL, PERCENT + searchQuery + PERCENT);
			se6 = Restrictions.like(EMERGENCY_NUMBER, PERCENT + searchQuery + PERCENT);
			se7 = Restrictions.like(FATHER_NAME, PERCENT + searchQuery + PERCENT);
			cr.add(Restrictions.or(se1, se2, se3, se4, se5, se6, se7));
		}
		if (!isNullOrEmpty(sortBy)) {
			if (isNullOrEmpty(order) || order.equalsIgnoreCase(ASCENDING)) {
				cr.addOrder(Order.asc(sortBy).nulls(NullPrecedence.LAST));
			} else {
				cr.addOrder(Order.desc(sortBy).nulls(NullPrecedence.LAST));
			}
		} else {
			cr.addOrder(Order.asc(NAME));
		}
		return (List<Devotee>) cr.list();
	}
	
	private boolean isNullOrEmpty(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public long getTotalResults(String searchQuery) {
		session = sessionFactory.getCurrentSession();
		Criteria cr = session.createCriteria(Devotee.class);
		if (searchQuery != null && !searchQuery.isEmpty()) {
			SimpleExpression se0, se1, se2, se3, se4, se5, se6, se7;
			if (StringUtils.isNumeric(searchQuery)) {
				se0 = Restrictions.eq(ID2, searchQuery);
				cr.add(Restrictions.or(se0));
			}
			se1 = Restrictions.like(NAME, PERCENT + searchQuery + PERCENT);
			se2 = Restrictions.like(PERMANENT_ADDRESS, PERCENT + searchQuery + PERCENT);
			se3 = Restrictions.like(CURRENT_ADDRESS, PERCENT + searchQuery + PERCENT);
			se4 = Restrictions.like(MOBILE_NUMBER, PERCENT + searchQuery + PERCENT);
			se5 = Restrictions.like(EMAIL, PERCENT + searchQuery + PERCENT);
			se6 = Restrictions.like(EMERGENCY_NUMBER, PERCENT + searchQuery + PERCENT);
			se7 = Restrictions.like(FATHER_NAME, PERCENT + searchQuery + PERCENT);
			cr.add(Restrictions.or(se1, se2, se3, se4, se5, se6, se7));
		}
		return (long) cr.setProjection(Projections.rowCount()).uniqueResult();

	}
}
