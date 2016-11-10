package es.prettychic.online.repository;

import es.prettychic.online.domain.Products;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Products entity.
 */
@SuppressWarnings("unused")
public interface ProductsRepository extends JpaRepository<Products,Long> {

}
