package vn.edu.iuh.fit.backend.pks;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class OrderDetailPK implements Serializable {
    private Long order;
    private Long product;
}