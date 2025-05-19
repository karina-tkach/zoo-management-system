package org.university.zoomanagementsystem.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("java:S2696")
public class StripeConfig {
    @Value("${stripe.api.secretKey}")
    private  String secretKey;

    @PostConstruct
    public void  initSecretKey(){
        Stripe.apiKey = secretKey;
    }
}
