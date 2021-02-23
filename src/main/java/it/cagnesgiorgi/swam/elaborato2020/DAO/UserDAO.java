package it.cagnesgiorgi.swam.elaborato2020.DAO;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;
import javax.transaction.Transactional;

import it.cagnesgiorgi.swam.elaborato2020.domainModel.User;

@RequestScoped
@Transactional
public class UserDAO extends BaseDAO<User>{
	
	public UserDAO() {
	}

	@Override
	public void save(User user) {
		if(user.getId()!=null) {
			entityManager.merge(user);
		}else {
			entityManager.persist(user);
		}
		
	}

	public User getUserByName(String username) {
		if(username == null || username.length()==0) {
			return null;
		}
		Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :name");
		query.setParameter("name", username);
		List<User> resultList= query.setMaxResults(1).getResultList();
		if(resultList.size()==0){
			return null;
		}else{
			return resultList.get(0);
		}
	}

	public User getUserByEmail(String email) {
		Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :mail");
		query.setParameter("mail", email);
		List<User> resultList= query.setMaxResults(1).getResultList();
		if(resultList.size()==0){
			return null;
		}else{
			return resultList.get(0);
		}
	}
	
	public User findById(Long id) {
		return entityManager.find(User.class, id);		
	}

	@Override
	public void delete(User user) {
		// Delete not implemented
		
	}


}
