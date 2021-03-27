package it.polimi.db2.DB2_project;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Table(name = "access")
@Entity
public class Access {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quest_fk", nullable = false)
    private Questionnaire questionnaire;

    @ElementCollection
    @CollectionTable (name = "answer",
            joinColumns = @JoinColumn (name = "access_fk"))
            @MapKeyJoinColumn (name = "question_fk")
            @Column(name = "text")
    private Map<Question, String> answers;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_id", nullable = false)
    private Integer id;

    @Column(name = "accessTime")
    private LocalDateTime accessTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Column(name = "age")
    private Short age;

    @Enumerated(EnumType.STRING)
    @Column(name = "expertise")
    private Expertise expertise;

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
    }

    public Expertise getExpertise() {
        return expertise;
    }

    public void setExpertise(Expertise expertise) {
        this.expertise = expertise;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Access access = (Access) o;

        return id != null && id.equals(access.id);
    }

    @Override
    public int hashCode() {
        return 271017061;
    }
}