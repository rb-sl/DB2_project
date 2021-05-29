package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class QuestionBean {
    @PersistenceContext
    private EntityManager em;

    public Question createQuestion(String text){
        Question newQuestion = new Question();
        newQuestion.setText(text);
        em.persist(newQuestion);
        return newQuestion;
    }

    public void addQuestionnaire(Questionnaire questionnaire, String questId, Integer i){
        Question question = em.find(Question.class, Integer.parseInt(questId));
    }

    public List<Question> findAllQuestions() {
        return em.createNamedQuery("Question.findAll", Question.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList();
    }

    public Question findQuestionByText(String text) {
        List<Question> questions =  em.createNamedQuery("Question.findByText", Question.class)
                .setParameter(1, text)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList();
        if(questions != null) {
            return questions.get(0);
        }
        return null;
    }
}
