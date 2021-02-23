package it.cagnesgiorgi.swam.elaborato2020;

public class Configurations {
    /** Set forwarded to true if the application is behind a proxy server, load balancer or Cloudflare*/
    public static final boolean forwarded = false;
    /** Set isLocal to true if the application is developed locally*/
    public static final boolean isLocal = true;
    /** The field inside the JSON of the location service that specify the region*/
    public static final String regionKey = "geoplugin_countryCode";
    /** The domain of the frontend, only requests with the origin set to this domain will be served*/
    //use the wildcard * to allow every origin
    public static final String frontendDomain = "http://localhost:3000";
    //public static final String frontendDomain = "*";
}
