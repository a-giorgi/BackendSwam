package it.cagnesgiorgi.swam.elaborato2020.businessLogic.mappers;

import it.cagnesgiorgi.swam.elaborato2020.DAO.ZoneDAO;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.UserDTO;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.User;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Zone;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Transactional
@RequestScoped
public class UserMapper extends BaseMapper<UserDTO, User> {
    @Inject
    private ZoneDAO zoneDAO;

    @Override
    public UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        Zone zone = user.getZone();
        if(zone!=null){
            userDTO.setCountryCode(serializeZone(zone));
        }
        userDTO.setId(user.getId());
        return userDTO;
    }

    @Override
    public void transfer(UserDTO userDTO, User user) {
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setZone(deserializeZone(userDTO.getCountryCode()));

    }

    public Zone deserializeZone(String countryCode){
        if(countryCode!=null) {
            return zoneDAO.getByCountryCode(countryCode);
        }else{
            return null;
        }
    }

    public String serializeZone(Zone zone){
        if(zone!=null) {
            return zone.getCountryCode();
        }else{
            return null;
        }
    }
}
