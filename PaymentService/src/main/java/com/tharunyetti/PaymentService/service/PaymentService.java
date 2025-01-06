package com.tharunyetti.PaymentService.service;

import com.tharunyetti.PaymentService.model.PaymentRequest;
import com.tharunyetti.PaymentService.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
