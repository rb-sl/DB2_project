package it.polimi.db2.DB2_project.GmaWeb.controllers;

import it.polimi.db2.DB2_project.GmaEJB.Entities.Product;
import it.polimi.db2.DB2_project.GmaEJB.Entities.Question;
import it.polimi.db2.DB2_project.GmaEJB.Entities.Questionnaire;
import it.polimi.db2.DB2_project.GmaEJB.Entities.User;
import it.polimi.db2.DB2_project.GmaEJB.Services.ProductBean;
import it.polimi.db2.DB2_project.GmaEJB.Services.QuestionBean;
import it.polimi.db2.DB2_project.GmaEJB.Services.QuestionnaireBean;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CreateQuestionnaire", value = "/CreateQuestionnaire")
@MultipartConfig
public class CreateQuestionnaire extends HttpServlet {
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/QuestionnaireBean")
    private QuestionnaireBean questionnaireBean;
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/ProductBean")
    private ProductBean productBean;
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/QuestionBean")
    private QuestionBean questionBean;

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

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginpath = getServletContext().getContextPath() + "/index.html";
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

        // Check for date availability
        LocalDate date = LocalDate.parse(request.getParameter("date"), DateTimeFormatter.ISO_LOCAL_DATE);
        Questionnaire questionnaire = questionnaireBean.findQuestionnaireByDate(date);
        if(questionnaire != null) {
            session.setAttribute("errorMsg", "A questionnaire for date " + date.toString() + " is already present");
            response.sendRedirect(getServletContext().getContextPath() + "/GoToCreate");
            return;
        }

        // Product creation or retrieval
        String productName = request.getParameter("product");

        Product product;
        Integer productId = Integer.parseInt(request.getParameter("radioProduct"));
        if(productId == -1) {
            Part imageFile = request.getPart("image");
            product = productBean.createProduct(productName, imageFile);
        }
        else {
            product = productBean.findProductById(productId);
        }

        // Question creation or retrieval
        String[] newQuestions = request.getParameterValues("questions");
        Map<Question, Integer> quests = new HashMap<>();

        for (int i = 0; i < newQuestions.length; i++) {
            Question question;
            question = questionBean.findQuestionByText(newQuestions[i]);
            if (question == null) {
                question = questionBean.createQuestion(newQuestions[i]);
            }

            quests.put(question, i + 1);
        }

        // Questionnaire creation
        Questionnaire newQuestionnaire = questionnaireBean.createQuestionnaire(date, product, quests);

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        session.setAttribute("msg", "Questionnaire created correctly");

        response.sendRedirect(getServletContext().getContextPath() + "/GoToCreate");
    }
}
