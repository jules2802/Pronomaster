package servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deconnexion")
public class LogOutServlet extends CreateTemplateEngine {

    private static final Logger LOG = LoggerFactory.getLogger(LogOutServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = (String) req.getSession().getAttribute("userConnected");
        LOG.debug("Suppression de l'attribut \"{}\" de la session",userName);
        req.getSession().removeAttribute("userConnected");
        LOG.info("Redirection de l'utilisateur vers la page d'accueil");
        resp.sendRedirect("accueil");
    }
}
