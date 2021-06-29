package it.polimi.db2.DB2_project.GmaWeb.controllers;

import it.polimi.db2.DB2_project.GmaEJB.Entities.Access;
import it.polimi.db2.DB2_project.GmaEJB.Entities.Questionnaire;
import it.polimi.db2.DB2_project.GmaEJB.Entities.User;
import it.polimi.db2.DB2_project.GmaEJB.Services.AccessBean;
import it.polimi.db2.DB2_project.GmaEJB.Services.QuestionnaireBean;
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

@WebServlet(name = "GoToQuestionnaire", value = "/GoToQuestionnaire")
public class GoToQuestionnaire extends HttpServlet {
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/QuestionnaireBean")
    private QuestionnaireBean questionnaireBean;
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
        Questionnaire questionnaire = questionnaireBean.findQuestionnaireByDate(LocalDate.now());
        String loginpath = getServletContext().getContextPath() + "/index.html";
        String homepath = getServletContext().getContextPath() + "/GoToHomePage";
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginpath);
            return;
        }
        User u = (User)request.getSession().getAttribute("user");
        Access access = accessBean.findAccessByUser(LocalDate.now(), u);
            if(access != null) {
                //todo:set message on homepage
                response.sendRedirect(homepath);
                return;
        }
        String path = "/questionnaire.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());



        ctx.setVariable("questions", questionnaire.getQuestions());
        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
