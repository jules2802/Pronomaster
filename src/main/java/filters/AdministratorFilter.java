package filters;

import managers.UserLibrary;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdministratorFilter implements Filter {
    @Override
    public void destroy() {

    }

    @Override
public void init(FilterConfig filterConfig) {

}

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;

        String identifiant = (String) httpRequest.getSession().getAttribute("userConnected");
        int role= UserLibrary.getInstance().getRole(identifiant);

        if(role==0) {
            HttpServletResponse httpResponse = (HttpServletResponse) resp;
            httpResponse.sendRedirect("/espace/Player/BetPlayer");
            return;
        }
        filterChain.doFilter(req, resp);
    }
}
