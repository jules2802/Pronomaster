package servlets;

import entities.User;
import exceptions.UserAlreadyExistsException;
import managers.UserLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.MotDePasseUtils;
import entities.Player;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/accueil")
public class HomeServlet extends CreateTemplateEngine {

    private static final Logger LOG = LoggerFactory.getLogger(HomeServlet.class);
    String message = "";

    private String erreurInscription = "";
    private String erreurConnexion = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("erreurInscription", erreurInscription);//peut-on supprimer ca?
        context.setVariable("erreurConnexion", erreurConnexion);

        TemplateEngine engine = CreateTemplateEngine(req, resp);
        engine.process("Home", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String form= req.getParameter("connexion");

        //Si l'utilisateur  appuie sur "inscription"
        if (form.equals("inscription")) {

            //Get Parameter
            String name = req.getParameter("name");
            String password1 = req.getParameter("mdp1");
            String password2 = req.getParameter("mdp2");
            String mail1 = req.getParameter("mail1");
            String mail2 = req.getParameter("mail2");
            LOG.info("Aucun problème lié au renseignement des champs n'est survenu");
            LOG.info("Les paramètres nécessaires à l'inscription ont été récupéré");

            User newJoueur = new Player(name, MotDePasseUtils.genererMotDePasse(password1), mail1);

            try {
                User createdUser = UserLibrary.getInstance().addUser(newJoueur);
            } catch (UserAlreadyExistsException e) {
                e.printStackTrace();
            }
            LOG.debug("Le nom du nouvel inscrit ({}) est placé dans la session",newJoueur.getName());
            req.getSession().setAttribute("userConnected", name);
            LOG.debug("Redirection du nouvel inscrit {} vers la page parier",newJoueur.getName());
            resp.sendRedirect("/espace/joueur/parier");
        }

        if(form.equals("connexion")) {
            String userForm = req.getParameter("name");
            String passwordForm = req.getParameter("mdp");
            LOG.info("Récupération des informations de connexion de l'utilisateur");
            if (userForm.equals("") || "".equals(userForm) || passwordForm.equals("") || "".equals(passwordForm)) {
                erreurConnexion="Identifiant ou mot de passe incorrect";
                LOG.info("L'identifiant ou le mot de passe de l'utilisateur est incorrect");
                resp.sendRedirect("accueil");
            } else {
                try{
                    if (UserLibrary.getInstance().validerMotDePasse(userForm, passwordForm)) {
                        erreurConnexion = "";
                        int statuts = UserLibrary.getInstance().getRole(userForm);
                        req.getSession().setAttribute("userConnected", userForm);
                        LOG.debug("Connexion de l'utilisateur {} réussie", userForm);
                        if (statuts == 0) {
                            LOG.debug("Redirection de l'utilisateur {} vers la page \"Joueur\"", userForm);
                            resp.sendRedirect("/espace/joueur/parier");

                        } else if (statuts == 1 || statuts==2) {
                            LOG.debug("Redirection de l'utilisateur {} vers la page \"Administrateur\"", userForm);
                            resp.sendRedirect("/espace/administrateur/accueiladmin");
                        }
                    } else {
                        erreurConnexion = "Mot de passe incorrect";
                        LOG.debug("Mot de passe de l'utilisateur {} incorrecte",userForm);
                        resp.sendRedirect("accueil"); //il faut rajouter un message d'erreur encore
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                    LOG.error("L'identifiant \"{}\" est incorrect",userForm,e);
                    erreurConnexion="Identifiant incorrect";
                    resp.sendRedirect("accueil");
                }
            }
        }
    }
}
