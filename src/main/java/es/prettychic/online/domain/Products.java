package es.prettychic.online.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Products.
 */
@Entity
@Table(name = "products")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "main_image")
    private byte[] mainImage;

    @Column(name = "main_image_content_type")
    private String mainImageContentType;

    @Lob
    @Column(name = "image_2")
    private byte[] image2;

    @Column(name = "image_2_content_type")
    private String image2ContentType;

    @Lob
    @Column(name = "image_3")
    private byte[] image3;

    @Column(name = "image_3_content_type")
    private String image3ContentType;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Stock> stocks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Products name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Products description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getMainImage() {
        return mainImage;
    }

    public Products mainImage(byte[] mainImage) {
        this.mainImage = mainImage;
        return this;
    }

    public void setMainImage(byte[] mainImage) {
        this.mainImage = mainImage;
    }

    public String getMainImageContentType() {
        return mainImageContentType;
    }

    public Products mainImageContentType(String mainImageContentType) {
        this.mainImageContentType = mainImageContentType;
        return this;
    }

    public void setMainImageContentType(String mainImageContentType) {
        this.mainImageContentType = mainImageContentType;
    }

    public byte[] getImage2() {
        return image2;
    }

    public Products image2(byte[] image2) {
        this.image2 = image2;
        return this;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public String getImage2ContentType() {
        return image2ContentType;
    }

    public Products image2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
        return this;
    }

    public void setImage2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
    }

    public byte[] getImage3() {
        return image3;
    }

    public Products image3(byte[] image3) {
        this.image3 = image3;
        return this;
    }

    public void setImage3(byte[] image3) {
        this.image3 = image3;
    }

    public String getImage3ContentType() {
        return image3ContentType;
    }

    public Products image3ContentType(String image3ContentType) {
        this.image3ContentType = image3ContentType;
        return this;
    }

    public void setImage3ContentType(String image3ContentType) {
        this.image3ContentType = image3ContentType;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    public Products stocks(Set<Stock> stocks) {
        this.stocks = stocks;
        return this;
    }

    public Products addStocks(Stock stock) {
        stocks.add(stock);
        stock.setProduct(this);
        return this;
    }

    public Products removeStocks(Stock stock) {
        stocks.remove(stock);
        stock.setProduct(null);
        return this;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Products products = (Products) o;
        if(products.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, products.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Products{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", mainImage='" + mainImage + "'" +
            ", mainImageContentType='" + mainImageContentType + "'" +
            ", image2='" + image2 + "'" +
            ", image2ContentType='" + image2ContentType + "'" +
            ", image3='" + image3 + "'" +
            ", image3ContentType='" + image3ContentType + "'" +
            '}';
    }
}
