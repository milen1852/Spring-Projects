package com.example.inventorySpring.controller;

import com.example.inventorySpring.dto.request.ProductRequestDTO;
import com.example.inventorySpring.dto.request.UpdateProductRequestDTO;
import com.example.inventorySpring.dto.response.ProductResponseDTO;
import com.example.inventorySpring.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody
                                                                 ProductRequestDTO productRequestDTO){
        log.info("POST /products/add - Adding a Product");

        ProductResponseDTO productResponseDTO = productService.createProduct(productRequestDTO);

        if(productResponseDTO != null)
            log.info("Created a product with ID: {}",productResponseDTO.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    @GetMapping("/view")
    public ResponseEntity<List<ProductResponseDTO>> getProducts(){

        log.info("GET /products/view - Fetching All Products");

        List<ProductResponseDTO> productResponseDTO = productService.getProducts();

        if(productResponseDTO != null)
            log.info("Fetched {} products", productResponseDTO.size());

        return ResponseEntity.ok(productResponseDTO);
    }

    @GetMapping("/view/id/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Integer productId){

        log.info("GET /products/view/id/ - Fetching Products with a specific Product ID");

        ProductResponseDTO productResponseDTO = productService.getProductByID(productId);

        if(productResponseDTO != null)
            log.info("Fetched product with Product ID: {}", productId);

        return ResponseEntity.ok(productResponseDTO);
    }

    @GetMapping("/view/{prodCategory}")
    public ResponseEntity<List<ProductResponseDTO>> getCategoryProducts(@PathVariable String prodCategory){

        log.info("GET /products/view/ - Fetching All Products with specific Product Category");

        List<ProductResponseDTO> productResponseDTO = productService.getCategoryProducts(prodCategory);

        if(productResponseDTO != null)
            log.info("Fetched {} products with Product Category: {}", productResponseDTO.size(), prodCategory);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productResponseDTO);
    }

    @GetMapping("/view/top3")
    public ResponseEntity<List<ProductResponseDTO>> getExpensiveProducts(){
        log.info("GET /products/view/top3 - Fetching Top 3 Expensive Products");

        List<ProductResponseDTO> productResponseDTO = productService.getExpensiveProducts();

        if(productResponseDTO != null)
            log.info("Fetched Top 3 Expensive Products");

        return ResponseEntity.ok(productResponseDTO);
    }

    @GetMapping("/view/price/{prodPrice}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPrice(@PathVariable Double prodPrice){
        log.info("GET /products/view/price - Fetching Products with Price > {}", prodPrice);

        List<ProductResponseDTO> productResponseDTO = productService.getProductsByPrice(prodPrice);

        return ResponseEntity.ok(productResponseDTO);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Integer productId,
                                                            @Valid @RequestBody UpdateProductRequestDTO
                                                                    updateProductRequestDTO){

        log.info("PUT /products/update/ - Updating a Product with specific ID");

        ProductResponseDTO productResponseDTO = productService.updateProduct(productId, updateProductRequestDTO);

        if(productResponseDTO != null)
            log.info("Updated Product with ID: {}", productId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productResponseDTO);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable Integer productId){

        log.info("DELETE /products/delete - Deleting product with a specific ID");

        ProductResponseDTO productResponseDTO = productService.deleteProduct(productId);

        if(productResponseDTO != null) {
            log.info("Deleted product with Product ID: {}",productId );
        }

        return ResponseEntity.ok(productResponseDTO);
    }
}
