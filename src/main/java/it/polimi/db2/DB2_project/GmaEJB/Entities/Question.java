package it.polimi.db2.DB2_project.GmaEJB.Entities;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Table(name = "question")
@NamedQuery(name = "Question.findAll",
        query = "SELECT q FROM Question q")
@NamedQuery(name = "Question.findByText",
        query = "SELECT q FROM Question q WHERE q.text = ?1")
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Integer question_id;

    @Column(name = "text", nullable = false, length = 65536)
    private String text;

    @ElementCollection
    @CollectionTable (name = "answer",
            joinColumns = @JoinColumn (name = "question_fk"))
    @MapKeyJoinColumn (name = "access_fk")
    @Column(name = "text")
    private Map<Access, String> answers = new HashMap<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public Map<Access, String> getAnswers() {
        return answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;

        return question_id != null && question_id.equals(question.question_id);
    }

    @Override
    public int hashCode() {
        return 1344421622;
    }
}