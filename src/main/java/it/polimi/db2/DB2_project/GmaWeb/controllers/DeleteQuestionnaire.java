package it.polimi.db2.DB2_project.GmaWeb.controllers;

import it.polimi.db2.DB2_project.GmaEJB.Entities.Questionnaire;
import it.polimi.db2.DB2_project.GmaEJB.Entities.User;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "DeleteQuestionnaire", value = "/DeleteQuestionnaire")
public class DeleteQuestionnaire extends HttpServlet {
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

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginpath = getServletContext().getContextPath() + "/Login";
        String homepath = getServletContext().getContextPath() + "/GoToHomePage";

        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginpath);
            return;
        }

        User u = (User)request.getSession().getAttribute("user");

        if (!u.getIsAdmin()) {
            response.sendRedirect(homepath);
            return;
        }

        String[] deleteIds = request.getParameterValues("toDelete");

        int done = 0;
        for(String id: deleteIds) {
            done += questionnaireBean.deleteQuestionnaire(Integer.parseInt(id));
        }

        if(done == deleteIds.length) {
            session.setAttribute("msg", done + " questionnaire(s) deleted");
        }
        else {
            session.setAttribute("errorMsg", "Invalid questionnaire selected: deleted " + done + "/" + deleteIds.length + " questionnaire(s)");
        }

        response.sendRedirect(getServletContext().getContextPath() + "/GoToDelete");
    }
}
