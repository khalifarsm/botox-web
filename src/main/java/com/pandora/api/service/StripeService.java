package com.pandora.api.service;

import com.pandora.api.dto.SessionDTO;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StripeService {

    @Value("${stripe.keys.secret}")
    private String API_SECRET_KEY;

    public StripeService() {
    }

    @SneakyThrows
    public Session getSession(String sessionId) {
        Stripe.apiKey = API_SECRET_KEY;
        Session session = Session.retrieve(sessionId);
        return session;
    }

    @SneakyThrows
    public SessionDTO createPaymentSession(double amount) {
        String id = UUID.randomUUID().toString();
        Stripe.apiKey = API_SECRET_KEY;
        String description = "Botox Subscription";
        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) amount * 100) // Amount in cents
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Botox Subscription")
                                                .setDescription(description)
                                                .build()
                                )
                                .build()
                )
                .build();
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setSuccessUrl("https://botox.pandorachat.io/payment/success/" + id)
                        .addLineItem(lineItem)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setAllowPromotionCodes(true)
                        .build();
        Session session = Session.create(params);
        return new SessionDTO().setSession(session).setId(id);
    }
}
