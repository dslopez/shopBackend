package es.prettychic.online.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import es.prettychic.online.domain.enumeration.Sizes;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "size", nullable = false)
    private Sizes size;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne
    @NotNull
    private Products product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sizes getSize() {
        return size;
    }

    public Stock size(Sizes size) {
        this.size = size;
        return this;
    }

    public void setSize(Sizes size) {
        this.size = size;
    }

    public Integer getStock() {
        return stock;
    }

    public Stock stock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Products getProduct() {
        return product;
    }

    public Stock product(Products products) {
        this.product = products;
        return this;
    }

    public void setProduct(Products products) {
        this.product = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stock stock = (Stock) o;
        if(stock.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stock.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + id +
            ", size='" + size + "'" +
            ", stock='" + stock + "'" +
            '}';
    }
}
