package it.cagnesgiorgi.swam.elaborato2020.businessLogic.auth;

import it.cagnesgiorgi.swam.elaborato2020.Configurations;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CrossOriginResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        int status  = responseContext.getStatus();
        //if I'm returning a cached response it has yet the ACAO set
        String headerCORS = responseContext.getHeaderString("Access-Control-Allow-Origin");
        if ((status!=401) && (headerCORS==null)) {
            responseContext.getHeaders().add(
                    "Access-Control-Allow-Origin", Configurations.frontendDomain);
            responseContext.getHeaders().add(
                    "Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add(
                    "Access-Control-Allow-Headers",
                    "origin, content-type, accept, authorization");
            responseContext.getHeaders().add(
                    "Access-Control-Allow-Methods",
                    "GET, POST, PATCH, PUT, DELETE, OPTIONS, HEAD");
        }
    }
}