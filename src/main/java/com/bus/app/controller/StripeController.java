package com.bus.app.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/stripe-payment")
public class StripeController {

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody final Map<String, Object> requestData) throws StripeException {
        final Long amount = Long.valueOf(requestData.get("amount").toString());
        final String currency = requestData.get("currency").toString();
        final String description = requestData.get("description").toString();
        final String email = requestData.get("email").toString();
        final String successUrl = requestData.get("successUrl").toString();
        final String cancelUrl = requestData.get("cancelUrl").toString();
        final SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .setCustomerEmail(email)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount(amount)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(description)
                                                                .build())
                                                .build())
                                .setQuantity(1L)
                                .build())
                .build();

        final Session session = Session.create(params);
        Session.retrieve(session.getId());

        return ResponseEntity.ok(Map.of("sessionId", session.getId()));
    }
}