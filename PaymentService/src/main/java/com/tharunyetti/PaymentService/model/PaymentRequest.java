package com.tharunyetti.PaymentService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PaymentRequest {
    private long orderId;
    private long amount;
    private String referenceNumber;
    private PaymentMode paymentMode;
}
