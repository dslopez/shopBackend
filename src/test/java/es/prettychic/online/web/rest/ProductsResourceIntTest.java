package es.prettychic.online.web.rest;

import es.prettychic.online.ShopApp;

import es.prettychic.online.domain.Products;
import es.prettychic.online.repository.ProductsRepository;
import es.prettychic.online.service.ProductsService;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductsResource REST controller.
 *
 * @see ProductsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApp.class)
public class ProductsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_MAIN_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_MAIN_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_MAIN_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_MAIN_IMAGE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_2 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_2 = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_2_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_2_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_3 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_3 = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_3_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_3_CONTENT_TYPE = "image/png";

    @Inject
    private ProductsRepository productsRepository;

    @Inject
    private ProductsService productsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProductsMockMvc;

    private Products products;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductsResource productsResource = new ProductsResource();
        ReflectionTestUtils.setField(productsResource, "productsService", productsService);
        this.restProductsMockMvc = MockMvcBuilders.standaloneSetup(productsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Products createEntity(EntityManager em) {
        Products products = new Products()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .mainImage(DEFAULT_MAIN_IMAGE)
                .mainImageContentType(DEFAULT_MAIN_IMAGE_CONTENT_TYPE)
                .image2(DEFAULT_IMAGE_2)
                .image2ContentType(DEFAULT_IMAGE_2_CONTENT_TYPE)
                .image3(DEFAULT_IMAGE_3)
                .image3ContentType(DEFAULT_IMAGE_3_CONTENT_TYPE);
        return products;
    }

    @Before
    public void initTest() {
        products = createEntity(em);
    }

    @Test
    @Transactional
    public void createProducts() throws Exception {
        int databaseSizeBeforeCreate = productsRepository.findAll().size();

        // Create the Products

        restProductsMockMvc.perform(post("/api/products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(products)))
                .andExpect(status().isCreated());

        // Validate the Products in the database
        List<Products> products = productsRepository.findAll();
        assertThat(products).hasSize(databaseSizeBeforeCreate + 1);
        Products testProducts = products.get(products.size() - 1);
        assertThat(testProducts.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProducts.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProducts.getMainImage()).isEqualTo(DEFAULT_MAIN_IMAGE);
        assertThat(testProducts.getMainImageContentType()).isEqualTo(DEFAULT_MAIN_IMAGE_CONTENT_TYPE);
        assertThat(testProducts.getImage2()).isEqualTo(DEFAULT_IMAGE_2);
        assertThat(testProducts.getImage2ContentType()).isEqualTo(DEFAULT_IMAGE_2_CONTENT_TYPE);
        assertThat(testProducts.getImage3()).isEqualTo(DEFAULT_IMAGE_3);
        assertThat(testProducts.getImage3ContentType()).isEqualTo(DEFAULT_IMAGE_3_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productsRepository.findAll().size();
        // set the field null
        products.setName(null);

        // Create the Products, which fails.

        restProductsMockMvc.perform(post("/api/products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(products)))
                .andExpect(status().isBadRequest());

        List<Products> products = productsRepository.findAll();
        assertThat(products).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get all the products
        restProductsMockMvc.perform(get("/api/products?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(products.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].mainImageContentType").value(hasItem(DEFAULT_MAIN_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].mainImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_MAIN_IMAGE))))
                .andExpect(jsonPath("$.[*].image2ContentType").value(hasItem(DEFAULT_IMAGE_2_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image2").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_2))))
                .andExpect(jsonPath("$.[*].image3ContentType").value(hasItem(DEFAULT_IMAGE_3_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image3").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_3))));
    }

    @Test
    @Transactional
    public void getProducts() throws Exception {
        // Initialize the database
        productsRepository.saveAndFlush(products);

        // Get the products
        restProductsMockMvc.perform(get("/api/products/{id}", products.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(products.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.mainImageContentType").value(DEFAULT_MAIN_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.mainImage").value(Base64Utils.encodeToString(DEFAULT_MAIN_IMAGE)))
            .andExpect(jsonPath("$.image2ContentType").value(DEFAULT_IMAGE_2_CONTENT_TYPE))
            .andExpect(jsonPath("$.image2").value(Base64Utils.encodeToString(DEFAULT_IMAGE_2)))
            .andExpect(jsonPath("$.image3ContentType").value(DEFAULT_IMAGE_3_CONTENT_TYPE))
            .andExpect(jsonPath("$.image3").value(Base64Utils.encodeToString(DEFAULT_IMAGE_3)));
    }

    @Test
    @Transactional
    public void getNonExistingProducts() throws Exception {
        // Get the products
        restProductsMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProducts() throws Exception {
        // Initialize the database
        productsService.save(products);

        int databaseSizeBeforeUpdate = productsRepository.findAll().size();

        // Update the products
        Products updatedProducts = productsRepository.findOne(products.getId());
        updatedProducts
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .mainImage(UPDATED_MAIN_IMAGE)
                .mainImageContentType(UPDATED_MAIN_IMAGE_CONTENT_TYPE)
                .image2(UPDATED_IMAGE_2)
                .image2ContentType(UPDATED_IMAGE_2_CONTENT_TYPE)
                .image3(UPDATED_IMAGE_3)
                .image3ContentType(UPDATED_IMAGE_3_CONTENT_TYPE);

        restProductsMockMvc.perform(put("/api/products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProducts)))
                .andExpect(status().isOk());

        // Validate the Products in the database
        List<Products> products = productsRepository.findAll();
        assertThat(products).hasSize(databaseSizeBeforeUpdate);
        Products testProducts = products.get(products.size() - 1);
        assertThat(testProducts.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProducts.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProducts.getMainImage()).isEqualTo(UPDATED_MAIN_IMAGE);
        assertThat(testProducts.getMainImageContentType()).isEqualTo(UPDATED_MAIN_IMAGE_CONTENT_TYPE);
        assertThat(testProducts.getImage2()).isEqualTo(UPDATED_IMAGE_2);
        assertThat(testProducts.getImage2ContentType()).isEqualTo(UPDATED_IMAGE_2_CONTENT_TYPE);
        assertThat(testProducts.getImage3()).isEqualTo(UPDATED_IMAGE_3);
        assertThat(testProducts.getImage3ContentType()).isEqualTo(UPDATED_IMAGE_3_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteProducts() throws Exception {
        // Initialize the database
        productsService.save(products);

        int databaseSizeBeforeDelete = productsRepository.findAll().size();

        // Get the products
        restProductsMockMvc.perform(delete("/api/products/{id}", products.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Products> products = productsRepository.findAll();
        assertThat(products).hasSize(databaseSizeBeforeDelete - 1);
    }
}
