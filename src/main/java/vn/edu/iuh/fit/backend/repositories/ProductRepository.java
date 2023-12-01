package vn.edu.iuh.fit.backend.repositories;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.iuh.fit.backend.dto.ProductInfoDTO;
import vn.edu.iuh.fit.backend.entities.*;
import vn.edu.iuh.fit.backend.enums.ProductStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductRepository {
    private final EntityManager em;
    private final EntityTransaction trans;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ProductRepository() {
        em = Persistence.createEntityManagerFactory("lab_week_2").createEntityManager();
        trans = em.getTransaction();
    }

    public Product getProductById(long id) {
        return em.find(Product.class, id);
    }

    public boolean insertProduct(Product product, ProductImage productImage, ProductPrice productPrice) {
        return executeTransaction(() -> {
            em.persist(product);
            em.persist(productImage);
            em.persist(productPrice);
        });
    }

    public boolean updateProduct(Product product, ProductImage productImage) {
        return executeTransaction(() -> {
                    em.merge(product);
                    em.merge(productImage);
                }
        );
    }

    public Optional<Product> findProduct(long id) {
        return executeTransactionWithResult(() -> Optional.ofNullable(em.find(Product.class, id)));
    }

    public boolean deleteProduct(long id) {
        return executeTransaction(() -> {
            findProduct(id).ifPresent(product -> {
                product.setStatus(ProductStatus.DELETE);
                em.merge(product);
            });
        });
    }

    public List<Product> getAllProducts() {
        return executeTransactionWithResult(() -> em.createQuery("Select p from Product p order by p.product_id asc", Product.class).getResultList());
    }


    /**
     * Truy xuất thông tin về tất cả các sản phẩm đang hoạt động từ cơ sở dữ liệu.
     *
     * @return Danh sách các đối tượng ProductInfoDTO chứa thông tin về mỗi sản phẩm đang hoạt động.
     */
    public List<ProductInfoDTO> getActiveProductInfo() {
        return executeTransactionWithResult(() -> {
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
        });
    }

    private boolean executeTransaction(Runnable action) {
    try {
        trans.begin();
        action.run();
        trans.commit();
        return true;
    } catch (Exception ex) {
        logger.error(ex.getMessage());
        throw new RuntimeException("Transaction failed", ex);
    } finally {
        if (trans.isActive()) {
            trans.rollback();
        }
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