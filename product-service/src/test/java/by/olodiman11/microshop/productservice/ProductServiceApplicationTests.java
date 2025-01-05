package by.olodiman11.microshop.productservice;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;

import by.olodiman11.microshop.productservice.dto.ProductRequest;
import by.olodiman11.microshop.productservice.dto.ProductResponse;
import by.olodiman11.microshop.productservice.model.Product;
import by.olodiman11.microshop.productservice.repository.ProductRepository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.IndicativeSentences.class)
@Disabled
class ProductServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@Container
	private static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

	@DynamicPropertySource
	private static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void createProduct_createsProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productReqiestJson = objectMapper.writeValueAsString(productRequest);
		mockMvc
			.perform(MockMvcRequestBuilders
				.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productReqiestJson))
			.andExpect(status().isCreated());	
		assertEquals(1, productRepository.findAll().size());		
	}

	@Test
	void getAllProducts_returnsAllProducts() throws Exception {
		List<Product> products = getProductList();
		productRepository.saveAll(products);

		MvcResult mvcResult = mockMvc
			.perform(MockMvcRequestBuilders
				.get("/api/product")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();
		String responseJson = mvcResult.getResponse().getContentAsString();
		ProductResponse[] responses = objectMapper.readValue(responseJson, ProductResponse[].class);
		assertEquals(3, responses.length);		
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
			.name("IPhone 13")
			.description("iphone 13")
			.price(BigDecimal.valueOf(1200))
			.build();
	}

	private List<Product> getProductList() {
		Product product1 = new Product();
		product1.setName("IPhone 13");
		product1.setDescription("iphone 13");
		product1.setPrice(BigDecimal.valueOf(1200));

		Product product2 = new Product();
		product2.setName("IPhone 14");
		product2.setDescription("iphone 14");
		product2.setPrice(BigDecimal.valueOf(2400));
		
		Product product3 = new Product();
		product3.setName("IPhone 15");
		product3.setDescription("iphone 15");
		product3.setPrice(BigDecimal.valueOf(3000));
		
		return List.of(product1, product2, product3);
	}
}
