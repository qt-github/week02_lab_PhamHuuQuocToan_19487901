package vn.edu.iuh.fit.backend.repositories;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.iuh.fit.backend.entities.ProductImage;


import java.util.Optional;
import java.util.List;


public class ProductImageRepository {
    private final EntityManager em;
    private final EntityTransaction trans;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ProductImageRepository() {
        em = Persistence.createEntityManagerFactory("lab_week_2").createEntityManager();
        trans=em.getTransaction();
    }
    public boolean insertProductImage(ProductImage productImage){
        try {
            trans.commit();
            em.persist(productImage);
            trans.commit();
            return true;
        }catch (Exception e){
            logger.info(e.getMessage());
            trans.rollback();
        }
        return false;
    }
    public boolean updateProductImage(ProductImage productImage){
        try {
            trans.commit();
            em.merge(productImage);
            trans.commit();
            return true;
        }catch (Exception e){
            logger.info(e.getMessage());
            trans.rollback();
        }
        return false;
    }

    public Optional<ProductImage> findProductImage(long id) {
        TypedQuery<ProductImage> query = em.createQuery("select o from ProductImage o where o.image_id=:id", ProductImage.class);
        query.setParameter("id", id);
        ProductImage productImage = query.getSingleResult();
        return productImage == null ? Optional.empty() : Optional.of(productImage);
    }

    public boolean deleteProductImage(long id) {
        Optional<ProductImage> op = findProductImage(id);
        ProductImage productImage = op.isPresent() ? op.get() : null;
        if (productImage == null) return false;
        try {
            trans.begin();
            em.remove(productImage);
            trans.commit();
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            trans.rollback();
        }
        return false;
    }

    public List<ProductImage> getAllProductImages() {
        try {
            trans.begin();
            List<ProductImage> list = em.createNativeQuery("SELECT * from product_image ", ProductImage.class).getResultList();
            trans.commit();
            return list;
        } catch (Exception e) {
            logger.info(e.getMessage());
            trans.rollback();
        }
        return null;
    }

}
