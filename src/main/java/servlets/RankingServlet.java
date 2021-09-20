package servlets;

import entities.Player;
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
import java.util.List;

@WebServlet("/espace/joueur/classement")
public class RankingServlet extends CreateTemplateEngine {

    private static final Logger LOG = LoggerFactory.getLogger(RankingServlet.class);
    String message = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("errorMessage",message);


        List<Player> listOfUser = UserLibrary.getInstance().getAllUsersOrderedByScore();
        LOG.info("Recuperation de la liste des utilisateurs ordonnée par score");
        context.setVariable("userList", listOfUser);

        String currentUser = (String) request.getSession().getAttribute("userConnected");
        context.setVariable("userName", currentUser);

        System.out.println(currentUser);

        TemplateEngine engine= CreateTemplateEngine(request,response);
        engine.process("/Player/Ranking", context, response.getWriter());
    }


}
