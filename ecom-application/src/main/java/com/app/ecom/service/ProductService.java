package com.app.ecom.service;

import com.app.ecom.dto.product.ProductRequest;
import com.app.ecom.dto.product.ProductResponse;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    public final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByIsActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductById(Long id){
        return productRepository.findById(id).map(this::mapToProductResponse);
    }

    public ProductResponse createProduct(ProductRequest product){
        Product prod = new Product();
        mapToProduct(product,prod);
        Product savedProduct = productRepository.save(prod);
        return mapToProductResponse(savedProduct);
    }

    private void mapToProduct(ProductRequest product, Product prod) {
        prod.setName(product.getName());
        prod.setDescription(product.getDescription());
        prod.setPrice(product.getPrice());
        prod.setCategory(product.getCategory());
        prod.setUnits(product.getUnits());
        prod.setImageUrl(product.getImageUrl());
    }

    public ProductResponse mapToProductResponse(Product product){
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setUnits(product.getUnits());
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setIsActive(product.getIsActive());

        return response;
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest product) {
        return productRepository.findById(id)
                .map(
                        existingProduct -> {
                            mapToProduct(product,existingProduct);
                            Product savedProd = productRepository.save(existingProduct);
                            return mapToProductResponse(savedProd);
                        }
                );
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(
                        prod -> {
                            prod.setIsActive(Boolean.FALSE);
                            productRepository.save(prod);
                            return true;
                        }
                ).orElse(false);
    }

    public List<ProductResponse> searchProducts(String key) {
        return productRepository.searchProducts(key).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
}
