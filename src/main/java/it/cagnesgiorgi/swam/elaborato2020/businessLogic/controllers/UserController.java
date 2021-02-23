package it.cagnesgiorgi.swam.elaborato2020.businessLogic.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cagnesgiorgi.swam.elaborato2020.DAO.UserDAO;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.*;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs.GenericResponse;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.auth.PasswordAuthenticator;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.auth.TokenFactory;
import it.cagnesgiorgi.swam.elaborato2020.businessLogic.mappers.UserMapper;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.ModelFactory;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.User;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Path("/user")
public class UserController {
    @Inject
    private UserDAO userDao;
    @Inject
    private UserMapper mapper;

    LinkResource userlink = new LinkResource("/rest/user","POST, OPTIONS","Create a new user (POST)");
    LinkResource editlink = new LinkResource("/rest/user/{id}","PATCH, DELETE", "Get user info (GET) or update user by id (PATCH) or delete user by id (DELETE)");
    LinkResource loginlink = new LinkResource("/rest/user/login","POST","Login with your credentials (POST)");
    LinkResource resetlink = new LinkResource("/rest/user/reset","POST","Reset Password (POST)");


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUser(UserDTO userDTO) {
        if(!userDTO.assertRequired()){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new GenericResponse("User is invalid", "400-1"))
                    .build();
        }
        if(!userDTO.checkPasswordRequirements()){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new GenericResponse("Password does not meet the requirements", "400-2"))
                    .build();
        }
        User user = ModelFactory.user();
        mapper.transfer(userDTO, user);

        //now generating password
        String salt = PasswordAuthenticator.getSalt(30);
        String encryptedPassword = PasswordAuthenticator.generateSecurePassword(userDTO.getPassword(),salt);
        user.setPassword(encryptedPassword);
        user.setSalt(salt);
        try {
            userDao.save(user);
        }catch (javax.persistence.PersistenceException exception){
            return Response
                    .status(javax.ws.rs.core.Response.Status.CONFLICT)
                    .entity(new GenericResponse("The provided username or email is already inside the database!", "ERROR"))
                    .build();
        }
        userDTO.setId(user.getId());
        userDTO.setPassword("**********");
        userDTO.setConfirmPassword(null);

        ResourceWrapper wrapperUser = new ResourceWrapper(userDTO);
        wrapperUser.addResourceInfo("self",userlink)
                .addResourceInfo("update", editlink)
                .addResourceInfo("login",loginlink)
                .addResourceInfo("reset",resetlink);

        return Response
                .status(Response.Status.OK)
                .entity(wrapperUser.toJson())
                .build();

    }

    @PATCH
    @Path("/{id}/reset")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setNewPassword(@Context SecurityContext securityContext, @PathParam("id") long userId, UserDTO userDTO){
        String principalUsername = null;
        Principal principal = securityContext.getUserPrincipal();
        if(principal!=null){
            principalUsername = principal.getName();
        }
        if(principalUsername==null){
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .build();
        }
        User user = userDao.getUserByEmail(principalUsername);
        if(user.getId() == userId){
            if(!userDTO.checkPasswordRequirements()){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(new GenericResponse("Password does not meet the requirements", "400-2"))
                        .build();
            }
            if(userDTO.getOldPassword()==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(new GenericResponse("Old Password missing", "400-3"))
                        .build();
            }
            if(!PasswordAuthenticator.verifyPassword(userDTO.getOldPassword(), user.getPassword(),user.getSalt())) {
                return Response
                        .status(Response.Status.FORBIDDEN)
                        .entity(new GenericResponse("Wrong password!", "403-1"))
                        .build();
            }


            String salt = PasswordAuthenticator.getSalt(30);
            String encryptedPassword = PasswordAuthenticator.generateSecurePassword(userDTO.getPassword(),salt);
            user.setPassword(encryptedPassword);
            user.setSalt(salt);

            userDao.save(user);

            UserDTO responseUser = mapper.convert(user);
            responseUser.setPassword("**********");
            responseUser.setConfirmPassword("**********");

            ResourceWrapper wrapperUser = new ResourceWrapper(responseUser);
            wrapperUser.addResourceInfo("user",userlink)
                    .addResourceInfo("update", editlink)
                    .addResourceInfo("login",loginlink)
                    .addResourceInfo("self",resetlink);

            return Response
                    .status(Response.Status.OK)
                    .entity(wrapperUser.toJson())
                    .build();
        }else{
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .build();
        }
    }

    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@Context SecurityContext securityContext, @PathParam("id") long userId, UserDTO userDTO){
        String principalUsername = null;
        Principal principal = securityContext.getUserPrincipal();
        if(principal!=null){
            principalUsername = principal.getName();
        }
        if(principalUsername==null){
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .build();
        }
        User user = userDao.getUserByEmail(principalUsername);
        if(user.getId() == userId){
            user.setZone(mapper.deserializeZone(userDTO.getCountryCode()));
            userDao.save(user);
            UserDTO responseUser = mapper.convert(user);
            userDTO.setPassword(null);

            ResourceWrapper wrapperUser = new ResourceWrapper(responseUser);
            wrapperUser.addResourceInfo("user",userlink)
                    .addResourceInfo("self", editlink)
                    .addResourceInfo("login",loginlink)
                    .addResourceInfo("reset",resetlink);

            return Response
                    .status(Response.Status.OK)
                    .entity(wrapperUser.toJson())
                    .build();
        }else{
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserInfo(@Context SecurityContext securityContext, @PathParam("id") long userId){
        String principalUsername = null;
        Principal principal = securityContext.getUserPrincipal();
        if(principal!=null){
            principalUsername = principal.getName();
        }
        if(principalUsername==null){
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .build();
        }
        User user = userDao.getUserByEmail(principalUsername);
        if(user == null){
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .build();
        }
        if(user.getId() == userId){
            UserDTO responseUser = mapper.convert(user);
            responseUser.setPassword(null);

            ResourceWrapper wrapperUser = new ResourceWrapper(responseUser);
            wrapperUser.addResourceInfo("user",userlink)
                    .addResourceInfo("self", editlink)
                    .addResourceInfo("login",loginlink)
                    .addResourceInfo("reset",resetlink);

            return Response
                    .status(Response.Status.OK)
                    .entity(wrapperUser.toJson())
                    .build();
        }else{
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .build();
        }
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        if((loginRequest.username==null)||(loginRequest.username.length()==0)){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new GenericResponse("Username is empty", "400-4"))
                    .build();
        }


        User user = userDao.getUserByName(loginRequest.username);

        if( user == null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new GenericResponse("User not found!", "404-2"))
                    .build();
        }

        if(loginRequest.password==null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new GenericResponse("Password is empty", "400-4"))
                    .build();
        }
        if(PasswordAuthenticator.verifyPassword(loginRequest.password, user.getPassword(),user.getSalt())) {
            String token = TokenFactory.buildToken(user);

            return Response
                    .status(Response.Status.OK)
                    .entity(token)
                    .build();
        }else{
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .build();
        }
    }

    @OPTIONS
    @Produces(MediaType.TEXT_PLAIN)
    public Response getUserOptions() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        StringBuilder sb = new StringBuilder();
        String url = "/rest/user/";
        sb.append("------------- Allowed Operations for url ").append(url).append(" -------------").append('\n').append('\n');

        //POST INFORMATION
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("yourusername");
        userDTO.setCountryCode("IT");
        userDTO.setEmail("youremail@email.com");
        userDTO.setPassword("password");
        userDTO.setConfirmPassword("password");


        sb.append("Operation: CREATE NEW USER").append('\n');
        sb.append("Verb: POST").append('\n');
        sb.append("Auth: no authentication required").append('\n');
        sb.append("Params: username: String, email: String, password: String, confirmPassword: String, countryCode: String").append('\n');
        sb.append("Example request body: ").append(gson.toJson(userDTO)).append('\n');
        sb.append("username must be unique").append('\n');
        sb.append("email must be unique").append('\n');
        sb.append("countryCode (not required) must contain the country code of an EU state ").append('\n');
        sb.append("password and confirmPassword must be equal").append('\n');

        sb.append("--------------------------").append('\n').append('\n');

        url = "/rest/user/login";
        sb.append("------------- Allowed Operations for url ").append(url).append(" -------------").append('\n').append('\n');

        //LOGIN INFORMATION
        sb.append("Operation: LOGIN").append('\n');
        sb.append("Verb: POST").append('\n');
        sb.append("Auth: no authentication required").append('\n');
        sb.append("Params: username: String, password: String").append('\n');
        sb.append("Example request body: ").append("\n{\n\"username\":\"user\",\n\"password\":\"********\"\n}").append('\n');
        sb.append("username must be unique").append('\n');
        sb.append("email must be unique").append('\n');
        sb.append("countryCode (not required) must contain the country code of an EU state ").append('\n');
        sb.append("password and confirmPassword must be equal").append('\n');

        sb.append("--------------------------").append('\n').append('\n');

        url= "/rest/user/{id}";
        sb.append("------------- Allowed Operations for url ").append(url).append(" -------------").append('\n').append('\n');

        //GET SPECIFIC INFORMATION

        sb.append("Operation: GET SPECIFIC USER INFORMATION").append('\n');
        sb.append("Verb: GET").append('\n');
        sb.append("Auth: Bearer JWT").append('\n');
        sb.append("Role required: none").append('\n');
        sb.append("Params: id: long (in url)").append('\n');
        sb.append("Replace the {id} in the url with the id of the desired resource").append('\n');
        sb.append("WARNING: only logged in user can change its own properties").append('\n');
        sb.append("--------------------------").append('\n').append('\n');

        //PATCH SPECIFIC INFORMATION

        sb.append("Operation: UPDATE SPECIFIC USER").append('\n');
        sb.append("Verb: PATCH").append('\n');
        sb.append("Auth: Bearer JWT").append('\n');
        sb.append("Role required: none").append('\n');
        sb.append("Params: id: long (in url), countryCode: String").append('\n');
        sb.append("Example request body: ").append("{\"countryCode\":\"IT\"}").append('\n');
        sb.append("Replace the {id} in the url with the id of the desired resource").append('\n');
        sb.append("WARNING: other property cannot be modified. You must delete and create a new user to change them").append('\n');
        sb.append("WARNING: only logged in user can change its own properties").append('\n');
        sb.append("countryCode must contain the country code of an EU state ").append('\n');
        sb.append("--------------------------").append('\n').append('\n');

        url= "/rest/user/{id}/reset";
        sb.append("------------- Allowed Operations for url ").append(url).append(" -------------").append('\n').append('\n');
        //RESET USER PASSWORD
        sb.append("Operation: RESET USER PASSWORD ").append('\n');
        sb.append("Verb: PATCH").append('\n');
        sb.append("Auth: Bearer JWT").append('\n');
        sb.append("Role required: ADMIN").append('\n');
        sb.append("Params: id: long (in url), oldPassword: String, password: String, confirmPassword:String").append('\n');
        sb.append("Replace the {id} in the url with the id of the desired resource").append('\n');
        sb.append("Example request body: ").append("\n{\n\"oldPassword\":\"myCurrentpassword\",\n\"password\":\"myNewPassword\",\n\"confirmPassword\":\"myNewPassword\"\n}").append('\n');
        sb.append("WARNING: only logged in user can change its own properties").append('\n');
        sb.append("password and confirmPassword must be equal").append('\n');
        sb.append("password must contain the new password, while oldPassword the current one").append('\n');
        sb.append("--------------------------").append('\n').append('\n');


        return Response
                .status(Response.Status.OK)
                .header("Allow:","GET, POST, DELETE, PATCH, OPTIONS")
                .entity(sb.toString())
                .build();

    }


}
