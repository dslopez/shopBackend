package es.prettychic.online.web.rest;

import es.prettychic.online.ShopApp;

import es.prettychic.online.domain.Stock;
import es.prettychic.online.domain.Products;
import es.prettychic.online.repository.StockRepository;
import es.prettychic.online.service.StockService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.prettychic.online.domain.enumeration.Sizes;
/**
 * Test class for the StockResource REST controller.
 *
 * @see StockResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApp.class)
public class StockResourceIntTest {

    private static final Sizes DEFAULT_SIZE = Sizes.XS;
    private static final Sizes UPDATED_SIZE = Sizes.S;

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    @Inject
    private StockRepository stockRepository;

    @Inject
    private StockService stockService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restStockMockMvc;

    private Stock stock;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockResource stockResource = new StockResource();
        ReflectionTestUtils.setField(stockResource, "stockService", stockService);
        this.restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock()
                .size(DEFAULT_SIZE)
                .stock(DEFAULT_STOCK);
        // Add required entity
        Products product = ProductsResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        stock.setProduct(product);
        return stock;
    }

    @Before
    public void initTest() {
        stock = createEntity(em);
    }

    @Test
    @Transactional
    public void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock

        restStockMockMvc.perform(post("/api/stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stock)))
                .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stocks.get(stocks.size() - 1);
        assertThat(testStock.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testStock.getStock()).isEqualTo(DEFAULT_STOCK);
    }

    @Test
    @Transactional
    public void checkSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setSize(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stock)))
                .andExpect(status().isBadRequest());

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setStock(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stock)))
                .andExpect(status().isBadRequest());

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stocks
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
                .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())))
                .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)));
    }

    @Test
    @Transactional
    public void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.toString()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK));
    }

    @Test
    @Transactional
    public void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = stockRepository.findOne(stock.getId());
        updatedStock
                .size(UPDATED_SIZE)
                .stock(UPDATED_STOCK);

        restStockMockMvc.perform(put("/api/stocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStock)))
                .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stocks.get(stocks.size() - 1);
        assertThat(testStock.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testStock.getStock()).isEqualTo(UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void deleteStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Get the stock
        restStockMockMvc.perform(delete("/api/stocks/{id}", stock.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
