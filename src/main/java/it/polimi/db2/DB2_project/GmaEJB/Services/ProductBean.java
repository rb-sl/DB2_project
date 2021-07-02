package it.polimi.db2.DB2_project.GmaEJB.Services;

import it.polimi.db2.DB2_project.GmaEJB.Entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class ProductBean {
    @PersistenceContext
    private EntityManager em;

    public Product createProduct(String productName, Part imageFile) throws IOException {
        byte[] imageBytes;

        // Transform image to array of byte
        InputStream imageInputStream = imageFile.getInputStream();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = imageInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        imageBytes = outputStream.toByteArray();

        Product product = new Product();

        product.setImage(imageBytes);
        product.setName(productName);

        em.persist(product);

        return product;
    }

    public Product findProductById(Integer id) {
        return em.find(Product.class, id);
    }

    public List<Product> findAllProducts() {
        return em.createNamedQuery("Product.findAll", Product.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .getResultList();
    }
}
