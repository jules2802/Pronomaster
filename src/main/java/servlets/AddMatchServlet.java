package servlets;

import entities.Team;
import entities.Match;
import exceptions.MatchAlreadyExistsException;
import exceptions.TeamNotFoundException;
import managers.TeamLibrary;
import managers.MatchLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@WebServlet("/espace/administrateur/ajoutmatch")
public class AddMatchServlet extends CreateTemplateEngine {

    private static final Logger LOG = LoggerFactory.getLogger(AddMatchServlet.class);

    String message = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        WebContext context = new WebContext(req, resp, req.getServletContext());

        String userName = (String) req.getSession().getAttribute("userConnected");
        context.setVariable("userName", userName);

        List<Team> listOfTeams = TeamLibrary.getInstance().getAllTeams();
        context.setVariable("teamList", listOfTeams);

        List<Match> listOfUnpassedMatches = MatchLibrary.getInstance().listMatchUnpassedMatchs();
        context.setVariable("UnpassedMatchsList", listOfUnpassedMatches);

        context.setVariable("errorMessage", message);

        TemplateEngine engine = CreateTemplateEngine(req, resp);
        engine.process("/Administrator/AddMatch", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("ajouterMatch") != null) {

            //Récupération de l'id de l'équipe jouant à domicile
            String idInAsString = req.getParameter("teamIn");
            int idIn = Integer.parseInt(idInAsString);
            LOG.debug("Récupération de l'id de l'équipe à domicile : {}", idInAsString);

            //Création de l'équipe correspondante
            Team teamIn = null;
            try {
                teamIn = TeamLibrary.getInstance().getTeamWithId(idIn);
            } catch (TeamNotFoundException e) {
                e.printStackTrace();
            }
            LOG.debug("Création de l'objet Team de l'équipe à domicile : {}", teamIn.getName());

            //Récupération de l'équipe jouant à l'extérieur
            String idOutAsString = req.getParameter("teamOut");
            int idOut = Integer.parseInt(idOutAsString);
            LOG.debug("Récupération de l'id de l'équipe à l'extérieur : {}", idOutAsString);

            //Création de l'équipe correspondante
            Team teamOut = null;
            try {
                teamOut = TeamLibrary.getInstance().getTeamWithId(idOut);
            } catch (TeamNotFoundException e) {
                e.printStackTrace();
            }
            LOG.debug("Création de l'objet Team de l'équipe à l'extérieur : {}", teamOut.getName());


            //Récupération de la date et conversion en LocalDate
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            LocalDate date = null;
            try {
                Date dateAsDate = formatter.parse(req.getParameter("date"));
                date = dateAsDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LOG.info("Récupération et formattage de la date réussi");
            } catch (ParseException e) {
                LOG.error("ParseException apparue lors du formattage de la date", e);
                e.printStackTrace();
                message = "Un problème interne est survenu, veuillez rééssayer";
            }

            //Création du match à ajouter
            Match matchToAdd = new Match(teamIn, teamOut, date);
            LOG.debug("Le match à ajouter \"{}-{}\" a bien été créé", matchToAdd.getTeamIn().getName(), matchToAdd.getTeamOut().getName());

            //Erreur si les deux équipes sélectionnées sont identique
            if (idIn == idOut) {
                //affige du message d'erreur
                message = "Vous ne pouvez pas ajouter un match où s'affronte la même équipe";
                LOG.error("L'utilisateur a essayé d'ajouter un match où une même équipe s'affronte");

            } else {
                //suppression du message d'erreur
                message = "";
                //Ajout du match dans la base de données
                try {
                    MatchLibrary.getInstance().addMatch(matchToAdd);
                } catch (NullPointerException e) {
                    LOG.error("L'utilisateur n'a pas renseigné la date", e);
                    message = "Vous n'avez pas renseigné la date";
                } catch (MatchAlreadyExistsException e) {
                    e.printStackTrace();
                }

            }
            //Redirection sur la page ajouter match
            resp.sendRedirect("/espace/administrateur/ajoutmatch");


            LOG.info("L'utilisateur est redirigé vers la page \"ajoutmatch\"");
        }
    }
}
