package ru.sbrf.rest;

import org.json.JSONException;
import org.json.JSONObject;
import ru.sbrf.controller.AdminClientService;
import ru.sbrf.controller.DatabaseService;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * Created by kozmi on 2016-01-08.
 */
@Singleton
@Path("/monitor")
public class RestFulService{

    @EJB(name = "dbs")
    private DatabaseService dbs;

    @EJB(name = "acs")
    private AdminClientService acs;

    @GET
    @Produces("application/json")
    public Response getAllSevers(){
        Response.ResponseBuilder resp;
        String result;
        try {
            result = dbs.getServers();
            resp = Response.ok(result, MediaType.APPLICATION_JSON_TYPE);
        } catch (Exception e) {
            System.out.println("Exception getting all servers"+e.getMessage());
            e.printStackTrace();
            resp = Response.serverError();
        }
        return resp.build();
    }

    @POST
    @Path("/{username}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response addServer(@PathParam("username") String username, String body) throws JSONException{
        Response.ResponseBuilder resp;
        JSONObject jsonObject = new JSONObject(body);
        String host = jsonObject.getString("host");
        Integer port = jsonObject.getInt("port");
        UUID id;
        try {
            id = dbs.addServer(username, host, port);
            resp = Response.ok(id, MediaType.APPLICATION_JSON_TYPE);
        } catch (Exception e) {
            System.out.println("Exception adding server"+e.getMessage());
            e.printStackTrace();
            resp = Response.serverError();
        }
        return resp.build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeServer(@PathParam("id") String id){
        Response.ResponseBuilder resp;
        try {
            dbs.removeServer(id);
            resp = Response.ok();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            resp = Response.serverError();
        }
        return resp.build();
    }

    @GET
    @Path("/{username}")
    @Produces("application/json")
    public Response getServersByUser(@PathParam("username") String username){
        Response.ResponseBuilder resp;
        String jsonResp;
        try {
            jsonResp = dbs.getServerByUser(username);
            resp = Response.ok(jsonResp, MediaType.APPLICATION_JSON_TYPE);
        } catch (Exception e) {
            System.out.println("Exception getting servers by user"+e.getMessage());
            e.printStackTrace();
            resp = Response.serverError();
        }
        return resp.build();
    }

    @GET
    @Path("/test/test")
    @Produces("application/json")
    public Response test(){
        Response.ResponseBuilder resp;
        String response = null;
        try {
            response = acs.test();
            resp = Response.ok(response, MediaType.APPLICATION_JSON_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
            resp = Response.serverError();
        }
        return resp.build();
    }

    @POST
    @Path("server/info")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getServerInfo(String serversArr){
        Response.ResponseBuilder resp;
        try {
            String res = acs.getServerInfo(serversArr);
            resp = Response.ok(res, MediaType.APPLICATION_JSON_TYPE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            resp = Response.serverError();
        }

        return resp.build();
    }


}

