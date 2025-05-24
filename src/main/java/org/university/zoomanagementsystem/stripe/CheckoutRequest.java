package org.university.zoomanagementsystem.stripe;
public class CheckoutRequest {
    private String successUrl;
    private String cancelUrl;
    private Long amount;

    public CheckoutRequest() {
        this.successUrl = null;
        this.cancelUrl = null;
        this.amount = null;
    }

    public CheckoutRequest(String successUrl, String cancelUrl, Long amount) {
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
        this.amount = amount;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
