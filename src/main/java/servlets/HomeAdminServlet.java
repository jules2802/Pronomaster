package servlets;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/espace/administrateur/accueiladmin")
public class HomeAdminServlet extends CreateTemplateEngine {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        WebContext context = new WebContext(req, resp, req.getServletContext());

        String userName = (String) req.getSession().getAttribute("userConnected");
        context.setVariable("userName", userName);

        TemplateEngine engine= CreateTemplateEngine(req,resp);
        engine.process("/Administrator/HomeAdmin", context, resp.getWriter());
    }
}