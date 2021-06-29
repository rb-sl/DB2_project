package it.polimi.db2.DB2_project.GmaWeb.controllers;

import it.polimi.db2.DB2_project.GmaEJB.Entities.*;
import it.polimi.db2.DB2_project.GmaEJB.Services.AccessBean;
import it.polimi.db2.DB2_project.GmaEJB.Services.ProductBean;
import it.polimi.db2.DB2_project.GmaEJB.Services.QuestionnaireBean;
import it.polimi.db2.DB2_project.GmaEJB.Services.UserBean;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "GoToHomePage", value = "/GoToHomePage")
public class GoToHomePage extends HttpServlet {
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/ProductBean")
    private ProductBean productBean;
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/AccessBean")
    private AccessBean accessBean;
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/QuestionnaireBean")
    private QuestionnaireBean questionnaireBean;
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/UserBean")
    private UserBean userBean;

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
        String loginpath = getServletContext().getContextPath() + "/index.html";
        List<Question> questions = null;
        List<Access> accesses = null;

        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginpath);
            return;
        }
        User u = (User)request.getSession().getAttribute("user");
        Access access = accessBean.findAccessByUser(LocalDate.now(), u);

        String path = "/home.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        Product product = productBean.findProductByDate(LocalDate.now());

        ctx.setVariable("product", product);
        // Variable to know if the user has already compiled or discarded a questionnaire
        ctx.setVariable("canCompile", access == null);

        ctx.setVariable("isBanned", u.getBanned());

        // Review variables
        Questionnaire questionnaire = questionnaireBean.findQuestionnaireByDate(LocalDate.now());
        if (questionnaire != null) {
            questions = questionnaire.getQuestions();
            accesses = questionnaire.getSubmitted();
        }

        ctx.setVariable("questions", questions);
        ctx.setVariable("accesses", accesses);

        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
