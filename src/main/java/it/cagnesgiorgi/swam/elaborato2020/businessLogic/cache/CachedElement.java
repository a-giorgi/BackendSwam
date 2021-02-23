package it.cagnesgiorgi.swam.elaborato2020.businessLogic.cache;

import javax.ws.rs.core.Response;
import java.time.Instant;

public class CachedElement {
    private Instant created;
    private Instant expiry;

    public Response getResponse() {
        return response;
    }

    private Response response;

    public CachedElement(Response response, Instant created) {
        int maxAge = Cache.getMaxAge(response);
        this.created = created;
        this.expiry = created.plusSeconds(maxAge);
        this.response = response;
    }

    public boolean isExpired(){
        Instant now = Instant.now();
        int compare = now.compareTo(expiry);

        if (compare > 0) {
            return true;
        }
        return false;

    }

}
