package com.tharunyetti.ProductService.service;

import com.tharunyetti.ProductService.entity.Product;
import com.tharunyetti.ProductService.exception.ProductServiceCustomException;
import com.tharunyetti.ProductService.model.ProductRequest;
import com.tharunyetti.ProductService.model.ProductResponse;
import com.tharunyetti.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    @Autowired
    private  ProductRepository productRepository;

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get the product for product id:{}",productId);
//        Product p = productRepository.findById(productId)
//                .orElseThrow(()-> new RuntimeException("Product with given Id not found"));
        Product p = productRepository.findById(productId)
                .orElseThrow(()-> new ProductServiceCustomException("Product with given Id not found","PRODUCT_NOT_FOUND"));
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(p,productResponse);
        return productResponse;
    }

    @Override
    public long addProduct(ProductRequest p) {
        log.info("Adding Product..");
        Product product = Product.builder()
                .productName(p.getName())
                .quantity(p.getQuantity())
                .price(p.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product Created!");
        return product.getProductId();
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reducing quantity {} for id {}",quantity,productId);
        Product product =
                productRepository.findById(productId)
                        .orElseThrow(()->new ProductServiceCustomException("Product with given id not found","PRODUCT_NOT_FOUND"));
        if(product.getQuantity()<quantity){
            throw new ProductServiceCustomException(
                    "PRODUCT DOES NOT HAVE SUFFICIENT QUANTITY"
                    ,"INSUFFICIENT_QUANTITY");
        }
        product.setQuantity(product.getQuantity()-quantity);
        productRepository.save(product);
        log.info("Product Quantity is updated successfully");
    }
}
