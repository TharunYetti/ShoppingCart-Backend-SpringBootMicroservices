package com.tharunyetti.OrderService.external.client;

import com.tharunyetti.OrderService.exception.CustomException;
import com.tharunyetti.OrderService.external.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
//@FeignClient(name = "PAYMENT-SERVICE/payment")
@FeignClient(name = "payment", url = "${microservices.payment}")
public interface PaymentService {
    @PostMapping
    ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    default ResponseEntity<Long> fallback(Exception e){
        throw new CustomException(
                "Payment service is not avaiable",
                "UNAVAILABLE",
                500
        );
    }

}
