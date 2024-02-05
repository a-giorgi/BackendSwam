package it.cagnesgiorgi.swam.elaborato2020.businessLogic.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import it.cagnesgiorgi.swam.elaborato2020.DAO.FeedDAO;
import it.cagnesgiorgi.swam.elaborato2020.DAO.ZoneDAO;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.*;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.GenericResponse;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.mappers.FeedMapper;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Feed;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.ModelFactory;


//this class controls the resource feed
@Path("/feed")
public class FeedController {
	@Inject
	private FeedDAO feedDAO;
	@Inject
	private ZoneDAO zoneDAO;
	@Inject
	FeedMapper mapper;

	LinkResource feedslink = new LinkResource("/rest/feed","GET, POST, OPTIONS","Retrieve all feeds(GET) or create new feed(POST)");
	LinkResource feedlink = new LinkResource("/rest/feed/{id}","GET, PATCH, DELETE", "Retrieve feed by id (GET) or update feed by id (PATCH) or delete feed by id (DELETE)");


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFeeds() {

		List<Feed> feeds = feedDAO.getFeeds();
		List<FeedDTO> feedsDTOs = new ArrayList<FeedDTO>();
		ListIterator<Feed> feedsIterator = feeds.listIterator();

		while (feedsIterator.hasNext()) {
			Feed feed = feedsIterator.next();
			feedsDTOs.add(mapper.convert(feed));
		}
		ResourceWrapper wrapperFeed = new ResourceWrapper(feedsDTOs);
		wrapperFeed.addResourceInfo("self",feedslink)
                   .addResourceInfo("feed",feedlink);

		return Response
				.status(Response.Status.OK)
				.entity(wrapperFeed)
				.build();
		
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response createFeed(FeedDTO feedDTO){
		//firstly I have to be sure that the provided link is an rss feed
		try
		{
			SyndFeedInput input = new SyndFeedInput();
			XmlReader xmlReader = new XmlReader(new URL(feedDTO.getUrl()));
			input.build(xmlReader);
			//Ok the feed is valid
		}catch(Exception exception){
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(new GenericResponse("The provided url wasn't a valid rss feed!","400-1"))
					.build();
		};
		//Now let's create the Feed Object
		Feed feed = ModelFactory.feed();
		//I'll use the mapper to set up the Feed Object
		mapper.transfer(feedDTO,feed);
		//saving feed inside database
		try {
			feedDAO.save(feed);
		}catch (javax.persistence.PersistenceException exception){
			return Response
					.status(Response.Status.CONFLICT)
					.entity(new GenericResponse("The provided url is already inside the database!", "409-1"))
					.build();
		}

		//giving back the saved feed
		ResourceWrapper wrapperFeed = new ResourceWrapper(mapper.convert(feed));
		wrapperFeed.addResourceInfo("self",feedslink)
				   .addResourceInfo("feed",feedlink);

		return Response
				.status(Response.Status.OK)
				.entity(wrapperFeed)
				.build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response deleteFeed(@PathParam("id") long id) {
		Feed feed = feedDAO.getFeed(id);
		if(feed != null){
			feedDAO.delete(feed);
			ResourceWrapper wrapperFeed = new ResourceWrapper("Feed "+id+" was deleted successfully");
			wrapperFeed.addResourceInfo("feeds",feedslink)
					       .addResourceInfo("self",feedlink);

			return Response
					.status(Response.Status.OK)
					.entity(wrapperFeed)
					.build();
		}

		return Response
				.status(Response.Status.NOT_FOUND)
				.build();

	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFeed(@PathParam("id") long id) {
		Feed feed = feedDAO.getFeed(id);
		if(feed != null){

			ResourceWrapper wrapperFeed = new ResourceWrapper(mapper.convert(feed));
			wrapperFeed.addResourceInfo("feeds",feedslink)
					.addResourceInfo("self",feedlink);

			return Response
					.status(Response.Status.OK)
					.entity(wrapperFeed)
					.build();
		}

		return Response
				.status(Response.Status.NOT_FOUND)
				.build();
		
	}

	@PATCH
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response updateFeed(FeedDTO feedDTO, @PathParam("id") long id){
		Feed feed = feedDAO.getFeed(id);
		feed.removeTags();
		mapper.deserializeTags(feedDTO,feed);
		feed.setTopNews(feedDTO.isTopNews());
		feed.setName(feedDTO.getName());
		feed.setZone(zoneDAO.getByCountryCode(feedDTO.getCountryCode()));
		feed.setActive(feedDTO.isActive());
		try {
			feedDAO.save(feed);
		}catch (javax.persistence.PersistenceException exception){
			return javax.ws.rs.core.Response
					.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
					.entity(new GenericResponse("There was an error updating the submitted feed!", "400-2"))
					.build();
		}
		//now updating dtos to show updated data

		ResourceWrapper wrapperFeed = new ResourceWrapper(mapper.convert(feed));
		wrapperFeed.addResourceInfo("feeds",feedslink)
				.addResourceInfo("self",feedlink);

		return Response
				.status(Response.Status.OK)
				.entity(wrapperFeed)
				.build();
	}

	@OPTIONS
	@Produces(MediaType.TEXT_PLAIN)
	public Response getFeedOptions() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		StringBuilder sb = new StringBuilder();
		String url = "/rest/feed/";
		sb.append("------------- Allowed Operations for url ").append(url).append(" -------------").append('\n').append('\n');

		//GET INFORMATION
		sb.append("Operation: RETRIEVE EVERY FEEDS").append('\n');
		sb.append("Verb: GET").append('\n');
		sb.append("Auth: no authentication required").append('\n');
		sb.append("Params: no params are required").append('\n').append('\n');

		//POST INFORMATION
		FeedDTO feedDTO = new FeedDTO();
		feedDTO.setUrl("http://your-feed-url-here.com/feed.xml");
		feedDTO.setCountryCode("IT");
		feedDTO.setName("Your Feed");
		feedDTO.setTags("1,5,3");
		feedDTO.setTopNews(true);
		feedDTO.setActive(true);

		sb.append("Operation: CREATE NEW FEED").append('\n');
		sb.append("Verb: POST").append('\n');
		sb.append("Auth: Bearer JWT").append('\n');
		sb.append("Role required: ADMIN").append('\n');
		sb.append("Params: name: String, url: String, countryCode: String, topNews: boolean, active: boolean, tags: String").append('\n');
		sb.append("Example request body: ").append(gson.toJson(feedDTO)).append('\n');
		sb.append("countryCode must contain the country code of an EU state ").append('\n');
		sb.append("tags must contain the id of previously inserted tags separated by commas; example: \"1,3,5\"").append('\n');


		sb.append("--------------------------").append('\n').append('\n');

		url= "/rest/feed/{id}";
		sb.append("------------- Allowed Operations for url ").append(url).append(" -------------").append('\n').append('\n');
		//GET SPECIFIC INFORMATION
		sb.append("Operation: RETRIEVE SPECIFIC FEED").append('\n');
		sb.append("Verb: GET").append('\n');
		sb.append("Auth: no authentication required").append('\n');
		sb.append("Params: id: long (in url)").append('\n');
		sb.append("Replace the {id} in the url with the id of the desired resource").append('\n').append('\n');


		//PATCH SPECIFIC INFORMATION
		feedDTO.setUrl(null);
		sb.append("Operation: UPDATE SPECIFIC FEED").append('\n');
		sb.append("Verb: PATCH").append('\n');
		sb.append("Auth: Bearer JWT").append('\n');
		sb.append("Role required: ADMIN").append('\n');
		sb.append("Params: id: long (in url), name: String, countryCode: String, topNews: boolean, active: boolean, tags: String").append('\n');
		sb.append("Example request body: ").append(gson.toJson(feedDTO)).append('\n');
		sb.append("Replace the {id} in the url with the id of the desired resource").append('\n');
		sb.append("WARNING: url property cannot be modified. You must delete and create a new feed to change it").append('\n');
		sb.append("countryCode must contain the country code of an EU state ").append('\n');
		sb.append("tags must contain the id of previously inserted tags separated by commas; example: \"1,3,5\"").append('\n').append('\n');

		//DELETE SPECIFIC INFORMATION
		sb.append("Operation: DELETE SPECIFIC FEED").append('\n');
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
