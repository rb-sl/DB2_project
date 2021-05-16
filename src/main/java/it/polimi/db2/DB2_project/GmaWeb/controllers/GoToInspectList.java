package it.polimi.db2.DB2_project.GmaWeb.controllers;

import it.polimi.db2.DB2_project.GmaEJB.Entities.*;
import it.polimi.db2.DB2_project.GmaEJB.Services.AccessBean;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "GoToInspectList", value = "/GoToInspectList")
public class GoToInspectList extends HttpServlet {
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/QuestionnaireBean")
    private QuestionnaireBean questionnaireBean;

    private TemplateEngine templateEngine;
    private Map<Integer, String> answ = new HashMap<Integer, String>();

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
        String loginpath = getServletContext().getContextPath() + "/Login";
        String homepath = getServletContext().getContextPath() + "/GoToHomePage";

        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginpath);
            return;
        }
        User u = (User) request.getSession().getAttribute("user");

        if (!u.getIsAdmin()) {
            response.sendRedirect(homepath);
            return;
        }

        List<Questionnaire> questionnaireList = questionnaireBean.findAll();

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        ctx.setVariable("questionnaireList", questionnaireList);

        Object error = request.getAttribute("errorMsg");
        if(error != null) {
            ctx.setVariable("errorMsg", "The requested questionnaire does not exist");
        }

        String path = "/inspectList";
        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}