package es.prettychic.online.service;

import es.prettychic.online.domain.Stock;
import es.prettychic.online.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Stock.
 */
@Service
@Transactional
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);
    
    @Inject
    private StockRepository stockRepository;

    /**
     * Save a stock.
     *
     * @param stock the entity to save
     * @return the persisted entity
     */
    public Stock save(Stock stock) {
        log.debug("Request to save Stock : {}", stock);
        Stock result = stockRepository.save(stock);
        return result;
    }

    /**
     *  Get all the stocks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Stock> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        Page<Stock> result = stockRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one stock by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Stock findOne(Long id) {
        log.debug("Request to get Stock : {}", id);
        Stock stock = stockRepository.findOne(id);
        return stock;
    }

    /**
     *  Delete the  stock by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Stock : {}", id);
        stockRepository.delete(id);
    }
}
