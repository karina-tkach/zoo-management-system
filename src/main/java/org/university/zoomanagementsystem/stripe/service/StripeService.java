package org.university.zoomanagementsystem.stripe.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.university.zoomanagementsystem.stripe.CheckoutRequest;
import org.university.zoomanagementsystem.stripe.CheckoutResponse;

import java.time.LocalDate;


@Service
public class StripeService {

    @Value("${stripe.api.secretKey}")
    private String stripeApiKey;

    public CheckoutResponse createCheckoutSession(String fullName, String email, String ticketType,
                                                  String visitType, LocalDate visitDate,
                                                  Integer excursionId, CheckoutRequest checkoutRequest) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        // Create Checkout Session parameters
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(checkoutRequest.getSuccessUrl() + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(checkoutRequest.getCancelUrl())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(checkoutRequest.getAmount() * 100L)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Zoo ticket")
                                                                .build())
                                                .build())
                                .build())
                .putMetadata("fullName", fullName)
                .putMetadata("email", email)
                .putMetadata("ticketType", ticketType)
                .putMetadata("visitType", visitType)
                .putMetadata("visitDate", String.valueOf(visitDate))
                .putMetadata("excursionId", String.valueOf(excursionId))
                .build();

        // Create the session
        Session session = null;
        CheckoutResponse response = new CheckoutResponse();
        try {
            session = Session.create(params);
            response.setCheckoutId(session.getId());
            response.setStatus("SUCCESS");
            response.setCheckoutLink(session.getUrl());
            response.setMessage("Session created");

        } catch (StripeException e) {
            System.out.println("Error creating Stripe session: {}"+ e.getMessage());

            // Return an error response
            CheckoutResponse errorResponse = new CheckoutResponse();
            errorResponse.setStatus("failure");
            errorResponse.setMessage("Failed to create Stripe session");
            return errorResponse;
        }

        return response;
    }
    public Session getSessionDetails(String sessionId) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        return Session.retrieve(sessionId);
    }
}

