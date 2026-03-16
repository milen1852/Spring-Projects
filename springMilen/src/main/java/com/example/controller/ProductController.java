package com.example.controller;

import com.example.dto.request.ProductRequestDTO;
import com.example.dto.request.SpecificationRequestDTO;
import com.example.dto.response.ProductResponseDTO;
import com.example.entity.Product;
import com.example.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO){

        log.info("Post /products/add - Adding a new Product with ID : {}", productRequestDTO.getProdId());

        ProductResponseDTO product = productService.createProduct(productRequestDTO);

        log.info("Created product with ID : {} and Name : {}", product.getProdId(), product.getProdName());

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/product")
    public ResponseEntity<List<ProductResponseDTO>> getProduct(){

        log.info("GET /products/view - Fetching all products");

        List<ProductResponseDTO> products = productService.getProduct();

        log.info("Fetched {} products", products.size());

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProducts(@RequestBody SpecificationRequestDTO spec){

        log.info("{}", spec);

        Sort sort = spec.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(spec.getSortField()).descending()
                : Sort.by(spec.getSortField()).ascending();

        Pageable pageable = PageRequest.of(spec.getPage(), spec.getSize(), sort);

        Page<ProductResponseDTO> productPage = productService.getAllProducts(spec, pageable);


        Map<String, Object> response = new HashMap<>();
        response.put("content", productPage.getContent());
        response.put("currentPage", productPage.getNumber());
        response.put("totalPages", productPage.getTotalPages());
        response.put("totalElements", productPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{prodId}/{prodName}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Integer prodId,
                                                         @PathVariable String prodName){

        log.info("GET /product - Fetching product with ID : {} and Name : {}", prodId, prodName);

        ProductResponseDTO product = productService.getProductById(prodId, prodName);

        log.info("Fetched product with ID : {} and Name: {}" , prodId, prodName);

        return ResponseEntity.status(HttpStatus.FOUND).body(product);
    }

    @PutMapping("/product/{prodId}/{prodName}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer prodId, @PathVariable String prodName,
                                                            @Valid @RequestBody ProductRequestDTO requestDTO){

        log.info("PUT /products/update - Updating product with ID: {} and Name : {}", prodId, prodName);

        ProductResponseDTO product = productService.updateProduct(prodId, prodName, requestDTO);

        log.info("Updated product with ID: {} and Name : {}", prodId, prodName);

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/product/{prodId}/{prodName}")
    public ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable Integer prodId, @PathVariable String prodName){

        log.info("DELETE /products/delete/{} - Deleting product with Name : {}", prodId, prodName);

        ProductResponseDTO product = productService.deleteProduct(prodId, prodName);

        log.info("DELETED product with ID: {} and Name: {}", prodId, prodName);

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

}
