package it.cagnesgiorgi.swam.elaborato2020.businessLogic.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cagnesgiorgi.swam.elaborato2020.DAO.TagDAO;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.*;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.GenericResponse;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.mappers.TagMapper;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.ModelFactory;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Tag;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Path("/tag")
public class TagController {

    @Inject
    TagDAO tagDAO;

    @Inject
    TagMapper mapper;

    LinkResource tagslink = new LinkResource("/rest/tag","GET, POST, OPTIONS","Retrieve all tags (GET) or create new tag(POST)");
    LinkResource taglink = new LinkResource("/rest/tag/{id}","GET, PATCH, DELETE", "Retrieve tag by id (GET) or update tag by id (PATCH) or delete tag by id (DELETE)");
    LinkResource active = new LinkResource("/rest/tag/active","GET","Retrieve all active tags (GET)");

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response createTag(TagDTO tagDTO){
        Tag tag = ModelFactory.tag();
        mapper.transfer(tagDTO,tag);
        //saving tag inside database
        try {
            tagDAO.save(tag);
        }catch (javax.persistence.PersistenceException exception){
            return javax.ws.rs.core.Response
                    .status(javax.ws.rs.core.Response.Status.CONFLICT)
                    .entity(new GenericResponse("The provided tag is already inside the database!", "ERROR"))
                    .build();
        }

        //returning back the newly created tag with resource information
        ResourceWrapper wrapperTag = new ResourceWrapper(mapper.convert(tag));
        wrapperTag.addResourceInfo("self",tagslink)
                .addResourceInfo("tag",taglink)
                .addResourceInfo("active",active);

        return javax.ws.rs.core.Response
                .status(javax.ws.rs.core.Response.Status.OK)
                .entity(wrapperTag)
                .build();
    }

    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response updateTag(TagDTO tagDTO, @PathParam("id") long id){
        //In this cased I'll allow only to active or disable the submitted tag
        Tag tag = tagDAO.getTag(id);
        //check if tag is not null
        if(tag == null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        tag.setActive(tagDTO.isActive());
        //saving tag inside database
        try {
            tagDAO.save(tag);
        }catch (javax.persistence.PersistenceException exception){
            return javax.ws.rs.core.Response
                    .status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
                    .entity(new GenericResponse("There was an error updating the submitted tag!", "ERROR"))
                    .build();
        }

        ResourceWrapper wrapperTag = new ResourceWrapper(mapper.convert(tag));
        wrapperTag.addResourceInfo("self",taglink)
                .addResourceInfo("tags",tagslink)
                .addResourceInfo("active",active);


        return javax.ws.rs.core.Response
                .status(javax.ws.rs.core.Response.Status.OK)
                .entity(wrapperTag)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response getTags(){
        List<Tag> tags = tagDAO.getTags();
        List<TagDTO> tagDTOS = new ArrayList<TagDTO>();
        ListIterator<Tag> tagListIterator = tags.listIterator();

        while (tagListIterator.hasNext()) {
            Tag tag = tagListIterator.next();
            tagDTOS.add(mapper.convert(tag));
        }
        ResourceWrapper wrapperTag = new ResourceWrapper(tagDTOS);
        wrapperTag.addResourceInfo("self",tagslink)
                .addResourceInfo("tag",taglink)
                .addResourceInfo("active",active);



        return Response
                .status(Response.Status.OK)
                .entity(wrapperTag)
                .build();
    }

    @GET
    @Path("/active")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiveTags() {
        List<Tag> tags = tagDAO.getActiveTags();
        List<TagDTO> tagDTOS = new ArrayList<TagDTO>();
        ListIterator<Tag> tagListIterator = tags.listIterator();

        while (tagListIterator.hasNext()) {
            Tag tag = tagListIterator.next();
            tagDTOS.add(mapper.convert(tag));
        }

        ResourceWrapper wrapperTag = new ResourceWrapper(tagDTOS);
        wrapperTag.addResourceInfo("self",active)
                .addResourceInfo("tag",taglink)
                .addResourceInfo("tags",tagslink);


        return Response
                .status(Response.Status.OK)
                .entity(wrapperTag)
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response getTag(@PathParam("id") long id) {
        Tag tag = tagDAO.getTag(id);
        if (tag != null) {


            ResourceWrapper wrapperTag = new ResourceWrapper(mapper.convert(tag));
            wrapperTag.addResourceInfo("self",taglink)
                    .addResourceInfo("tags",tagslink)
                    .addResourceInfo("active",active);

            return Response
                    .status(Response.Status.OK)
                    .entity(wrapperTag)
                    .build();
        }
        return Response
                .status(Response.Status.NOT_FOUND)
                .build();
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response deleteTag(@PathParam("id") long id) {
        Tag tag = tagDAO.getTag(id);
        if(tag != null){
            tagDAO.delete(tag);

            ResourceWrapper wrapperTag = new ResourceWrapper("Tag "+id+" was deleted successfully");
            wrapperTag.addResourceInfo("self",taglink)
                    .addResourceInfo("tags",tagslink)
                    .addResourceInfo("active",active);

            return Response
                    .status(Response.Status.OK)
                    .entity(wrapperTag)
                    .build();
        }

        return Response
                .status(Response.Status.NOT_FOUND)
                .build();

    }

    @OPTIONS
    @Produces(MediaType.TEXT_PLAIN)
    public Response getTagOptions() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        StringBuilder sb = new StringBuilder();
        String url = "/rest/tag/";
        sb.append("------------- Allowed Operations for url ").append(url).append(" -------------").append('\n').append('\n');

        //GET INFORMATION
        sb.append("Operation: RETRIEVE EVERY TAG").append('\n');
        sb.append("Verb: GET").append('\n');
        sb.append("Auth: Bearer JWT").append('\n');
        sb.append("Role required: ADMIN").append('\n');
        sb.append("Params: no params are required").append('\n').append('\n');

        //POST INFORMATION
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("Your Tag Name");
        tagDTO.setActive(true);

        sb.append("Operation: CREATE NEW TAG").append('\n');
        sb.append("Verb: POST").append('\n');
        sb.append("Auth: Bearer JWT").append('\n');
        sb.append("Role required: ADMIN").append('\n');
        sb.append("Params: name: String, active: boolean").append('\n');
        sb.append("Example request body: ").append(gson.toJson(tagDTO)).append('\n');


        sb.append("--------------------------").append('\n').append('\n');

        url= "/rest/tag/active";
        sb.append("------------- Allowed Operations for url ").append(url).append(" -------------").append('\n').append('\n');

        //GET ACTIVE TAGS
        sb.append("Operation: RETRIEVE ACTIVE TAGS").append('\n');
        sb.append("Verb: GET").append('\n');
        sb.append("Auth: no authentication required").append('\n');
        sb.append("Params: no params are required").append('\n');
        sb.append("--------------------------").append('\n').append('\n');

        url= "/rest/tag/{id}";
        sb.append("------------- Allowed Operations for url ").append(url).append(" -------------").append('\n').append('\n');


        //GET SPECIFIC INFORMATION
        sb.append("Operation: RETRIEVE SPECIFIC TAG").append('\n');
        sb.append("Verb: GET").append('\n');
        sb.append("Auth: Bearer JWT").append('\n');
        sb.append("Role required: ADMIN").append('\n');
        sb.append("Params: id: long (in url)").append('\n');
        sb.append("Replace the {id} in the url with the id of the desired resource").append('\n').append('\n');

        //PATCH SPECIFIC INFORMATION
        tagDTO.setName(null);
        sb.append("Operation: UPDATE SPECIFIC TAG").append('\n');
        sb.append("Verb: PATCH").append('\n');
        sb.append("Auth: Bearer JWT").append('\n');
        sb.append("Role required: ADMIN").append('\n');
        sb.append("Params: id: long (in url), active: boolean").append('\n');
        sb.append("Example request body: ").append(gson.toJson(tagDTO)).append('\n');
        sb.append("Replace the {id} in the url with the id of the desired resource").append('\n');
        sb.append("WARNING: name property cannot be modified. You must delete and create a new tag to change it").append('\n').append('\n');

        //DELETE SPECIFIC INFORMATION
        sb.append("Operation: DELETE SPECIFIC TAG").append('\n');
        sb.append("Verb: DELETE").append('\n');
        sb.append("Auth: Bearer JWT").append('\n');
        sb.append("Role required: ADMIN").append('\n');
        sb.append("Params: id: long (in url)").append('\n');
        sb.append("Replace the {id} in the url with the id of the desired resource").append('\n');
        sb.append("--------------------------").append('\n').append('\n');


        return Response
                .status(Response.Status.OK)
                .header("Allow:","GET, POST, DELETE, PATCH, OPTIONS")
                .entity(sb.toString())
                .build();
    }

}
