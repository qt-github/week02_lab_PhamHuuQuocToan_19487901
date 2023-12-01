package vn.edu.iuh.fit.backend.repositories;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.iuh.fit.backend.entities.ProductPrice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProductPriceRepository {
    private final EntityManager em;
    private final EntityTransaction trans;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ProductPriceRepository() {
        em = Persistence.createEntityManagerFactory("lab_week_2").createEntityManager();
        trans = em.getTransaction();
    }

    public boolean insertProductPrice(ProductPrice productPrice) {
        return executeTransaction(() -> em.persist(productPrice));
    }

    public boolean updateProductPrice(ProductPrice productPrice) {
        return executeTransaction(() -> em.merge(productPrice));
    }



    public Optional<List<ProductPrice>> findProductPricesByProductId(Long productId) {
    return executeTransactionWithResult(() -> {
        TypedQuery<ProductPrice> query = em.createQuery("select o from ProductPrice o where o.product.product_id=:id", ProductPrice.class);
        query.setParameter("id", productId);
        List<ProductPrice> productPrices = query.getResultList();
        return Optional.ofNullable(productPrices);
    });
}

    public Optional<ProductPrice> findProductPrice(long id, LocalDateTime dateTime) {
        return executeTransactionWithResult(() -> {
            TypedQuery<ProductPrice> query = em.createQuery("select o from ProductPrice o where o.product.product_id=:id and o.price_date_time=:date", ProductPrice.class);
            query.setParameter("id", id);
            query.setParameter("date", dateTime);
            ProductPrice productPrice = query.getSingleResult();
            return productPrice == null ? Optional.empty() : Optional.of(productPrice);
        });
    }

    public boolean deleteProductPrice(long id, LocalDateTime dateTime) {
        return executeTransaction(() -> {
            Optional<ProductPrice> op = findProductPrice(id, dateTime);
            ProductPrice productPrice = op.orElse(null);
            if (productPrice != null) {
                em.remove(productPrice);
            }
        });
    }

    public List<ProductPrice> getAllProductPrice() {
        return executeTransactionWithResult(() -> em.createQuery("SELECT c from ProductPrice c order by c.product.product_id ASC", ProductPrice.class).getResultList());
    }

    /**
     * Truy xuất giá mới nhất của một sản phẩm từ cơ sở dữ liệu bằng ID của sản phẩm.
     *
     * @param productId ID của sản phẩm.
     * @return Giá mới nhất của sản phẩm, hoặc null nếu không tìm thấy giá nào.
     */
    public Double findLatestProductPrice(long productId) {
        return executeTransactionWithResult(() -> {
            String jpql = "SELECT pp.price " +
                    "FROM ProductPrice pp " +
                    "WHERE pp.product.product_id = :productId " +
                    "ORDER BY pp.price_date_time DESC";

            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("productId", productId);
            query.setMaxResults(1);  // Limit the result to return only one price

            Double latestPrice = null;

            try {
                latestPrice = query.getSingleResult();
            } catch (NoResultException e) {
                // Handle when no result is found if necessary
            }

            return latestPrice;
        });
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