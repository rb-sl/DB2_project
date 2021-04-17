package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        access.setAccessDate(LocalDate.now());
        access.setAge(age);
        access.setSex(sex);
        access.setExpertise(ex);
        access.setUser(u);
        access.setQuestionnaire(questionnaire);
        if(answ != null) {
            answ.forEach((key, text) -> access.addAnswer(em.find(Question.class, key), text));
        }
        em.persist(access);
    }

    public Access retrieveAccess(){
        return em.find(Access.class, 1);
    }

    public Access findAccess(LocalDate date, User user) {
        List<Access> list = em.createNamedQuery("Access.findByUser", Access.class)
                .setParameter(1, user)
                .setParameter(2, date)
                .getResultList();

        if(list == null || list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }
}
