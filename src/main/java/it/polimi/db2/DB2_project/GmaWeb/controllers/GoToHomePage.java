package it.polimi.db2.DB2_project.GmaWeb.controllers;

import it.polimi.db2.DB2_project.GmaEJB.Entities.Access;
import it.polimi.db2.DB2_project.GmaEJB.Entities.Product;
import it.polimi.db2.DB2_project.GmaEJB.Entities.User;
import it.polimi.db2.DB2_project.GmaEJB.Services.AccessBean;
import it.polimi.db2.DB2_project.GmaEJB.Services.ProductBean;
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
import java.time.LocalDate;

@WebServlet(name = "GoToHomePage", value = "/GoToHomePage")
public class GoToHomePage extends HttpServlet {
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/ProductBean")
    private ProductBean productBean;
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/AccessBean")
    private AccessBean accessBean;

    private TemplateEngine templateEngine;

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
        HttpSession session = request.getSession();
        String loginpath = getServletContext().getContextPath() + "/Login";

        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginpath);
            return;
        }
        User u = (User)request.getSession().getAttribute("user");
        Access access = accessBean.findAccess(LocalDate.now(), u);


        String path = "/home.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        Product product = productBean.findProductByDate(LocalDate.now());

        ctx.setVariable("product", product);
        // Variable to know if the user has already compiled or discarded a questionnaire
        ctx.setVariable("canCompile", access == null);

        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
