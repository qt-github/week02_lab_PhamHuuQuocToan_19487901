package vn.edu.iuh.fit.backend.repositories;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.iuh.fit.backend.dto.ProductInfoDTO;
import vn.edu.iuh.fit.backend.entities.Product;
import vn.edu.iuh.fit.backend.enums.ProductStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductRepository {
    private final EntityManager em = Persistence.createEntityManagerFactory("lab_week_2").createEntityManager();
    private final EntityTransaction trans = em.getTransaction();
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public boolean insertProduct(Product product) {
        return performTransaction(() -> em.persist(product));
    }

    public boolean updateProduct(Product product) {
        return performTransaction(() -> em.merge(product));
    }

    public Optional<Product> findProduct(long id) {
        return Optional.ofNullable(em.find(Product.class, id));
    }

    public boolean deleteProduct(long id) {
        return performTransaction(() -> {
            Product product = findProduct(id).orElse(null);
            if (product != null) {
                product.setStatus(ProductStatus.DELETE);
                em.merge(product);
            }
        });
    }

    public List<Product> getAllProducts() {
        return em.createQuery("Select p from Product p order by p.product_id asc", Product.class).getResultList();
    }

    public List<Product> getActiveProduct() {
        return em.createQuery("Select p from Product p where p.status=:status", Product.class)
                .setParameter("status", ProductStatus.ACTIVE)
                .getResultList();
    }

    public List<ProductInfoDTO> getActiveProductInfo() {
        List<Product> list = em.createQuery("SELECT p FROM Product p JOIN FETCH p.productImageList ig JOIN FETCH p.productPrices pp WHERE p.status = :status order by pp.price_date_time desc ", Product.class)
                .setParameter("status", ProductStatus.ACTIVE)
                .getResultList();

        return list.stream().map(p -> {
            ProductInfoDTO productInfoDTO = new ProductInfoDTO();
            productInfoDTO.setName(p.getName());
            productInfoDTO.setProductId(p.getProduct_id());
            productInfoDTO.setDescription(p.getDescription());
            productInfoDTO.setUnit(p.getUnit());
            productInfoDTO.setManufacturer(p.getManufacturer());
            productInfoDTO.setPath(p.getProductImageList().get(0).getPath());
            productInfoDTO.setPrice(p.getProductPrices().get(0).getPrice());
            return productInfoDTO;
        }).collect(Collectors.toList());
    }

    private boolean performTransaction(Runnable action) {
        try {
            trans.begin();
            action.run();
            trans.commit();
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            trans.rollback();
        }
        return false;
    }
}