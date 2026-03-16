package com.example.service;

import com.example.dto.request.ProductRequestDTO;
import com.example.dto.request.SpecificationRequestDTO;
import com.example.dto.response.ProductResponseDTO;
import com.example.entity.Product;
import com.example.entity.ProductKey;
import com.example.exceptions.ProductExistsException;
import com.example.exceptions.ProductNotFoundException;
import com.example.repository.ProductRepository;
import com.example.repository.ProductSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.ProviderNotFoundException;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO){

        Integer prodId = productRequestDTO.getProdId();
        String prodName = productRequestDTO.getProdName();

        if(productRepository.existsByKeyProdIdAndKeyProdName(prodId, prodName))
            throw new ProductExistsException("Product exists with ID : " + prodId + " and Name : " + prodName);

        Product product = convertToEntity(productRequestDTO);

        Product savedProduct = productRepository.save(product);

        return convertToResponseDTO(savedProduct);
    }

    public List<ProductResponseDTO> getProduct(){
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(this::convertToResponseDTO)           //Also can use .map(n -> convertToResponseDTO(n))
                .toList();
    }

    public Page<ProductResponseDTO> getAllProducts(SpecificationRequestDTO spec, Pageable pageable){

        Specification<Product> specification = ProductSpecification.buildFilter(spec);

        Page<Product> products = productRepository.findAll(specification, pageable);

        return products.map(this::convertToResponseDTO);
    }

    public ProductResponseDTO getProductById(Integer prodId, String prodName){

        Product product = productRepository.findByKeyProdIdAndKeyProdName(prodId, prodName)
                .orElseThrow(() -> new ProviderNotFoundException("Product with ID : " + prodId +
                        " and Name: " + prodName + " does not exits."));

        log.info(String.valueOf(product));

        return convertToResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO updateProduct(Integer prodId, String prodName, ProductRequestDTO requestDTO){

        Product product = productRepository.findByKeyProdIdAndKeyProdName(prodId, prodName)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID : " + prodId +
                        " and Name: " + prodName + " does not exists to update."));


        product.setProdPrice(requestDTO.getProdPrice());
        product.setProdCategory(requestDTO.getProdCategory());
        product.setProdQuantity(requestDTO.getProdQuantity());
        product.setOrderDate(requestDTO.getOrderDate());

        Product updatedProduct = productRepository.save(product);

        return convertToResponseDTO(updatedProduct);
    }


    public ProductResponseDTO deleteProduct(Integer prodId, String prodName){

        Product product = productRepository.findByKeyProdIdAndKeyProdName(prodId, prodName)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID : " + prodId +
                        " and Name : " + prodName + " does not exists."));

        productRepository.delete(product);

        return convertToResponseDTO(product);
    }

    public ProductResponseDTO convertToResponseDTO(Product product){
        return ProductResponseDTO.builder()
                .prodId(product.getKey().getProdId())
                .prodName(product.getKey().getProdName())

                .prodCategory(product.getProdCategory())
                .prodPrice(product.getProdPrice())
                .prodQuantity(product.getProdQuantity())
                .orderDate(product.getOrderDate())
                .build();
    }

    private Product convertToEntity(ProductRequestDTO requestDTO){
        return Product.builder()
                .key(ProductKey.builder()
                        .prodId(requestDTO.getProdId())
                        .prodName(requestDTO.getProdName()).build())

                .prodCategory(requestDTO.getProdCategory())
                .prodPrice(requestDTO.getProdPrice())
                .prodQuantity(requestDTO.getProdQuantity())
                .orderDate(requestDTO.getOrderDate())
                .build();
    }

}
