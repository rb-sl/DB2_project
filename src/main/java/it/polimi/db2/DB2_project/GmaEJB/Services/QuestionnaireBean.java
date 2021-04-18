package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.Product;
import it.polimi.db2.DB2_project.GmaEJB.Entities.Questionnaire;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class QuestionnaireBean {
    @PersistenceContext
    private EntityManager em;

    // Function that returns the product of a given day
    public Questionnaire findQuestionnaireByDate(LocalDate date) {
        List<Questionnaire> qs = em.createNamedQuery("Questionnaire.findQuestionnaireByDate", Questionnaire.class)
                                .setParameter(1, date)
                                .getResultList();

        if (qs == null || qs.isEmpty()) {
            return null;
        }

        return qs.get(0);
    }
}