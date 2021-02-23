package it.cagnesgiorgi.swam.elaborato2020.businessLogic.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import it.cagnesgiorgi.swam.elaborato2020.Configurations;
import it.cagnesgiorgi.swam.elaborato2020.DAO.FeedDAO;
import it.cagnesgiorgi.swam.elaborato2020.DAO.UserDAO;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.*;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.GenericResponse;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.cache.Cache;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.mappers.FeedMapper;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Feed;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.User;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.Zone;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


@Path("/news")
public class NewsController {

    @Inject
    private FeedDAO feedDAO;

    @Inject
    private Cache cache;

    @Inject
    private FeedMapper mapper;

    @Inject
    private UserDAO userDAO;

    LinkResource newslink = new LinkResource("/rest/news","GET, DELETE, OPTIONS","Retrieve all news (GET) or delete the cache (DELETE)");

    public static String filterDescription(String description){
        description = description.replaceAll("(?s)<.*?>", "");
        description = description.replace("&nbsp;", " ");
        return description;
    };

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNews(@Context SecurityContext securityContext, @Context HttpServletRequest httpServletRequest){
        String principalUsername = null;
        Principal principal = securityContext.getUserPrincipal();
        if(principal!=null){
            principalUsername = principal.getName();
        }

        //Retrieving the IP Address
        String ipAddress =  httpServletRequest.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = httpServletRequest.getRemoteAddr();
        }

        //this is a placeholder IP just to test the endpoint when deployed locally (obtained from https://lite.ip2location.com/italy-ip-address-ranges)
        if(Configurations.isLocal){
            ipAddress = "184.174.46.0";
        }

        Zone zone = null;

        if(principalUsername!=null){
            User user = userDAO.getUserByEmail(principalUsername);
            if (user!=null){
                zone = user.getZone();
            }
        }

        String countryCode;

        if(zone==null){
            try {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://www.geoplugin.net/json.gp?ip="+ipAddress)
                        .get()
                        .build();
                //firstly I'll retrieve the location
                okhttp3.Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                JsonObject jsonResponse = new Gson().fromJson(jsonData, com.google.gson.JsonObject.class);

                //Now I have the zone
                countryCode = jsonResponse.get(Configurations.regionKey).getAsString();
            }catch(IOException e) {
                e.printStackTrace();
                return Response
                        .status(Response.Status.SERVICE_UNAVAILABLE)
                        .build();
            }
        }else {
            countryCode = zone.getCountryCode();
        }

        //Now I have the correct countryCode

        //let's check if there is a Response cached
        Response cachedResponse = cache.getCached(countryCode);
        if(cachedResponse!=null){
            return cachedResponse;
        }

        //I retrieve the feeds from that zone with feedDAO
        List<Feed> feeds = feedDAO.getActiveFeeds(countryCode);

        //I'll gather every news now
        ListIterator<Feed> feedsIterator = feeds.listIterator();

        //using an ArrayList with a dedicated DTO to generate the response
        ArrayList<NewsDTO> news = new ArrayList<NewsDTO>();

        StringBuilder sb = new StringBuilder();
        int relativeId = 0;

        while (feedsIterator.hasNext()) {
            Feed feed = feedsIterator.next();
            try {
                SyndFeedInput input = new SyndFeedInput();
                XmlReader xmlReader = new XmlReader(new URL(feed.getUrl()));
                SyndFeed feedStream = input.build(xmlReader);
                String feedImageUrl = "";
                if(feedStream.getImage()!=null){
                    feedImageUrl = feedStream.getImage().getUrl();
                }
                String tags = mapper.serializeTags(feed);
                for (Iterator<SyndEntry> i = feedStream.getEntries().iterator(); i.hasNext(); ) {
                    relativeId++;
                    SyndEntry entry = i.next();
                    String enclosure = "";
                    if(!entry.getEnclosures().isEmpty()){
                        SyndEnclosure enc = (SyndEnclosure) entry.getEnclosures().get(0);
                        enclosure = enc.getUrl();
                    }else{
                        enclosure = feedImageUrl;
                    }
                    NewsDTO newsDTO = new NewsDTO(entry.getTitle(), NewsController.filterDescription(entry.getDescription().getValue()), entry.getLink(), tags, feed.getName(), feed.isTopNews(),enclosure);
                    news.add(newsDTO);
                    newsDTO.setId(relativeId);

                }
                xmlReader.close();
            }catch(FeedException | IOException exception) {
                sb.append("Feed ").append(feed.getName()).append("with id: ").append(feed.getId()).append(" threw an exception").append('\n');
            }
        }
        //now it's time to set up the cache
        CacheControl cc = new CacheControl();

        //with this the client will never store the responses: the only cache available will be the server one
        cc.setNoCache(true);
        cc.setMaxAge(3600);

        ResourceWrapper resourceWrapper = new ResourceWrapper(news);
        resourceWrapper.addResourceInfo("self",newslink);

        Response.ResponseBuilder builder = Response.ok(resourceWrapper);
        builder.cacheControl(cc);

        Response feedsResponse  = builder.build();
        cache.cacheResponse(countryCode,feedsResponse);

        return feedsResponse;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response deleteCache(){
        cache.clearCache();
        return Response
                .status(Response.Status.OK)
                .entity(new GenericResponse("Cache was successfully cleared","SUCCESS"))
                .build();
    }

    @OPTIONS
    @Produces(MediaType.TEXT_PLAIN)
    public Response getNewsOptions() {
        StringBuilder sb = new StringBuilder();
        String url = "/rest/news";

        sb.append("------------- Allowed Operations for url ").append(url).append(" -------------").append('\n').append('\n');

        //GET INFORMATION
        sb.append("Operation: RETRIEVE EVERY NEWS BASED ON THE USER COUNTRY").append('\n');
        sb.append("Verb: GET").append('\n');
        sb.append("Auth: no authentication required").append('\n');
        sb.append("Params: no params are required").append('\n');
        sb.append("If the user is authenticated and has a zone set up, news will be retrieved based on that instead of using the IP-based geolocalization service")
                .append('\n').append('\n');

        sb.append("Operation: DELETE ALL CACHED RESPONSES").append('\n');
        sb.append("Verb: DELETE").append('\n');
        sb.append("Auth: Bearer JWT").append('\n');
        sb.append("Role required: ADMIN").append('\n');
        sb.append("Params: no params are required").append('\n');

        sb.append("--------------------------");



        return Response
                .status(Response.Status.OK)
                .header("Allow:","GET, OPTIONS")
                .entity(sb.toString())
                .build();

    }

}
