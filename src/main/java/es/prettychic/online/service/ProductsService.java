package es.prettychic.online.service;

import es.prettychic.online.domain.Products;
import es.prettychic.online.repository.ProductsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Products.
 */
@Service
@Transactional
public class ProductsService {

    private final Logger log = LoggerFactory.getLogger(ProductsService.class);
    
    @Inject
    private ProductsRepository productsRepository;

    /**
     * Save a products.
     *
     * @param products the entity to save
     * @return the persisted entity
     */
    public Products save(Products products) {
        log.debug("Request to save Products : {}", products);
        Products result = productsRepository.save(products);
        return result;
    }

    /**
     *  Get all the products.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Products> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        Page<Products> result = productsRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one products by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Products findOne(Long id) {
        log.debug("Request to get Products : {}", id);
        Products products = productsRepository.findOne(id);
        return products;
    }

    /**
     *  Delete the  products by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Products : {}", id);
        productsRepository.delete(id);
    }
}
