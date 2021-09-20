package servlets;

import entities.Match;
import entities.Pari;
import entities.Player;
import managers.MatchLibrary;
import managers.PariLibrary;
import managers.UserLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/espace/joueur/mesparis")
public class MyBetsServlet extends CreateTemplateEngine {


    private static final Logger LOG = LoggerFactory.getLogger(MyBetsServlet.class);
    String message = "";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("errorMessage",message);

        //affiche la liste des paris réalisés par l'utilisateur connecté
        String user_name= (String) req.getSession().getAttribute("userConnected");
        LOG.info("Recuperation utilisateur connecte réussi");
        List<Pari> listOfParis = PariLibrary.getInstance().listUnPassedUserBets(user_name);
        context.setVariable("parislist", listOfParis);
        LOG.debug("Affichage de la liste des paris : {}",listOfParis);

        //Recuperation des 3 derniers matches passés avec un score
        List<Match> listOfPassedMatches = MatchLibrary.getInstance().lastThreeMatchesWithScore();
        LOG.info("Recuperation des 3 derniers matches réussi");

        //On place dans le context
        context.setVariable("listOfPassedMatches",listOfPassedMatches);

        List<Player> listOfUsers = UserLibrary.getInstance().getAllUsersOrderedByScore();
        LOG.info("Recuperation des utilisateurs ordonné par score réussi");
        int sizeList= listOfUsers.size();
        int sizeClassement;

        //si le nombre d'utilisateur est superieur a 10, le classement ne prends que 10 joueurs, sinon il prends le nombre de joueurs
        if (sizeList>10) {
            sizeClassement = 10;
            LOG.info("On limite la taille du classement à 10");
        }else{sizeClassement=sizeList;}

        List<Player> listOfUserSmaller= new ArrayList<>();
        for(int i=0;i<sizeClassement;i++){
            listOfUserSmaller.add(listOfUsers.get(i));
            LOG.debug("Ajout des meilleurs joueurs dans la liste réduite : {}",listOfUserSmaller);
        }

        context.setVariable("userListSmaller", listOfUserSmaller);

        context.setVariable("userName", user_name);

        TemplateEngine engine= CreateTemplateEngine(req,resp);
        engine.process("/Player/MyBets", context, resp.getWriter());
    }
}
