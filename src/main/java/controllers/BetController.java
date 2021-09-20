package controllers;

import entities.Pari;
import managers.PariLibrary;
import managers.UserLibrary;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/bets")
public class BetController {

    @GET
    @Path("/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pari> listUnPassedUserBets(@PathParam("userName") String userName) throws IOException {
        return PariLibrary.getInstance().listUnPassedUserBets(userName);
    }

    @DELETE
    @Path("/{userName}/{idBet}")
    public void deleteCity(@PathParam("userName") String userName,@PathParam("idBet") int idBet){
        PariLibrary.getInstance().deleteBet(userName,idBet);
    }
}
