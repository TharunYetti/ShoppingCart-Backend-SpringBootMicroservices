// package com.tharunyetti.OrderService.controller;

// import com.fasterxml.jackson.databind.DeserializationFeature;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
// import com.tharunyetti.OrderService.OrderServiceConfig;
// import com.tharunyetti.OrderService.entity.Order;
// import com.tharunyetti.OrderService.model.OrderRequest;
// import com.tharunyetti.OrderService.model.PaymentMode;
// import com.tharunyetti.OrderService.repository.OrderRepository;
// import com.tharunyetti.OrderService.service.OrderService;
// import jakarta.ws.rs.core.MediaType;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.RegisterExtension;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.HttpStatus;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// import java.io.IOException;
// import java.util.Optional;

// import static com.github.tomakehurst.wiremock.client.WireMock.*;
// import static java.nio.charset.Charset.*;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
// import static org.springframework.util.StreamUtils.*;

// @SpringBootTest({"server.port=0"}) // setting server.port=0,not to depend on any port
// @EnableConfigurationProperties //to get all the configurations of yaml file of the main application
// @AutoConfigureMockMvc //for using directly apis(rather than methods), we have to mock them
// @ContextConfiguration(classes = {OrderServiceConfig.class}) //for getting beans of configurations
// public class OrderControllerTest {

//     @Autowired
//     private OrderService orderService;

//     @Autowired
//     private OrderRepository orderRepository;

//     @Autowired
//     private MockMvc mockMvc;

//     @RegisterExtension
//     static WireMockExtension wireMockServer = WireMockExtension.newInstance()

//             .build();
// //            WireMockExtension.newInstance()
// //                    .options(WireMockConfiguration
// //                            .wireMockConfig()
// //                            .port(8080))
// //                    .build();

//     //as we need to convert objects to json data, we need object mapper that converts objects<->json
//     private ObjectMapper objectMapper
//             = new ObjectMapper()
//             .findAndRegisterModules()
//             .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//             .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//     @BeforeEach
//     void setup() throws IOException {
//         getProductDetailsResponse();
//         doPayment();
//         getPaymentDetails();
//         reduceQuantity();
//     }

//     private void reduceQuantity() {
//         wireMockServer.stubFor(put(urlMatching("/product/reduceQuantity/.*"))
//                 .willReturn(aResponse()
//                         .withStatus(HttpStatus.OK.value())
//                         .withHeader("Content-Type", MediaType.APPLICATION_JSON)));
//     }

//     private void getPaymentDetails() throws IOException {
//         wireMockServer.stubFor(get(urlMatching("/payment/.*"))
//                 .willReturn(aResponse()
//                         .withStatus(HttpStatus.OK.value())
//                         .withHeader("Content-Type", MediaType.APPLICATION_JSON)
//                         .withBody(copyToString(
//                                 OrderControllerTest.class
//                                         .getClassLoader()
//                                         .getResourceAsStream("mock/GetPayment.json"),
//                                 defaultCharset()
//                         ))));
//     }

//     private void doPayment() {
//         wireMockServer.stubFor(post(urlEqualTo("/payment"))
//                 .willReturn(aResponse()
//                         .withStatus(HttpStatus.OK.value())
//                         .withHeader("Content-Type", MediaType.APPLICATION_JSON)));
//     }

//     private void getProductDetailsResponse() throws IOException {
//         // Get api call : /product/1
//         wireMockServer.stubFor(get("/product/1")
//                 .willReturn(aResponse()
//                         .withStatus(HttpStatus.OK.value())
//                         .withHeader("Content-Type", MediaType.APPLICATION_JSON)
//                         .withBody(copyToString(
//                                 OrderControllerTest.class
//                                         .getClassLoader()
//                                         .getResourceAsStream("mock/GetProduct.json"),
//                                 defaultCharset()
//                         ))));
//     }

//     @DisplayName("Place Order Integration - Success Scenario")
//     @Test
//     public void test_WhenPlaceOrder_DoPayment_Success() throws Exception {
//         //First place order
//         //Get order by order Id from db and check
//         //check the output

//         OrderRequest orderRequest = getMockOrderRequest();
//         MvcResult mvcResult
//                 = mockMvc.perform(MockMvcRequestBuilders.post("/order/placeOrder")
//                 .with(jwt().authorities(new SimpleGrantedAuthority("Customer")))
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(orderRequest))
//         ).andExpect(MockMvcResultMatchers.status().isOk())
//                 .andReturn();

//         String orderId = mvcResult.getResponse().getContentAsString();

//         Optional<Order> order = orderRepository.findById(Long.valueOf(orderId));
//         assertTrue(order.isPresent());

//         Order o = order.get();
//         assertEquals(Long.parseLong(orderId),o.getId());
//         assertEquals("PLACED",o.getOrderStatus());
//         assertEquals(orderRequest.getTotalAmount(),o.getAmount());
//         assertEquals(orderRequest.getTotalAmount(),o.getAmount());
//         assertEquals(orderRequest.getQuantity(),o.getQuantity());

//     }

//     @Test
//     public void test_WhenPlaceOrdWithWrongAccess_thenThrow403()throws Exception{
//         OrderRequest orderRequest = getMockOrderRequest();
//         MvcResult mvcResult
//                 = mockMvc.perform(MockMvcRequestBuilders.post("/order/placeOrder")
//                         .with(jwt().authorities(new SimpleGrantedAuthority("Admin")))
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(orderRequest))
//                 ).andExpect(MockMvcResultMatchers.status().isForbidden())
//                 .andReturn();
//     }

//     private OrderRequest getMockOrderRequest() {
//         return OrderRequest.builder()
//                 .productId(1)
//                 .paymentMode(PaymentMode.CASH)
//                 .quantity(10)
//                 .totalAmount(200)
//                 .build();
//     }
// }