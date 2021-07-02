package it.polimi.db2.DB2_project.GmaEJB.Entities;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@Table(name = "product")
@NamedQuery(name = "Product.findAll",
        query = "SELECT p FROM Product p")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_id", nullable = false)
    private Integer prod_id;

    @OneToMany(mappedBy = "product")
    private List<Questionnaire> questionnaires;

    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    public Integer getProd_id() {
        return prod_id;
    }

    public String getImage()  {
        return Base64.getMimeEncoder().encodeToString(image);
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;

        return prod_id != null && prod_id.equals(product.prod_id);
    }

    @Override
    public int hashCode() {
        return 2042274511;
    }
}
