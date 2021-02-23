package it.cagnesgiorgi.swam.elaborato2020.DAO;

import it.cagnesgiorgi.swam.elaborato2020.domainModel.Feed;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Tag;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Zone;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional
public class ZoneDAO extends BaseDAO<Zone>{

    public ZoneDAO(){

    }

    @Override
    public void save(Zone zone) {
        if(zone.getId()!=null) {
            entityManager.merge(zone);
        }else {
            try {
                entityManager.persist(zone);
            }catch(Exception exception){
                System.out.println(exception.getMessage());
            }
        }
    }

    public Zone getByCountryCode(String countryCode){
        TypedQuery<Zone> query = entityManager.createQuery("Select z FROM Zone z WHERE z.countryCode = :countryCode", Zone.class);
        query.setParameter("countryCode", countryCode);
        List<Zone> resultList = query.getResultList();
        if(resultList.size()==0){
            return null;
        }else{
            return resultList.get(0);
        }
    }

    public List<Zone> getZones(){
        TypedQuery<Zone> query = entityManager.createQuery("SELECT z FROM Zone z", Zone.class);
        return query.getResultList();
    }

    @Override
    public void delete(Zone zone) {
        //Delete not implemented
    }
}
