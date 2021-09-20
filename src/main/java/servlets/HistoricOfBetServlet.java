package servlets;

import entities.Match;
import entities.Pari;
import entities.Team;
import exceptions.MatchNotFoundException;
import exceptions.PariAlreadyExistsException;
import managers.MatchLibrary;
import managers.PariLibrary;
import managers.TeamLibrary;
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

@WebServlet("/espace/joueur/historiqueParis")
public class HistoricOfBetServlet extends CreateTemplateEngine {

    private static final Logger LOG = LoggerFactory.getLogger(HistoricOfBetServlet.class);
    String errormessage = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        WebContext context = new WebContext(req, resp, req.getServletContext());

        String idUser = (String) req.getSession().getAttribute("userConnected");
        LOG.debug("Nom de l'utilisateur {} récupéré", idUser);
        List<Pari> listParisPassedMatchs = PariLibrary.getInstance().listParisPassedMatchs(idUser);
        LOG.debug("Creation liste des paris passés : {}", listParisPassedMatchs);

        for (int i=0; i<listParisPassedMatchs.size(); i++){
            System.out.println(listParisPassedMatchs.get(i).getIdPari());
        }

        List<Team> listOfTeams = TeamLibrary.getInstance().getAllTeams();
        context.setVariable("teamList", listOfTeams);
        LOG.info("liste des équipes placées dans le template");


        if (req.getParameter("searchBet")!=null){
            LOG.debug("l'utilisateur a appuyé sur le bouton \"{}\"",req.getParameter("searchBet"));
            String filtreTeam = req.getParameter("team");
            String filtreDate = req.getParameter("date");
            String filtreResultAsString = req.getParameter("result");
            int filtreResult=2;
            if (filtreResultAsString.equals("victory")){
                filtreResult=1;
            }else if (filtreResultAsString.equals("defeat")){
                filtreResult=0;
            }
            LOG.debug("les paramètres de filtrage de la recherche équipe : {}, date : {} et résultat ont été récupéré",filtreTeam,filtreDate);

            listParisPassedMatchs = PariLibrary.getInstance().searchBet(idUser,filtreTeam,filtreDate,filtreResult);

            LOG.debug("La liste des paris correspondant à la recherche a été créée, taille : {}", listParisPassedMatchs.size());

            if(listParisPassedMatchs.size()==0){
                errormessage="Il n'y a aucun pari lié à cette recherche";
            }

            context.setVariable("Error message", errormessage);

        }

        context.setVariable("listParisPassedMatchs",listParisPassedMatchs);
        LOG.info("La liste des paris à afficher a été placée dans la requête");

        context.setVariable("userName", idUser);

        TemplateEngine engine= CreateTemplateEngine(req,resp);
        engine.process("/Player/HistoricOfBet", context, resp.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //si on appuie sur le bouton enregistrer
        if (req.getParameter("enregistrer")!=null){
            //récupération de l'identifiant du match traité
            int idMatch = Integer.parseInt(req.getParameter("enregistrer"));
            LOG.info("récupération de l'identifiant du match traité");
            //Récupération du match correspondant
            Match match = null;
            try {
                match = MatchLibrary.getInstance().getMatchById(idMatch);
            } catch (MatchNotFoundException e) {
                e.printStackTrace();
            }
            LOG.debug("Récupération du match correspondant : {}", match);
            try {
                int resultat = Integer.parseInt(req.getParameter("resultat"));
                //récupération du but_in
                int butIn = Integer.parseInt(req.getParameter("score_equipe_in"));
                LOG.debug("Recuperation du pari de l'utilisateur pour le nombre de but de l'equipe à domicile : {}",butIn);
                //récupération du but_out
                int butOut = Integer.parseInt(req.getParameter("score_equipe_out"));
                LOG.debug("Recuperation du pari de l'utilisateur pour le nombre de but de l'equipe exterieure : {}",butOut);
                //récupération du name de l'utilisateur
                String userName= (String) req.getSession().getAttribute("userConnected");
                LOG.debug("Utilsateur est connecte : {}", userName);

                Pari newPari= new Pari(null, match,butIn,butOut,userName);
                LOG.debug("Creation du nouveau pari : {}", newPari);
                //Ajout des données dans le match correspondant grâce à id_match
                PariLibrary.getInstance().addPari(newPari);
                LOG.info("Ajout du nouveau pari dans la librairie");
                errormessage="";
            }catch(NumberFormatException e){
                LOG.error("Le format n'est pas accepté",e);
                errormessage="Vous devez renseigner des chiffres et rien d'autre";
            } catch (PariAlreadyExistsException e) {
                e.printStackTrace();
            }

            //Traitement terminé, redirection sur la page "Parier"
            LOG.info("Redirection vers la page \"historiqueParis\"");
            resp.sendRedirect("historiqueParis");
        }
    }
}
