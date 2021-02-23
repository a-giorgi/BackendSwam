package it.cagnesgiorgi.swam.elaborato2020.DAO;

import it.cagnesgiorgi.swam.elaborato2020.domainModel.Feed;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Tag;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Zone;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional
public class TagDAO extends BaseDAO<Tag> {

    @Inject
    private FeedDAO feedDAO;

    public TagDAO(){
    }

    @Override
    public void save(Tag tag) {
        if(tag.getId()!=null) {
            entityManager.merge(tag);
        }else {
            entityManager.persist(tag);
        }
    }

    @Override
    public void delete(Tag tag) {
        if(tag!=null){
            //removing the tag from all feeds
            List<Feed> feeds = feedDAO.getFeeds();
            for (Feed feed: feeds){
                feed.removeTag(tag);
                feedDAO.save(feed);
            }
            //With JTA at every commit, entities will be detached by persistence context so I have to check this
            entityManager.remove(entityManager.contains(tag) ? entityManager : entityManager.merge(tag));


        }
    }
    public Tag getTag(Long id){
        return entityManager.find(Tag.class, id);
    }

    public List<Tag> getTags(){
        TypedQuery<Tag> query = entityManager.createQuery("SELECT t FROM Tag t", Tag.class);
        return query.getResultList();
    }

    public List<Tag> getActiveTags(){
        TypedQuery<Tag> query = entityManager.createQuery("SELECT t FROM Tag t WHERE t.active = true", Tag.class);
        return query.getResultList();
    }

}
