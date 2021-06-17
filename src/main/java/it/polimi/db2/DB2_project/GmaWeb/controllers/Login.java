package it.polimi.db2.DB2_project.GmaWeb.controllers;

import it.polimi.db2.DB2_project.GmaEJB.Entities.User;
import it.polimi.db2.DB2_project.GmaEJB.Services.UserBean;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/UserBean")
    private UserBean userBean;

    private TemplateEngine templateEngine;

    public Login() {
        super();
    }

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = null;
        String password = null;
        User user = null;

        try {
            username = StringEscapeUtils.escapeJava(request.getParameter("username"));
            password = StringEscapeUtils.escapeJava(request.getParameter("password"));

            if(username == null || username.isEmpty()
                    || password == null || password.isEmpty())
                throw new Exception("Missing user/password");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user/password");
            e.printStackTrace();
            return;
        }

        try {
            user = userBean.checkCredentials(username, password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String path;
        if (user == null) {
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("errorMsg", "Incorrect username or password");
            path = "/index.html";
            templateEngine.process(path, ctx, response.getWriter());
        } else {
            request.getSession().setAttribute("user", user);
            String target = (user.getIsAdmin()) ? "/admin.html" : "/GoToHomePage";
            path = getServletContext().getContextPath() + target;
            response.sendRedirect(path);
        }
    }
}
