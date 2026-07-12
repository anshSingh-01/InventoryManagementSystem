package org.springproject.inventorymangaement.ProductTest;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springproject.inventorymangaement.dtos.ProductDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.services.ProductService;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProductService productService;


    @Test
    void productControllerTest() throws Exception {
        ProductDto productDto = new ProductDto(
                "Laptop Model X", "Dell",
                "High-performance laptop with 16GB RAM and SSD storage",
                OffsetDateTime.parse("2026-06-01T10:15:30+05:30"),
                OffsetDateTime.parse("2026-06-15T14:45:00+05:30")
        );

        StatusSender expectedResponse =
                new StatusSender(StatusCode.SUCCESS, "Product Saved", productDto);

        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();


        when(productService.addProduct(any(ProductDto.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(expectedResponse)));
    }

    @Test
    void productControllerTest_addingList() throws Exception {
        List<ProductDto> productDtos = List.of(
                new ProductDto("Laptop Model X", "Dell", "High-performance laptop with 16GB RAM and SSD storage",
                        OffsetDateTime.parse("2026-06-01T10:15:30+05:30"),
                        OffsetDateTime.parse("2026-06-15T14:45:00+05:30")),

                new ProductDto("Running Shoes", "Nike", "Lightweight shoes designed for marathon runners",
                        OffsetDateTime.parse("2026-05-20T09:00:00+05:30"),
                        OffsetDateTime.parse("2026-06-25T11:30:00+05:30")),

                new ProductDto("Smartphone Pro", "Samsung", "Flagship smartphone with AMOLED display and 5G support",
                        OffsetDateTime.parse("2026-04-10T12:00:00+05:30"),
                        OffsetDateTime.parse("2026-06-28T16:20:00+05:30")),

                new ProductDto("Organic Green Tea", "Tata Tea", "Premium organic tea leaves sourced from Assam",
                        OffsetDateTime.parse("2026-03-05T08:45:00+05:30"),
                        OffsetDateTime.parse("2026-06-30T13:10:00+05:30")),

                new ProductDto("Office Chair Deluxe", "Godrej", "Ergonomic chair with lumbar support and adjustable height",
                        OffsetDateTime.parse("2026-02-18T07:30:00+05:30"),
                        OffsetDateTime.parse("2026-06-29T15:00:00+05:30"))
        );


        StatusSender expectedResponse =
                new StatusSender(StatusCode.SUCCESS, "Saved All Products", productDtos);

        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();


        when(productService.addAllProducts(anyList())).thenReturn(expectedResponse);

        mockMvc.perform(post("/products/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(productDtos)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(expectedResponse)));
    }

}


