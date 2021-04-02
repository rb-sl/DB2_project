package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Stateless
public class AccessBean {
    @PersistenceContext
    private EntityManager em;
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/QuestionnaireBean")
    private QuestionnaireBean questionnaireBean;
    private Access access;
    public void createAccess(User u, Sex sex, Short age, Expertise ex, Map<Integer, String> answ){
        Questionnaire questionnaire = questionnaireBean.findQuestionnaireByDate(LocalDate.now());
        access = new Access();
        access.setAccessTime(LocalDateTime.now());
        access.setAge(age);
        access.setSex(sex);
        access.setExpertise(ex);
        access.setUser(u);
        access.setQuestionnaire(questionnaire);
        answ.forEach((key, text) ->access.addAnswer(em.find(Question.class, key), text));
        em.persist(access);
    }
    public Access retrieveAccess(){
        return em.find(Access.class, 1);
    }
}
