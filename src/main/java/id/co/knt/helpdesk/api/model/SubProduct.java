package id.co.knt.helpdesk.api.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author marlina_kreative
 */
@Entity
@Table(name = "helpdesk_sub_product")
public class SubProduct implements Serializable {

    private static final long serialVersionUID = -1169013840501699745L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "label")
    private String label;

    @Column(name = "value")
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
