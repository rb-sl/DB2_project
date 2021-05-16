package it.polimi.db2.DB2_project.GmaEJB.Entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "questionnaire")
@NamedQuery(name = "Questionnaire.findQuestionnaireByDate",
        query = "SELECT q FROM Questionnaire q " +
                "WHERE q.date=?1")
@NamedQuery(name = "Questionnaire.findAll",
        query = "SELECT q FROM Questionnaire q ORDER BY q.date DESC ")
@NamedQuery(name = "Questionnaire.findById",
        query = "SELECT q FROM Questionnaire q WHERE q.quest_id=?1")
@Entity
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id", nullable = false)
    private Integer quest_id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "questionnaire") //No cascade because it's all managed by the database
    private List<Access> accesses;

    @JoinTable(name = "form",
            joinColumns = @JoinColumn(name = "quest_fk"),
            inverseJoinColumns = @JoinColumn(name = "question_fk"))
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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

    public List<Access> getCanceled() {
        return getAccesses().stream().filter(a -> a.getSex() == null && a.getAge() == null && a.getExpertise() == null)
                .collect(Collectors.toList());
    }
    public List<Access> getSubmitted() {
        return getAccesses().stream().filter(a -> a.getSex() != null && a.getAge() != null && a.getExpertise() != null)
                .sorted(Comparator.comparing(Access::getAccessTime).reversed())
                .collect(Collectors.toList());
    }
}