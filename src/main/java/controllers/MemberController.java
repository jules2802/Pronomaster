package controllers;

import entities.User;
import exceptions.UserNotFoundException;
import managers.UserLibrary;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/members")
public class MemberController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listUser() throws IOException {
        return UserLibrary.getInstance().getAllUsersOrderedByStatus();
    }

    @GET
    @Path("/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public User user(@PathParam("userName") String userName) throws IOException, UserNotFoundException {
        return UserLibrary.getInstance().getUser(userName);
    }

    @PATCH
    @Path("/{userName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response changeRole(@PathParam("userName") String userName) throws IOException {
        try {
            UserLibrary.getInstance().changeRole(userName);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @DELETE
    @Path("/{userName}")
    public void deleteCity(@PathParam("userName") String userName) throws IOException {
        UserLibrary.getInstance().deleteUser(userName);
    }

    @GET
    @Path("/search/name/{keyboard}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> searchUser(@PathParam("keyboard") String keyboard) throws IOException {
        return UserLibrary.getInstance().searchUser(keyboard);
    }

    @GET
    @Path("/search/role/{role}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> searchUserWithRole(@PathParam("role") Integer role){
        return UserLibrary.getInstance().searchUserWithRole(role);
    }
}
