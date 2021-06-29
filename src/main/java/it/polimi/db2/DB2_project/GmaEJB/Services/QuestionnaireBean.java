package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.Product;
import it.polimi.db2.DB2_project.GmaEJB.Entities.Question;
import it.polimi.db2.DB2_project.GmaEJB.Entities.Questionnaire;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class QuestionnaireBean {
    @PersistenceContext
    private EntityManager em;

    // Function that returns the product of a given day
    public Questionnaire findQuestionnaireByDate(LocalDate date) {
        List<Questionnaire> qs = em.createNamedQuery("Questionnaire.findQuestionnaireByDate", Questionnaire.class)
                                .setParameter(1, date)
                                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                                .getResultList();

        if (qs == null || qs.isEmpty()) {
            return null;
        }

        return qs.get(0);
    }

    public List<Questionnaire> findAll() {
        List<Questionnaire> qs = em.createNamedQuery("Questionnaire.findAll", Questionnaire.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList();
        if (qs == null || qs.isEmpty()) {
            return null;
        }
        return qs;
    }


    public List<String> findAllDates() {
        List<String> dates = em.createNamedQuery("Questionnaire.findAllDates", LocalDate.class)
                .getResultList().stream().map(LocalDate::toString).collect(Collectors.toList());
        if (dates.isEmpty()) {
            return new ArrayList<String>();
        }
        return dates;
    }

    public Questionnaire findById(Integer id){
        return em.find(Questionnaire.class, id);
    }

    public int deleteQuestionnaire(Integer id) {
        Questionnaire toDelete = findById(id);
        if (toDelete != null) {
            em.remove(toDelete);
            return 1;
        }
        return 0;
    }

    public Questionnaire createQuestionnaire(LocalDate date, Product product, Map<Question, Integer> questionMap){
        Questionnaire newQuestionnaire = new Questionnaire();
        newQuestionnaire.setDate(date);
        newQuestionnaire.setQuestions(questionMap);
        newQuestionnaire.setProduct(em.find(Product.class, product.getProd_id()));
        em.persist(newQuestionnaire);
        return newQuestionnaire;
    }
}
