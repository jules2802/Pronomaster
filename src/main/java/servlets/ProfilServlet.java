package servlets;

import entities.Player;
import managers.UserLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import utils.MotDePasseUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/espace/joueur/profil")
@MultipartConfig
public class ProfilServlet extends CreateTemplateEngine {

    private static final Logger LOG = LoggerFactory.getLogger(ProfilServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        WebContext context = new WebContext(req, resp, req.getServletContext());

        String name = (String) req.getSession().getAttribute("userConnected");
        Player player = UserLibrary.getInstance().getPlayer(name);
        LOG.info("Le joueur de la session, {} a bien été récupéré.",player.getName());
        context.setVariable("player", player);

        context.setVariable("userName", name);

        //Récupération du lien dans la base de données
        String url = UserLibrary.getInstance().getUrlProfil(name);
        LOG.info("URL de la photo de profil de l'utilisateur : {}",url);
        if (url==null){
            url="../../../images/absence-photo.png";
        }
        //On met le lien dans le contexte
        context.setVariable("url", url);
        LOG.info("URL lié à l'image de profil : {}",url);
        TemplateEngine engine = CreateTemplateEngine(req, resp);
        engine.process("/Player/ProfilJoueur", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       if(req.getParameter("profilePicture")!=null) {
           LOG.info("L'utilisateur appuie sur le bouton {}.",req.getParameter("profilePicture"));
           //Récupération du fichier
           Part fileForm = req.getPart("photoProfil");
           LOG.info("Récupération de la photo sur la form d'un objet part : {}",fileForm.getName());
           //Récupération du name de l'utilisateur
           String name = (String) req.getSession().getAttribute("userConnected");
           //Confection de l'url
           String url = "/Users/paulmathon/Documents/HEI/HEI 4/Projet/pronomaster/pronomaster/data/images/photo-profil/"+name+".jpg";
           //Placement de l'image dans le répertoire images/photo-profil
           fileForm.write(url);
           //Enregistrement du lien dans la base de donnée
           UserLibrary.getInstance().addUrlProfil(name, "/Users/paulmathon/Documents/HEI/HEI%204/Projet/pronomaster/pronomaster/data/images/photo-profil/"+name+".jpg");
           //Redirection vers profil
           resp.sendRedirect("/espace/joueur/profil");
       }
        //Modification du mot de passe
        if(req.getParameter("changePassword")!=null) {
            LOG.info("L'utilisateur appuie sur le bouton {}.", req.getParameter("changePassword"));
            String password = req.getParameter("password");
            UserLibrary.getInstance().updatePassword((String) req.getSession().getAttribute("userConnected"), MotDePasseUtils.genererMotDePasse(password));
            resp.sendRedirect("/espace/joueur/profil");
        }
    }
}
