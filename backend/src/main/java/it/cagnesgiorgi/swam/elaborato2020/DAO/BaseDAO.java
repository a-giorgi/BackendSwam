package it.cagnesgiorgi.swam.elaborato2020.DAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//found examples with BaseDAO as interface but during lessons was specified as abstract class
public abstract class BaseDAO<T> {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public abstract void save(T t);
	public abstract void delete(T t);
}
