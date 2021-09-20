package servlets;

import entities.Match;
import entities.Pari;
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
import java.util.List;

@WebServlet("/espace/administrateur/entrerscore")
public class PutScoreMatchServlet extends CreateTemplateEngine {

    private static final Logger LOG = LoggerFactory.getLogger(PutScoreMatchServlet.class);
    String errormessage = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        WebContext context = new WebContext(req, resp, req.getServletContext());

        List<Match> listOfPassedMathsWithoutScore = MatchLibrary.getInstance().listPassedMatchsWithoutScore();
        context.setVariable("listOfPassedMathsWithoutScore", listOfPassedMathsWithoutScore);

        List<Match> listOfPassedMatchsWithScore = MatchLibrary.getInstance().listPassedMatchsWithScore();
        context.setVariable("listOfPassedMatchsWithScore", listOfPassedMatchsWithScore);

        context.setVariable("errormessage", errormessage);

        String userName = (String) req.getSession().getAttribute("userConnected");
        context.setVariable("userName", userName);

        TemplateEngine engine = CreateTemplateEngine(req, resp);
        engine.process("/Administrator/PutScoreMatch", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //si on appuie sur le bouton...
        if (req.getParameter("entrerScore") != null) {
            //récupération de l'identifiant du match traité
            int idMatch = Integer.parseInt(req.getParameter("entrerScore"));
            LOG.debug("récupération de l'identifiant du match traité",idMatch);
            try {
                //récupération du but_in
                int butIn = Integer.parseInt(req.getParameter("butIn"));
                LOG.debug("récupération de but in",butIn);
                //récupération du but_out
                int butOut = Integer.parseInt(req.getParameter("butOut"));
                LOG.debug("récupération de but out",butOut);
                if (butIn>=0 && butOut>=0){
                    //Ajout des données dans le match correspondant grâce à id_match
                    MatchLibrary.getInstance().updateScore(idMatch, butIn, butOut);
                    LOG.info("Recuperation des données de updateScore reussi");
                    errormessage="";
                // Mise à jour des scores chez chaque utilisateur
                //Recuperation de tous les paris associé au match
                // , si resultatMatch<0 alors out gagne
                List<Pari> listAllParisByIdMatch = PariLibrary.getInstance().listAllParisByIdMatch(idMatch);

                //je verifie qui a gagne, si resultatMatch>0 alors butin a gagne
                int resultatMatch = butIn - butOut;

                for (int i = 0; i < listAllParisByIdMatch.size(); i++) {
                    Pari pari = listAllParisByIdMatch.get(i);
                    LOG.info("On recupere tous les paris liste par l'id des matches");
                    int scoreIn = pari.getButIn();
                    int scoreOut = pari.getButOut();
                    if (((scoreIn - scoreOut) > 0 && resultatMatch > 0) || ((scoreIn - scoreOut) < 0 && resultatMatch < 0) || ( (scoreIn-scoreOut)==0 && resultatMatch==0)) {
                        UserLibrary.getInstance().updateScore(pari.getIdUser());
                        PariLibrary.getInstance().updateResultat(pari.getIdPari(), true);
                    } else {
                        PariLibrary.getInstance().updateResultat(pari.getIdPari(), false);
                    }

                }
                }else{
                    errormessage="Le score doit être composé de chiffres positifs";
                }

            }catch(NumberFormatException e){
                LOG.error("Le format choisi n'est pas approprié", e);
                e.printStackTrace();
                errormessage="Vous devez renseigner des chiffres et rien d'autre";
            }

            //Mise à jour des scores


            //Traitement terminé, redirection sur la page "Entrer Score"
            resp.sendRedirect("/espace/administrateur/entrerscore");
            LOG.info("L'utilisateur est redirigé vers la page \"entrerscore\"");
        }
    }
}