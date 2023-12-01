package vn.edu.iuh.fit.backend.repositories;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.iuh.fit.backend.entities.ProductImage;

import java.util.List;
import java.util.Optional;

public class ProductImageRepository {
    private final EntityManager em;
    private final EntityTransaction trans;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ProductImageRepository() {
        em = Persistence.createEntityManagerFactory("lab_week_2").createEntityManager();
        trans = em.getTransaction();
    }

    public boolean insertProductImage(ProductImage productImage) {
        return executeTransaction(() -> em.persist(productImage));
    }

    public boolean updateProductImage(ProductImage productImage) {
        return executeTransaction(() -> em.merge(productImage));
    }

    public Optional<ProductImage> findProductImage(long id) {
        return executeTransactionWithResult(() -> {
            TypedQuery<ProductImage> query = em.createQuery("select o from ProductImage o where o.image_id=:id", ProductImage.class);
            query.setParameter("id", id);
            ProductImage productImage = query.getSingleResult();
            return productImage == null ? Optional.empty() : Optional.of(productImage);
        });
    }

    public boolean deleteProductImage(long id) {
        return executeTransaction(() -> {
            Optional<ProductImage> op = findProductImage(id);
            ProductImage productImage = op.orElse(null);
            if (productImage != null) {
                em.remove(productImage);
            }
        });
    }

    public List<ProductImage> getAllProductImages() {
        return executeTransactionWithResult(() -> em.createNativeQuery("SELECT * from product_image ", ProductImage.class).getResultList());
    }

    private boolean executeTransaction(Runnable action) {
        try {
            trans.begin();
            action.run();
            trans.commit();
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            trans.rollback();
            return false;
        }
    }

    private <T> T executeTransactionWithResult(ResultSupplier<T> action) {
        try {
            trans.begin();
            T result = action.get();
            trans.commit();
            return result;
        } catch (Exception e) {
            logger.info(e.getMessage());
            trans.rollback();
            return null;
        }
    }
}