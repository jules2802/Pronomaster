package servlets;

import entities.Player;
import entities.Match;
import entities.Pari;
import exceptions.MatchNotFoundException;
import exceptions.PariAlreadyExistsException;
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

@WebServlet("/espace/joueur/parier")
public class BetPlayerServlet extends CreateTemplateEngine {

    private static final Logger LOG = LoggerFactory.getLogger(BetPlayerServlet.class);

    String errormessage="";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        WebContext context = new WebContext(req, resp, req.getServletContext());

        List<Player> listOfUsers = UserLibrary.getInstance().getAllUsersOrderedByScore();
        int sizeList= listOfUsers.size();
        int sizeRanking;

        //si le nombre d'utilisateur est superieur a 10, le classement ne prends que 10 joueurs, sinon il prends le nombre de joueurs
        if (sizeList>10) {
            sizeRanking = 10;
        }else{sizeRanking=sizeList;}

        List<Player> listOfUserSmaller= new ArrayList<>();
        for(int i=0;i<sizeRanking;i++){
            listOfUserSmaller.add(listOfUsers.get(i));
        }

        //Recuperation des 3 derniers matches passés avec un score
        List<Match> listOfPassedMatches = MatchLibrary.getInstance().lastThreeMatchesWithScore();
        //On place dans le context
        context.setVariable("listOfPassedMatches",listOfPassedMatches);

        context.setVariable("userListSmaller", listOfUserSmaller);
        //On récupère le nom du joueur dans la session
        String userName= (String) req.getSession().getAttribute("userConnected");
        //Récupération des matchs où le joueur n'a pas parié sous la forme d'une liste
        List<Match> listOfUnbetMatchs = MatchLibrary.getInstance().listUnbetMatchs(userName);
        //On place la liste dans le contexte
        context.setVariable("listOfUnbetMatchs", listOfUnbetMatchs);

        context.setVariable("errorMessage",errormessage);
        context.setVariable("userName", userName);

        TemplateEngine engine= CreateTemplateEngine(req,resp);
        engine.process("/Player/BetPlayer", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //si on appuie sur le bouton enregistrer
        if (req.getParameter("enregistrer")!=null){
            //récupération de l'identifiant du match traité
            LOG.info("L'ulisateur souhaite ajouter un nouveau pari");
            int idMatch = Integer.parseInt(req.getParameter("enregistrer"));
            LOG.debug("L'identifiant ({}) du match sur lequel l'utilisateur pari est récupéré et converti en type int",idMatch);
            //Récupération du match correspondant
            Match match = null;
            try {
                match = MatchLibrary.getInstance().getMatchById(idMatch);
            } catch (MatchNotFoundException e) {
                e.printStackTrace();
            }
            LOG.debug("Le match parié \"{}-{}\" a bien été créé",match.getTeamIn().getName(),match.getTeamOut().getName());
            try {
                //récupération du but_in
                int butIn = Integer.parseInt(req.getParameter("score_equipe_in_"+idMatch));
                LOG.debug("L'utilisateur pari qu'il y aura {} buts pour l'équipe jouant à domicile",butIn);
                //récupération du but_out
                int butOut = Integer.parseInt(req.getParameter("score_equipe_out_"+idMatch));
                LOG.debug("L'utilisateur pari qu'il y aura {} buts pour l'équipe jouant à l'extérieur",butOut);
                if (butIn<0 || butOut<0){
                    errormessage="Il est impossible d'entrer un score négatif";
                }else{
                    //récupération du name de l'utilisateur
                    String userName= (String) req.getSession().getAttribute("userConnected");
                    LOG.debug("Récupération du nom de l'utilisateur dans la session : {}",userName);
                    //Création de l'objet Pari
                    Pari newPari= new Pari(null, match,butIn,butOut,userName);
                    LOG.info("L'objet pari a été créé");
                    //Ajout des données dans le match correspondant grâce à id_match
                    PariLibrary.getInstance().addPari(newPari);
                    LOG.info("le pari a été ajouté à la base de données");
                    errormessage="";
                }

            }catch(NumberFormatException e){
                LOG.error("NumberFormatException, l'utilisateur n'a pas renseigné des chiffres pour le score",e);
                errormessage="Vous devez renseigner des chiffres et rien d'autre";
            } catch (PariAlreadyExistsException e) {
                e.printStackTrace();
            }

            //Traitement terminé, redirection sur la page "Parier"
            LOG.info("Redirection vers la page \"Parie\"");
            resp.sendRedirect("/espace/joueur/parier");
        }


    }
}
