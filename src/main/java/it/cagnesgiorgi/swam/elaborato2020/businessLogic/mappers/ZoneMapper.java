package it.cagnesgiorgi.swam.elaborato2020.businessLogic.mappers;



import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.ZoneDTO;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Zone;

public class ZoneMapper extends BaseMapper<ZoneDTO, Zone> {

    @Override
    public ZoneDTO convert(Zone zone) {
        return new ZoneDTO(zone.getCountryName(),zone.getCountryCode());
    }

    @Override
    public void transfer(ZoneDTO zoneDTO, Zone zone) {
        //not implemented
    }
}
