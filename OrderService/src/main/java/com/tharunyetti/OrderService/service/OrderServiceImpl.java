package com.tharunyetti.OrderService.service;

import com.tharunyetti.OrderService.entity.Order;
import com.tharunyetti.OrderService.exception.CustomException;
import com.tharunyetti.OrderService.external.client.PaymentService;
import com.tharunyetti.OrderService.external.client.ProductService;
import com.tharunyetti.OrderService.external.request.PaymentRequest;
import com.tharunyetti.OrderService.external.response.PaymentResponse;
import com.tharunyetti.OrderService.model.OrderRequest;
import com.tharunyetti.OrderService.model.OrderResponse;
import com.tharunyetti.OrderService.model.ProductResponse;
import com.tharunyetti.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Log4j2
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${microservices.product}")
    private String productServiceUrl;
    @Value("${microservices.payment}")
    private String paymentServiceUrl;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //Order Entity -> Save the data with Status Order Created
        //Product Service -> Block products (Reduce the Quantity)
        //Payment Service -> Payments -> Success -> COMPLETE, Else CANCELLED
        log.info("Placing Order Request :{}",orderRequest);
        //calling product service to reduce quantity and check if there is any error
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        log.info("Creating order with Status CREATED");
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order = orderRepository.save(order);

        log.info("Calling payment service to complete the payment");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();
        String orderStatus = null;
        try{
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully. Changing order status to placed");
            orderStatus = "PLACED";
        }catch(Exception e){
            log.error("Error occured in payment. Changing order status to failed");
            orderStatus = "PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Order Placed Successfully with Order Id:{}",order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Getting order details for the id:{}",orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new CustomException("Not found for the order Id:"+orderId,"NOT_FOUND", 404));

        log.info("Getting product details for the product id:{}",order.getProductId());
        ProductResponse productResponse = restTemplate.getForObject(
                productServiceUrl + order.getProductId(),ProductResponse.class
        );
//        ProductResponse productResponse = restTemplate.getForObject(
//                "http://PRODUCT-SERVICE/product/"+order.getProductId(),ProductResponse.class
//        );
        //converting ProductResponse object into ProductDetails object
        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productId(productResponse.getProductId())
                .price(productResponse.getPrice())
                .productName(productResponse.getProductName())
                .quantity(productResponse.getQuantity())
                .build();

        log.info("Getting payment details from the payment service");
        PaymentResponse paymentResponse = restTemplate.getForObject(
                paymentServiceUrl + "order/" + order.getId(),
                PaymentResponse.class
        );
//        PaymentResponse paymentResponse = restTemplate.getForObject(
//                "http://PAYMENT-SERVICE/payment/order/"+order.getId(),
//                PaymentResponse.class
//        );
        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentStatus(paymentResponse.getStatus())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();


        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
        return orderResponse;
    }
}
