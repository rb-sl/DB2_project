package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Stateless
public class AccessBean {
    @PersistenceContext
    private EntityManager em;
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/QuestionnaireBean")
    private QuestionnaireBean questionnaireBean;
    @EJB(name = "it.polimi.db2.DB2_project.GmaEJB.Services/BadwordBean")
    private BadwordBean badwordbean;
    private Access access;

    public void createAccess(User u, Sex sex, Short age, Expertise ex, Map<Integer, String> answ){
        Questionnaire questionnaire = questionnaireBean.findQuestionnaireByDate(LocalDate.now());
        access = new Access();
        access.setAccessTime(LocalTime.now());
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

    public Boolean hasBadword(Map<Integer, String> answ) {
        List<Badwords> badwords = badwordbean.getBadwords();

        Boolean hasBadword = false;

        if (badwords != null && !badwords.isEmpty()) {
            for (Integer key : answ.keySet()) {
                for (Badwords bad : badwords) {
                    hasBadword = hasBadword || answ.get(key).contains(bad.getWord());
                }
            }
        }

        return hasBadword;
    }

    public Access findAccessByUser(LocalDate date, User user) {
        List<Access> accesses = em.createNamedQuery("Access.findByUser", Access.class)
                .setParameter(1, user)
                .setParameter(2, date)
                .getResultList();

        if(accesses == null || accesses.isEmpty()) {
            return null;
        }

        return accesses.get(0);
    }
}
