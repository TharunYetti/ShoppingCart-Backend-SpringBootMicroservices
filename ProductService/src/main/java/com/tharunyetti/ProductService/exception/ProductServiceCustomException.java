package com.tharunyetti.ProductService.exception;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ProductServiceCustomException extends RuntimeException{
    private String errorCode;
    public ProductServiceCustomException(String msg, String errorCode){
        super(msg);
        this.errorCode=errorCode;
    }
}
