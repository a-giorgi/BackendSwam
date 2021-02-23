package it.cagnesgiorgi.swam.elaborato2020.businessLogic.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cagnesgiorgi.swam.elaborato2020.DAO.ZoneDAO;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.ResourceWrapper;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.LinkResource;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.UserDTO;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.ZoneDTO;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.mappers.ZoneMapper;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Zone;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Path("/zone")
public class ZoneController {

    @Inject
    private ZoneDAO zoneDAO;

    @Inject
    private ZoneMapper mapper;

    LinkResource zonelink = new LinkResource("/rest/zone","GET, OPTIONS","Retrieve all allowed Zones");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTags(){
        List<Zone> zones = zoneDAO.getZones();
        List<ZoneDTO> zoneDTOs = new ArrayList<ZoneDTO>();
        ListIterator<Zone> zoneListIterator = zones.listIterator();

        while (zoneListIterator.hasNext()) {
            Zone zone = zoneListIterator.next();
            zoneDTOs.add(mapper.convert(zone));
        }
        ResourceWrapper wrapperZone = new ResourceWrapper(zoneDTOs);
        wrapperZone.addResourceInfo("self",zonelink);


        return Response
                .status(Response.Status.OK)
                .entity(wrapperZone)
                .build();
    }

    @OPTIONS
    @Produces(MediaType.TEXT_PLAIN)
    public Response getZoneOptions() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        StringBuilder sb = new StringBuilder();
        String url = "/rest/zone/";
        sb.append("------------- Allowed Operations for url ").append(url).append(" -------------").append('\n').append('\n');


        //GET SPECIFIC INFORMATION

        sb.append("Operation: GET ALL ZONES").append('\n');
        sb.append("Verb: GET").append('\n');
        sb.append("Auth: no authentication required").append('\n');
        sb.append("Role required: none").append('\n');
        sb.append("--------------------------").append('\n').append('\n');


        return Response
                .status(Response.Status.OK)
                .header("Allow:","GET, HEAD, OPTIONS")
                .entity(sb.toString())
                .build();

    }

}
