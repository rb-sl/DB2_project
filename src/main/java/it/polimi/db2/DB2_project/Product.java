package it.polimi.db2.DB2_project;

import javax.persistence.*;
import java.sql.Blob;
import java.util.List;

@Table(name = "product")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_id", nullable = false)
    private Integer prod_id;

    @OneToMany(mappedBy = "product")
    private List<Questionnaire> questionnaires;

    public Integer getProd_id() {
        return prod_id;
    }

    public void setProd_id(Integer prod_id) {
        this.prod_id = prod_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;

        return prod_id != null && prod_id.equals(product.prod_id);
    }

    @Lob
    @Column(name = "image", nullable = false)
    private Blob image;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    public List<Questionnaire> getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(List<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return 2042274511;
    }
}
