package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class ProductBean {
    @PersistenceContext
    private EntityManager em;

    // Function that returns the product of a given day
    public Product findProductByDate(LocalDate date) {
        List<Product> products = em.createNamedQuery("Product.findProductByDate", Product.class)
                                .setParameter(1, date)
                                .getResultList();
        if (products == null || products.isEmpty()) {
            return null;
        }
        return products.get(0);
    }
}
