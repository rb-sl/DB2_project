package it.polimi.db2.DB2_project.GmaWeb.controllers;

import it.polimi.db2.DB2_project.GmaEJB.Entities.Product;
import it.polimi.db2.DB2_project.GmaEJB.Entities.Question;
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "GoToCreate", value = "/GoToCreate")
public class GoToCreate extends HttpServlet {
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/ProductBean")
    private ProductBean productBean;
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/QuestionBean")
    private QuestionBean questionBean;
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
        String loginpath = getServletContext().getContextPath() + "/index.html";
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
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        List<Product> products;
        products = productBean.findAllProducts();
        Map<Integer, String> miniProducts = products.stream()
                .collect(Collectors.toMap(Product::getProd_id, Product::getName));
        ctx.setVariable("miniProducts", miniProducts);

        List<Question> questions;
        questions = questionBean.findAllQuestions();
        Map<Integer, String> miniQuestions = questions.stream()
                .collect(Collectors.toMap(Question::getQuestion_id, Question::getText));
        ctx.setVariable("miniQuestions", miniQuestions);

        List<String> dates;
        dates = questionnaireBean.findAllDates();
        ctx.setVariable("dates", dates);

        String message = (String) session.getAttribute("errorMsg");
        if(message != null) {
            ctx.setVariable("errorMsg", message);
            session.removeAttribute("errorMsg");
        }

        String path = "/newQuestionnaire";
        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}