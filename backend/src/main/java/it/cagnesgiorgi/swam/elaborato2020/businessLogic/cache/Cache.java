package it.cagnesgiorgi.swam.elaborato2020.businessLogic.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class Cache {
    private Map<String, CachedElement> responses;

    public Cache(){
        responses = new HashMap<String,CachedElement>();
    }

    public Response getCached(String region){
        if(responses.containsKey(region)){
            CachedElement cachedResponse = responses.get(region);
            if(!cachedResponse.isExpired()){
                return cachedResponse.getResponse();
            }
        }
        return null;

    }
    public void cacheResponse(String region, Response response){
        if(responses.containsKey(region)){
            responses.remove(region);
        }
        responses.put(region,new CachedElement(response, Instant.now()));
    }

    public static int getMaxAge(Response response){
        //If I doesn't have headers or cache control this is the cleanest way to prevent issue
        try {
            String cacheControl = response.getHeaders().get("Cache-Control").get(0).toString();
            String[] cacheHeaders = cacheControl.split(",");
            for (String cacheHeader : cacheHeaders) {
                if (cacheHeader.contains("max-age")) {
                    String maxAge = cacheHeader.substring(cacheHeader.lastIndexOf("=") + 1);
                    return Integer.parseInt(maxAge);
                }
            }
        }catch(NullPointerException exception){
            return 0;
        }
        return 0;
    }

    public void clearCache(){
        responses.clear();
    }
}
