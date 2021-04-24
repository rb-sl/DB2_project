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
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CompileQuestionnaire", value = "/CompileQuestionnaire")
public class CompileQuestionnaire extends HttpServlet {
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/QuestionnaireBean")
    private QuestionnaireBean questionnaireBean;
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/AccessBean")
    private AccessBean accessBean;

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
        Short age = 0;

        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginpath);
            return;
        }
        User u = (User)request.getSession().getAttribute("user");

        Access access = accessBean.findAccess(LocalDate.now(), u);
        if(access != null) {
            response.sendRedirect(homepath);
            return;
        }

        String resultAge = request.getParameter("age");
        if (resultAge != null && !resultAge.isEmpty()) {
            age = Short.parseShort(resultAge);
            if(age <= 0 || age > 99) {
                // todo if resilience: answer with user's input
                String path = getServletContext().getContextPath() + "/GoToQuestionnaire";
                response.sendRedirect(path);
                return;
            }
        }

        Sex sex = Sex.valueOf(request.getParameter("sex"));
        Expertise ex = Expertise.valueOf(request.getParameter("expertise"));



        Questionnaire questionnaire = questionnaireBean.findQuestionnaireByDate(LocalDate.now());
        List<Question> questionIds = questionnaire.getQuestions();

        for (Question q : questionIds) {
            answ.put(q.getQuestion_id(), request.getParameter("res" + q.getQuestion_id()));
            //System.out.println(answ.get(q.getQuestion_id()));
        }

        if(accessBean.hasBadword(answ)) {
            String path = getServletContext().getContextPath() + "/GoToQuestionnaire";
            response.sendRedirect(path);
            return;
        }
        accessBean.createAccess(u, sex, age, ex, answ);

        String path = "/Submitted";
        request.setAttribute("isSaved", 1);
        getServletContext().getRequestDispatcher(path).forward(request, response);
    }
}
