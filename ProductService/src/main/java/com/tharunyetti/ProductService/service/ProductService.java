package com.tharunyetti.ProductService.service;

import com.tharunyetti.ProductService.model.ProductRequest;
import com.tharunyetti.ProductService.model.ProductResponse;

public interface ProductService {
    ProductResponse getProductById(long productId);

    long addProduct(ProductRequest p);

    void reduceQuantity(long productId, long quantity);
}
