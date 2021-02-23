package it.cagnesgiorgi.swam.elaborato2020.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import it.cagnesgiorgi.swam.elaborato2020.domainModel.Feed;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Tag;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Zone;

@RequestScoped
@Transactional
public class FeedDAO extends BaseDAO<Feed> implements Serializable {

	@Inject
	private ZoneDAO zoneDAO;

	public FeedDAO() {}
	
	@Override
	public void save(Feed feed) {
		if(feed.getId()!=null) {
			entityManager.merge(feed);
		}else {
			entityManager.persist(feed);
		}
		
	}

	@Override
	public void delete(Feed feed) {
		if(feed!=null){
			//With JTA at every commit, entities will be detached by persistence context so I have to check this
			entityManager.remove(entityManager.contains(feed) ? entityManager : entityManager.merge(feed));
		}
	}

	public void deleteById(long id){
		Feed feed = entityManager.find(Feed.class, id);
		if(feed != null) {
			entityManager.remove(feed);
		}
	}

	
	public List<Feed> getFeeds(){
		TypedQuery<Feed> query = entityManager.createQuery("SELECT f FROM Feed f", Feed.class);
		return query.getResultList();
	}

	public List<Feed> getFeeds(String countryCode){
		TypedQuery<Feed> query = entityManager.createQuery("FROM Feed WHERE zone = :zone", Feed.class);
		Zone zone = zoneDAO.getByCountryCode(countryCode);
		query.setParameter("zone", zone);
		return query.getResultList();
	}

	public List<Feed> getFeeds(Tag tag){
		TypedQuery<Feed> query = entityManager.createQuery("FROM Feed WHERE Tag = :tag", Feed.class);
		query.setParameter("tag", tag);
		return query.getResultList();
	}

	public List<Feed> getActiveFeeds(String countryCode){
		TypedQuery<Feed> query = entityManager.createQuery(" SELECT f FROM Feed f WHERE f.zone.countryCode = :countryCode AND f.active = true", Feed.class);
		//Zone zone = zoneDAO.getByCountryCode(countryCode);
		query.setParameter("countryCode", countryCode);
		return query.getResultList();
	}

	public Feed getFeed(long id){
		/*TypedQuery<Feed> query = entityManager.createQuery("SELECT f FROM Feed f WHERE f.id = :id", Feed.class);
		query.setParameter("id", id);
		return query.getResultList().get(0);*/
		Feed feed = entityManager.find(Feed.class, id);
		return feed;
	}



}
