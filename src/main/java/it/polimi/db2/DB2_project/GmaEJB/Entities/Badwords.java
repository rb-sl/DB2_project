package it.polimi.db2.DB2_project.GmaEJB.Entities;

import javax.persistence.*;

@Table(name = "badwords")
@Entity
public class Badwords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id", nullable = false)
    private Integer word_id;

    @Column(name = "word", nullable = false, unique = true, length = 50)
    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getWord_id() {
        return word_id;
    }

    public void setWord_id(Integer word_id) {
        this.word_id = word_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Badwords badwords = (Badwords) o;

        return word_id != null && word_id.equals(badwords.word_id);
    }

    @Override
    public int hashCode() {
        return 1406608183;
    }
}