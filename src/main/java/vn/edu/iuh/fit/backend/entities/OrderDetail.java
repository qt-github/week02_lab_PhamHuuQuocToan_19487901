package vn.edu.iuh.fit.backend.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import vn.edu.iuh.fit.backend.pks.OrderDetailPK;


@Getter
@Entity
@Table(name = "order_detail")
@IdClass(OrderDetailPK.class)
public class OrderDetail {
    @Column(name = "quantity", nullable = false)
    private double quantity;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "note", length = 255, nullable = true)
    private String note;

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
    @Id
    @JoinColumn(name = "product_id")
    @JsonIgnore
    @ManyToOne
    private Product product;
    @Basic
    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId; // Thuộc tính productId
    public OrderDetail() {
    }
    @Basic
    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;
    public OrderDetail(double quantity, double price, String note, Order order, Product product) {
        this.quantity = quantity;
        this.price = price;
        this.note = note;
        this.order = order;
        this.product = product;
    }

    public OrderDetail(double quantity, double price, String note, Order order, Product product, Long productId, Long orderId) {
        this.quantity = quantity;
        this.price = price;
        this.note = note;
        this.order = order;
        this.product = product;
        this.productId = productId;
        this.orderId = orderId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                ", quantity=" + quantity +
                ", price=" + price +
                ", note='" + note + '\'' +
                ", order=" + order +
                ", product=" + product +
                '}';
    }
}

