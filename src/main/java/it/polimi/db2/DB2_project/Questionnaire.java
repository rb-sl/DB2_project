package it.polimi.db2.DB2_project;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Table(name = "questionnaire")
@Entity
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id", nullable = false)
    private Integer quest_id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.REMOVE)
    private List<Access> accesses;

    @JoinTable(name = "Form",
            joinColumns = @JoinColumn(name = "quest_fk"),
            inverseJoinColumns = @JoinColumn(name = "question_fk"))
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Question> questions;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "product_fk", nullable = false)
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Access> getAccesses() {
        return accesses;
    }

    public void setAccesses(List<Access> accesses) {
        this.accesses = accesses;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getQuest_id() {
        return quest_id;
    }

    public void setQuest_id(Integer quest_id) {
        this.quest_id = quest_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questionnaire that = (Questionnaire) o;

        return quest_id != null && quest_id.equals(that.quest_id);
    }

    @Override
    public int hashCode() {
        return 880705831;
    }
}