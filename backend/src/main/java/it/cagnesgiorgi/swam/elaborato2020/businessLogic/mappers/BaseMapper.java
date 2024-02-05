package it.cagnesgiorgi.swam.elaborato2020.businessLogic.mappers;

import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.FeedDTO;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Feed;

public abstract class BaseMapper<DtoObject, DomainModelObject> {
    public abstract DtoObject convert(DomainModelObject domainModelObject);
    public abstract void transfer(DtoObject dtoObject, DomainModelObject domainModelObject);
}
