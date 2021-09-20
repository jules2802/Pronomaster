package servlets;

import entities.User;
import exceptions.UserNotFoundException;
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

@WebServlet("/espace/administrateur/gerermembres")
public class ManageMemberServlet extends CreateTemplateEngine {

    private static final Logger LOG = LoggerFactory.getLogger(ManageMemberServlet.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        WebContext context = new WebContext(req, resp, req.getServletContext());

        /*//Récupération du nom de l'utilisateur dans la session
        String name = (String) req.getSession().getAttribute("userConnected");
        User user = null;
        try {
            user = UserLibrary.getInstance().getUser(name);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        context.setVariable("user", user);

        //Récupération de la liste des utilisateurs
        List<User> users = UserLibrary.getInstance().getAllUsersOrderedByStatus();

        //Suppression de l'user de la session de la liste des utilisateurs
        for (int i=0; i<users.size();i++){
            LOG.debug("Le joueur : {}  |  moi : {}",users.get(i).getName(),name);
            if (users.get(i).getName().equalsIgnoreCase(name)){
                users.remove(i);
            }
        }

        //Si on lance une rehcerche...
        if (req.getParameter("searchUser")!=null){
            //Récupération de la requête
            String filtre = req.getParameter("filtre");
            //Si la requête n'est pas vide (pour éviter le cas où l'utilisateur appuye par erreur par exemple)
            if (filtre.equals("")==false){
                users=UserLibrary.getInstance().searchUser(filtre);
                context.removeVariable("user");
            }
        }

        context.setVariable("users", users);*/

        String userName = (String) req.getSession().getAttribute("userConnected");
        context.setVariable("userName", userName);

        TemplateEngine engine = CreateTemplateEngine(req, resp);
        engine.process("/Administrator/ManageMember", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*String name = (String) req.getSession().getAttribute("userConnected");
        List<User> allUsers= UserLibrary.getInstance().getAllUsersOrderedByStatus();
        int adminNumber=0;

        for(int i=0;i<allUsers.size();i++){
            if(allUsers.get(i).getAdmin()==1){
                adminNumber++;
            }
        }

        if (req.getParameter("changerStatu")!=null) {
            String nom = req.getParameter("changerStatu");
            int role= UserLibrary.getInstance().getRole(nom);


            if ( (adminNumber > 1 && role==0) || (adminNumber>1 && role==1) || (adminNumber==1 && role==0)) {
                UserLibrary.getInstance().changeRole(nom);
            LOG.debug("Le rôle de l'utilisateur \"{}\" a été changé",nom);
                if (nom.equals(name)) {
                    resp.sendRedirect("/espace/joueur/mesparis");
                }else {
                    resp.sendRedirect("/espace/administrateur/gerermembres");
                }
            }
        }else if (req.getParameter("banir")!=null) {
                String nom = req.getParameter("banir");
                UserLibrary.getInstance().deleteUser(nom);
            LOG.debug("L'utilisateur \"{}\" a été supprimé",nom);
                resp.sendRedirect("/espace/administrateur/gerermembres");
        }*/

    }
}
