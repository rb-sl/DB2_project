package it.polimi.db2.DB2_project.GmaWeb.controllers;

import it.polimi.db2.DB2_project.GmaEJB.Entities.User;
import it.polimi.db2.DB2_project.GmaEJB.Services.UserBean;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(name = "Signup", value = "/Signup")
public class Signup extends HttpServlet {
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/UserBean")
    private UserBean userBean;

    private TemplateEngine templateEngine;

    public Signup() {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String path;

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        if(user == null || user.isEmpty()
                || password == null || password.isEmpty()
                || email == null || email.isEmpty()) {
            ctx.setVariable("errorMsg", "Invalid username, password or email");
            path = "/signup.html";
            templateEngine.process(path, ctx, response.getWriter());
            return;
        }

        String message = "";

        if(isValid(email)) {
            if(!userBean.isNameTaken(user)) {
                User newUser = userBean.createUser(user, password, email);
                request.getSession().setAttribute("user", newUser);
                response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
                return;
            }
            else {
                message = "Username already taken";
            }
        }
        else {
            message = "Wrong email format";
        }

        ctx.setVariable("errorMsg", message);
        path = "/signup.html";
        templateEngine.process(path, ctx, response.getWriter());
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
