package com.tharunyetti.OrderService.service;

import com.tharunyetti.OrderService.entity.Order;
import com.tharunyetti.OrderService.exception.CustomException;
import com.tharunyetti.OrderService.external.client.PaymentService;
import com.tharunyetti.OrderService.external.client.ProductService;
import com.tharunyetti.OrderService.external.request.PaymentRequest;
import com.tharunyetti.OrderService.external.response.PaymentResponse;
import com.tharunyetti.OrderService.model.OrderRequest;
import com.tharunyetti.OrderService.model.OrderResponse;
import com.tharunyetti.OrderService.model.PaymentMode;
import com.tharunyetti.OrderService.model.ProductResponse;
import com.tharunyetti.OrderService.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private RestTemplate restTemplate;
    @Value("${microservices.product}")
    private String productServiceUrl;
    @Value("${microservices.payment}")
    private String paymentServiceUrl;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @BeforeEach
    public void setup(){
        ReflectionTestUtils
                .setField(orderService, "productServiceUrl", productServiceUrl);
        ReflectionTestUtils
                .setField(orderService, "paymentServiceUrl", paymentServiceUrl);
    }

    @DisplayName("Get Order - Success Scenario")
    @Test
    void test_When_Order_Success(){

        //Mocking for internal calls
        Order order = getMockOrder();

        when(orderRepository.findById(anyLong())) //dummy output for order repo, syntax: Mockito.when(call(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(order));
        when(restTemplate.getForObject(
                productServiceUrl + order.getProductId(), ProductResponse.class))
                .thenReturn(getMockProductResponse());
        when(restTemplate.getForObject(
                paymentServiceUrl + "order/"+order.getId(), PaymentResponse.class))
                .thenReturn(getMockPaymentResponse());

        //Actual for actual method
        OrderResponse orderResponse = orderService.getOrderDetails(1);

        //Verification - how many times internal calls happened
        verify(orderRepository,times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject(
                productServiceUrl + order.getProductId(), ProductResponse.class);
        verify(restTemplate, times(1)).getForObject(
                paymentServiceUrl + "order/"+order.getId(), PaymentResponse.class);

        //Assert for review of results
        assertNotNull(orderResponse); //means we shouldn't get null response
        assertEquals(order.getId(), orderResponse.getOrderId()); //making sure the output id is same as the one we give
    }

    @DisplayName("Get Orders - Failure Scenario")
    @Test
    void test_When_Order_NOT_FOUND_then_not_found(){

        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        //OrderResponse orderResponse = orderService.getOrderDetails(1);

        CustomException exception = assertThrows(CustomException.class,
                ()->orderService.getOrderDetails(1));
        assertEquals("NOT_FOUND",exception.getErrorCode());
        assertEquals(404, exception.getStatus());

        verify(orderRepository, times(1)).findById(anyLong());
    }


    @DisplayName("Place Order - Success Scenario")
    @Test
    void test_When_Place_Order_Success(){
        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L,HttpStatus.OK));

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2)).save(any());
        verify(productService, times(1)).reduceQuantity(anyLong(),anyLong());
        verify(paymentService,times(1)).doPayment(any(PaymentRequest.class));

        assertEquals(order.getId(), orderId);
    }

    @DisplayName("Place Order - Failure Scenario")
    @Test
    void test_When_Place_Order_Payment_Fails_then_Order_Placed(){
        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException());

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2)).save(any());
        verify(productService, times(1)).reduceQuantity(anyLong(),anyLong());
        verify(paymentService,times(1)).doPayment(any(PaymentRequest.class));

        assertEquals(order.getId(), orderId);
    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .productId(1)
                .quantity(10)
                .paymentMode(PaymentMode.CASH)
                .totalAmount(100)
                .build();
    }

    private PaymentResponse getMockPaymentResponse() {
        return PaymentResponse.builder()
                .paymentId(1)
                .paymentDate(Instant.now())
                .paymentMode(PaymentMode.CASH)
                .amount(200)
                .orderId(1)
                .status("ACCEPTED")
                .build();
    }

    private ProductResponse getMockProductResponse() {
        return ProductResponse.builder()
                .productId(2)
                .productName("Iphone")
                .price(100)
                .quantity(200)
                .build();
    }

    private Order getMockOrder() {
        return Order.builder()
                .orderStatus("PLACED")
                .orderDate(Instant.now())
                .id(1)
                .amount(100)
                .quantity(200)
                .productId(2)
                .build();
    }

}