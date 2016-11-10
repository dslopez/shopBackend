package es.prettychic.online.repository;

import es.prettychic.online.domain.Stock;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Stock entity.
 */
@SuppressWarnings("unused")
public interface StockRepository extends JpaRepository<Stock,Long> {

}
