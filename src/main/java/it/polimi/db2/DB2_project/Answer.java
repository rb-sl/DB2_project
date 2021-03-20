package it.polimi.db2.DB2_project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "answer")
@Entity
public class Answer {
    @Id
    @Column(name = "answer_id", nullable = false)
    private Integer answer_id;

    @Column(name = "text", nullable = false, length = 65536)
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(Integer answer_id) {
        this.answer_id = answer_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;

        return answer_id != null && answer_id.equals(answer.answer_id);
    }

    @Override
    public int hashCode() {
        return 1142810673;
    }
}